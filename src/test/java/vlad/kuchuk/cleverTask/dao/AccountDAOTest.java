package vlad.kuchuk.cleverTask.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private AccountDAO accountDAO;

    @Test
    @DisplayName("getById")
    void testGetAccountById() throws SQLException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("account_number")).thenReturn("12345");
        when(resultSet.getInt("person_id")).thenReturn(1);
        when(resultSet.getInt("bank_id")).thenReturn(2);
        when(resultSet.getBigDecimal("balance")).thenReturn(new BigDecimal(123));
        when(resultSet.getDate("last_interest_calculation_date")).thenReturn(new Date(System.currentTimeMillis()));
        when(resultSet.getInt("id")).thenReturn(1);

        Account account = accountDAO.getById(1);

        assertNotNull(account);
        assertEquals("12345", account.getAccountNumber());
        assertEquals(1, account.getPersonId());
        assertEquals(2, account.getBankId());
        assertEquals(new BigDecimal(123), account.getBalance());
        assertNotNull(account.getLastInterestCalculationDate());
        assertEquals(1, account.getId());
    }

    @Test
    @DisplayName("getByAccountNumber")
    void testGetAccountByAccountNumber() throws SQLException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("account_number")).thenReturn("12345");
        when(resultSet.getInt("person_id")).thenReturn(1);
        when(resultSet.getInt("bank_id")).thenReturn(2);
        when(resultSet.getBigDecimal("balance")).thenReturn(new BigDecimal(123));
        when(resultSet.getDate("last_interest_calculation_date")).thenReturn(new Date(System.currentTimeMillis()));
        when(resultSet.getInt("id")).thenReturn(1);

        Account account = accountDAO.getByAccountNumber("12345");

        assertNotNull(account);
        assertEquals("12345", account.getAccountNumber());
        assertEquals(1, account.getPersonId());
        assertEquals(2, account.getBankId());
        assertEquals(new BigDecimal(123), account.getBalance());
        assertNotNull(account.getLastInterestCalculationDate());
        assertEquals(1, account.getId());
    }

    @Test
    @DisplayName("getAllAccountsForUser")
    void testGetAllAccountsForUser() throws SQLException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("account_number")).thenReturn("12345");
        when(resultSet.getInt("person_id")).thenReturn(1);
        when(resultSet.getInt("bank_id")).thenReturn(2);
        when(resultSet.getBigDecimal("balance")).thenReturn(new BigDecimal(123));
        when(resultSet.getDate("last_interest_calculation_date")).thenReturn(new Date(System.currentTimeMillis()));
        when(resultSet.getInt("id")).thenReturn(1);

        List<Account> accounts = accountDAO.getAllAccountsForUser(1);

        assertEquals(2, accounts.size());
        Account account = accounts.get(0);
        assertEquals("12345", account.getAccountNumber());
        assertEquals(1, account.getPersonId());
        assertEquals(2, account.getBankId());
        assertEquals(new BigDecimal(123), account.getBalance());
        assertNotNull(account.getLastInterestCalculationDate());
        assertEquals(1, account.getId());
    }
}

