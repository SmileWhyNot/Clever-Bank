package vlad.kuchuk.cleverTask.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Person;
import vlad.kuchuk.cleverTask.service.AccountService;
import vlad.kuchuk.cleverTask.service.BankService;
import vlad.kuchuk.cleverTask.service.PersonService;
import vlad.kuchuk.cleverTask.service.TransactionService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @DisplayName("isSuccessfulLoginAndDeposit")
    void testSuccessfulLoginAndDeposit() {
        Person user = new Person("User", "user@example.com");
        List<Account> userAccounts = new ArrayList<>();
        Account account = new Account("12345", user.getId(), 1);
        userAccounts.add(account);

        when(personService.authenticate("user@example.com")).thenReturn(user);
        when(accountService.getAccountsByOwner(user.getId())).thenReturn(userAccounts);

        String input = "user@example.com\n1\n1\n100,0\n4\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        System.setOut(new java.io.PrintStream(System.out));

        consoleOperations.start(scanner);

        verify(transactionService).depositMoney(account.getId(), BigDecimal.valueOf(100.00));
    }

    @Test
    @DisplayName("invalidLogin")
    void testInvalidLogin() {
        when(personService.authenticate("invalid@example.com")).thenReturn(null);

        String input = "invalid@example.com\n4\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        System.setOut(new java.io.PrintStream(System.out));

        consoleOperations.start(scanner);
        verifyNoInteractions(transactionService);
    }

    @Test
    @DisplayName("shouldWithdrawFunds")
    void testWithdrawFunds() {
        Person user = new Person("User", "user@example.com");
        List<Account> userAccounts = new ArrayList<>();
        Account account = new Account("12345", user.getId(), 1);
        account.setBalance(BigDecimal.valueOf(100));
        userAccounts.add(account);

        when(personService.authenticate("user@example.com")).thenReturn(user);
        when(accountService.getAccountsByOwner(user.getId())).thenReturn(userAccounts);

        String input = "user@example.com\n2\n1\n100,0\n4\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        System.setOut(new java.io.PrintStream(System.out));

        consoleOperations.start(scanner);

        verify(transactionService).withdrawMoney(account.getId(), BigDecimal.valueOf(100.00));
    }

    @Test
    @DisplayName("shouldPerformMoney")
    void testPerformMoneyTransfer() {
        Person user = new Person("User", "user@example.com");
        List<Account> userAccounts = new ArrayList<>();
        Account senderAccount = new Account("12345", user.getId(), 1);
        senderAccount.setBalance(BigDecimal.valueOf(100));
        Account receiverAccount = new Account("54321", 2,2);
        receiverAccount.setBalance(BigDecimal.valueOf(100));
        userAccounts.add(senderAccount);

        when(personService.authenticate("user@example.com")).thenReturn(user);
        when(accountService.getAccountsByOwner(user.getId())).thenReturn(userAccounts);
        when(accountService.getAccountDataByNumber("54321")).thenReturn(receiverAccount);

        String input = "user@example.com\n3\n1\n54321\n100,0\n4\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        System.setOut(new java.io.PrintStream(System.out));

        consoleOperations.start(scanner);

        verify(transactionService).executeMoneyTransfer(
                senderAccount.getId(),
                "54321",
                BigDecimal.valueOf(100.0)
        );
    }

    @Test
    @DisplayName("userHasNoAccounts")
    void testNoAccountsForOperation() {
        Person user = new Person("User", "user@example.com");
        List<Account> userAccounts = new ArrayList<>();

        when(personService.authenticate("user@example.com")).thenReturn(user);
        when(accountService.getAccountsByOwner(user.getId())).thenReturn(userAccounts);

        String input = "user@example.com\n3\n1\n2\n4\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        consoleOperations.start(scanner);

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("У вас нет счетов."));

        System.setOut(System.out);
    }
}

