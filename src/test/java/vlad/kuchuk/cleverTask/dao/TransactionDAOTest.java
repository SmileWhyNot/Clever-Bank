package vlad.kuchuk.cleverTask.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.model.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private TransactionDAO transactionDAO;

    @Test
    @DisplayName("saveTransaction")
    void testSaveTransaction() throws SQLException {
        Transaction transaction = new Transaction("Transfer", new BigDecimal("100.00"), new Timestamp(System.currentTimeMillis()), 1, 2);

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        transactionDAO.saveTransaction(transaction);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    @DisplayName("getAllTransactions")
    void testGetAllTransactions() throws SQLException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("transaction_type")).thenReturn("Transfer", "Payment");
        when(resultSet.getBigDecimal("amount")).thenReturn(new BigDecimal("100.00"), new BigDecimal("50.00"));
        when(resultSet.getTimestamp("timestamp")).thenReturn(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        when(resultSet.getInt("sender_account_id")).thenReturn(1, 2);
        when(resultSet.getInt("receiver_account_id")).thenReturn(2, 3);

        List<Transaction> transactions = transactionDAO.getAllTransactions();

        assertEquals(2, transactions.size());
        Transaction transaction = transactions.get(0);
        assertEquals("Transfer", transaction.getTransactionType());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertEquals(1, transaction.getSenderAccountId());
        assertEquals(2, transaction.getReceiverAccountId());
    }

    @Test
    @DisplayName("getTransactionById")
    void testGetTransactionById() throws SQLException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("transaction_type")).thenReturn("Transfer");
        when(resultSet.getBigDecimal("amount")).thenReturn(new BigDecimal("100.00"));
        when(resultSet.getTimestamp("timestamp")).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(resultSet.getInt("sender_account_id")).thenReturn(1);
        when(resultSet.getInt("receiver_account_id")).thenReturn(2);

        Transaction transaction = transactionDAO.getTransactionById(1);

        assertNotNull(transaction);
        assertEquals("Transfer", transaction.getTransactionType());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertEquals(1, transaction.getSenderAccountId());
        assertEquals(2, transaction.getReceiverAccountId());
    }

}

