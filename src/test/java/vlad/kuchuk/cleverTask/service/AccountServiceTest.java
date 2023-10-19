package vlad.kuchuk.cleverTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
    void testGetAccountDataByNumber() {
        String accountNumber = "12345";
        Account account = new Account(accountNumber, 1, 10);

        when(accountDAO.getByAccountNumber(accountNumber)).thenReturn(account);

        Account result = accountService.getAccountDataByNumber(accountNumber);

        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
    }

    @Test
    void testGetAccountsByOwnerWhenNoAccounts() {
        int ownerId = 1;
        List<Account> emptyList = new ArrayList<>();

        when(accountDAO.getAllAccountsForUser(ownerId)).thenReturn(emptyList);

        List<Account> result = accountService.getAccountsByOwner(ownerId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
