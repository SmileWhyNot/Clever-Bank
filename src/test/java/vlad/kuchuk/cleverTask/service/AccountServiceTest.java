package vlad.kuchuk.cleverTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.model.Account;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountDAO accountDAO;

    @Test
    @DisplayName("getAccountByOwner")
    void testGetAccountsByOwner() {
        int ownerId = 1;
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("12345", ownerId, 10));
        accounts.add(new Account( "54321", ownerId, 20));

        when(accountDAO.getAllAccountsForUser(ownerId)).thenReturn(accounts);

        List<Account> result = accountService.getAccountsByOwner(ownerId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("getAccountDataByNumber")
    void testGetAccountDataByNumber() {
        String accountNumber = "12345";
        Account account = new Account(accountNumber, 1, 10);

        when(accountDAO.getByAccountNumber(accountNumber)).thenReturn(account);

        Account result = accountService.getAccountDataByNumber(accountNumber);

        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
    }

    @Test
    @DisplayName("getAccountByOwnerZeroAccounts")
    void testGetAccountsByOwnerWhenNoAccounts() {
        int ownerId = 1;
        List<Account> emptyList = new ArrayList<>();

        when(accountDAO.getAllAccountsForUser(ownerId)).thenReturn(emptyList);

        List<Account> result = accountService.getAccountsByOwner(ownerId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("updateAccountById")
    void testUpdateAccountById() {
        int accountId = 1;
        Account updatedAccount = new Account("54321", 2, 30);

        doNothing().when(accountDAO).updateAccount(updatedAccount, accountId);

        accountService.updateAccountById(updatedAccount, accountId);

        verify(accountDAO, times(1)).updateAccount(updatedAccount, accountId);
    }

    @Test
    @DisplayName("getAccountById")
    void testGetAccountById() {
        int accountId = 1;
        Account account = new Account("12345", 1, 10);

        when(accountDAO.getById(accountId)).thenReturn(account);

        Account result = accountService.getAccountById(accountId);

        assertNotNull(result);
        assertEquals(account.getAccountNumber(), result.getAccountNumber());
    }

    @Test
    @DisplayName("createAccount")
    void testCreateAccount() {
        Account newAccount = new Account("99999", 3, 50);

        doNothing().when(accountDAO).addAccount(newAccount);

        accountService.createAccount(newAccount);

        verify(accountDAO, times(1)).addAccount(newAccount);
    }

    @Test
    @DisplayName("deleteAccountById")
    void testDeleteAccountById() {
        int accountId = 1;

        doNothing().when(accountDAO).deleteById(accountId);

        accountService.deleteAccountById(accountId);

        verify(accountDAO, times(1)).deleteById(accountId);
    }

    @Test
    @DisplayName("getAllAccounts")
    void testGetAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("11111", 1, 10));
        accounts.add(new Account("22222", 2, 20));

        when(accountDAO.getAllAccounts()).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts();

        assertNotNull(result);
        assertEquals(accounts.size(), result.size());
    }
}
