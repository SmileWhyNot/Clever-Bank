package vlad.kuchuk.cleverTask.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Person;
import vlad.kuchuk.cleverTask.service.AccountService;
import vlad.kuchuk.cleverTask.service.BankService;
import vlad.kuchuk.cleverTask.service.PersonService;
import vlad.kuchuk.cleverTask.service.TransactionService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankingConsoleOperationsTest {

    @Mock
    private AccountService accountService;

    @Mock
    private PersonService personService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private BankService bankService;

    @InjectMocks
    private BankingConsoleOperations consoleOperations;

    @Test
    void testSuccessfulLoginAndDeposit() {
        // Prepare test data
        Person user = new Person("User", "user@example.com");
        List<Account> userAccounts = new ArrayList<>();
        Account account = new Account("12345", user.getId(), 1);
        userAccounts.add(account);

        // Configure mock behavior
        when(personService.authenticate("user@example.com")).thenReturn(user);
        when(accountService.getAccountsByOwner(user.getId())).thenReturn(userAccounts);

        // Simulate user input
        String input = "user@example.com\n1\n1\n100,0\n4\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Redirect System.out to capture output
        System.setOut(new java.io.PrintStream(System.out));

        // Run the console operations
        consoleOperations.start(scanner);

        // Verify expected interactions with services
        verify(transactionService).depositMoney(account.getId(), BigDecimal.valueOf(100.00));
    }

    @Test
    void testInvalidLogin() {
        // Configure mock behavior for invalid login
        when(personService.authenticate("invalid@example.com")).thenReturn(null);

        // Simulate user input
        String input = "invalid@example.com\n4\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Redirect System.out to capture output
        System.setOut(new java.io.PrintStream(System.out));

        // Run the console operations
        consoleOperations.start(scanner);

        // Verify that no transactions were performed
        verifyNoInteractions(transactionService);
    }

    @Test
    void testWithdrawFunds() {
        // Prepare test data
        Person user = new Person("User", "user@example.com");
        List<Account> userAccounts = new ArrayList<>();
        Account account = new Account("12345", user.getId(), 1);
        account.setBalance(BigDecimal.valueOf(100));
        userAccounts.add(account);

        // Configure mock behavior
        when(personService.authenticate("user@example.com")).thenReturn(user);
        when(accountService.getAccountsByOwner(user.getId())).thenReturn(userAccounts);

        // Simulate user input
        String input = "user@example.com\n2\n1\n100,0\n4\n"; // Choose option 2 for withdrawing
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Redirect System.out to capture output
        System.setOut(new java.io.PrintStream(System.out));

        // Run the console operations
        consoleOperations.start(scanner);

        // Verify expected interactions with services
        verify(transactionService).withdrawMoney(account.getId(), BigDecimal.valueOf(100.00));
    }

    @Test
    void testPerformMoneyTransfer() {
        // Prepare test data
        Person user = new Person("User", "user@example.com");
        List<Account> userAccounts = new ArrayList<>();
        Account senderAccount = new Account("12345", user.getId(), 1);
        senderAccount.setBalance(BigDecimal.valueOf(100));
        Account receiverAccount = new Account("54321", 2,2);
        receiverAccount.setBalance(BigDecimal.valueOf(100));
        userAccounts.add(senderAccount);

        // Configure mock behavior
        when(personService.authenticate("user@example.com")).thenReturn(user);
        when(accountService.getAccountsByOwner(user.getId())).thenReturn(userAccounts);
        when(accountService.getAccountDataByNumber("54321")).thenReturn(receiverAccount);

        // Simulate user input
        String input = "user@example.com\n3\n1\n54321\n100,0\n4\n"; // Choose option 3 for money transfer
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Redirect System.out to capture output
        System.setOut(new java.io.PrintStream(System.out));

        // Run the console operations
        consoleOperations.start(scanner);

        // Verify expected interactions with services
        verify(transactionService).executeMoneyTransfer(
                senderAccount.getId(),
                "54321", // Receiver's account number
                BigDecimal.valueOf(100.0)
        );
    }
}

