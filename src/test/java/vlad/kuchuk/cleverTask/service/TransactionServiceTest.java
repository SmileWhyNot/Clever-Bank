package vlad.kuchuk.cleverTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.TransactionDAO;
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Transaction;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountDAO accountDAO;

    @Mock
    private TransactionDAO transactionDAO;

    @Test
    @DisplayName("shouldDepositMoney")
    void testDepositMoney() {
        String accountNumber = "12345";
        BigDecimal initialBalance = new BigDecimal("100.00");
        BigDecimal depositAmount = new BigDecimal("50.00");

        Account account = new Account(accountNumber, 1, 1);
        account.setId(1);
        account.setBalance(initialBalance);
        when(accountDAO.getById(1)).thenReturn(account);

        transactionService.depositMoney(1, depositAmount);

        assertEquals(initialBalance.add(depositAmount), account.getBalance());
    }

    @Test
    @DisplayName("shouldWithdrawMoney")
    void testWithdrawMoney() {
        int accountId = 1;
        BigDecimal initialBalance = new BigDecimal("100.00");
        BigDecimal withdrawalAmount = new BigDecimal("30.00");

        Account account = new Account("12345", accountId, 1);
        account.setBalance(initialBalance);
        when(accountDAO.getById(accountId)).thenReturn(account);

        transactionService.withdrawMoney(accountId, withdrawalAmount);

        assertEquals(initialBalance.subtract(withdrawalAmount), account.getBalance());
    }

    @Test
    @DisplayName("shouldExecuteMoney")
    void testExecuteMoneyTransferSuccessful() {
        int senderAccountId = 1;
        String receiverAccountNumber = "54321";
        BigDecimal amount = new BigDecimal("50.00");

        Account senderAccount = new Account("12345", senderAccountId, 1);
        Account receiverAccount = new Account(receiverAccountNumber, 2, 2);
        senderAccount.setBalance(amount);
        receiverAccount.setBalance(amount);

        when(accountDAO.getById(senderAccountId)).thenReturn(senderAccount);
        when(accountDAO.getByAccountNumber(receiverAccountNumber)).thenReturn(receiverAccount);
        when(accountDAO.transferFunds(senderAccountId, receiverAccount.getId(), amount)).thenReturn(true);

        String result = transactionService.executeMoneyTransfer(senderAccountId, receiverAccountNumber, amount);
        assertEquals("Транзакция проведена успешно", result);
    }

    @Test
    @DisplayName("failedMoneyTransferNoAccount")
    void testExecuteMoneyTransferAccountNotFound() {
        int senderAccountId = 1;
        String receiverAccountNumber = "54321";
        BigDecimal amount = new BigDecimal("50.00");

        when(accountDAO.getById(senderAccountId)).thenReturn(null);
        when(accountDAO.getByAccountNumber(receiverAccountNumber)).thenReturn(null);

        String result = transactionService.executeMoneyTransfer(senderAccountId, receiverAccountNumber, amount);
        assertEquals("Транзакция отменена. Счет не найден", result);
    }

    @Test
    @DisplayName("failedMoneyTransferInsufficientFunds")
    void testExecuteMoneyTransferInsufficientFunds() {
        int senderAccountId = 1;
        String receiverAccountNumber = "54321";
        BigDecimal amount = new BigDecimal("150.00");

        Account senderAccount = new Account("12345", senderAccountId, 1);
        senderAccount.setBalance(new BigDecimal("100.00"));
        Account receiverAccount = new Account(receiverAccountNumber, 2, 1);

        when(accountDAO.getById(senderAccountId)).thenReturn(senderAccount);
        when(accountDAO.getByAccountNumber(receiverAccountNumber)).thenReturn(receiverAccount);

        String result = transactionService.executeMoneyTransfer(senderAccountId, receiverAccountNumber, amount);
        assertEquals("Транзакция отменена. Недостаточно средств", result);
    }

    @Test
    @DisplayName("failedMoneyTransfer")
    void testExecuteMoneyTransferError() {
        int senderAccountId = 1;
        String receiverAccountNumber = "54321";
        BigDecimal amount = new BigDecimal("50.00");

        Account senderAccount = new Account("12345", senderAccountId, 1);
        Account receiverAccount = new Account(receiverAccountNumber, 2, 2);
        senderAccount.setBalance(amount);
        receiverAccount.setBalance(amount);

        when(accountDAO.getById(senderAccountId)).thenReturn(senderAccount);
        when(accountDAO.getByAccountNumber(receiverAccountNumber)).thenReturn(receiverAccount);
        when(accountDAO.transferFunds(senderAccountId, receiverAccount.getId(), amount)).thenReturn(false);

        String result = transactionService.executeMoneyTransfer(senderAccountId, receiverAccountNumber, amount);
        assertEquals("Возникла ошибка при проведении транзакции", result);
    }

    @Test
    @DisplayName("updateTransactionById")
    void testUpdateTransactionById() {
        int transactionId = 1;
        Transaction updatedTransaction = new Transaction(
                new String("перевод".getBytes(), StandardCharsets.UTF_8),
                new BigDecimal("50.00"),
                new Timestamp(System.currentTimeMillis()),
                1,
                2
        );

        doNothing().when(transactionDAO).updateTransaction(updatedTransaction, transactionId);

        transactionService.updateTransactionById(updatedTransaction, transactionId);

        verify(transactionDAO, times(1)).updateTransaction(updatedTransaction, transactionId);
    }

    @Test
    @DisplayName("getTransactionById")
    void testGetTransactionById() {
        int transactionId = 1;
        Transaction transaction = new Transaction(
                new String("перевод".getBytes(), StandardCharsets.UTF_8),
                new BigDecimal("50.00"),
                new Timestamp(System.currentTimeMillis()),
                1,
                2
        );

        when(transactionDAO.getTransactionById(transactionId)).thenReturn(transaction);

        Transaction result = transactionService.getTransactionById(transactionId);

        assertNotNull(result);
        assertEquals(transaction.getTransactionType(), result.getTransactionType());
    }

    @Test
    @DisplayName("createTransaction")
    void testCreateTransaction() {
        Transaction newTransaction = new Transaction(
                new String("перевод".getBytes(), StandardCharsets.UTF_8),
                new BigDecimal("50.00"),
                new Timestamp(System.currentTimeMillis()),
                1,
                2
        );

        doNothing().when(transactionDAO).addTransaction(newTransaction);

        transactionService.createTransaction(newTransaction);

        verify(transactionDAO, times(1)).addTransaction(newTransaction);
    }

    @Test
    @DisplayName("deleteTransaction")
    void testDeleteTransaction() {
        int transactionId = 1;

        doNothing().when(transactionDAO).deleteTransactionById(transactionId);

        transactionService.deleteTransaction(transactionId);

        verify(transactionDAO, times(1)).deleteTransactionById(transactionId);
    }

    @Test
    @DisplayName("getAllTransactions")
    void testGetAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                new String("перевод".getBytes(), StandardCharsets.UTF_8),
                new BigDecimal("50.00"),
                new Timestamp(System.currentTimeMillis()),
                1,
                2
        ));
        transactions.add(new Transaction(
                new String("перевод".getBytes(), StandardCharsets.UTF_8),
                new BigDecimal("30.00"),
                new Timestamp(System.currentTimeMillis()),
                2,
                3
        ));

        when(transactionDAO.getAllTransactions()).thenReturn(transactions);

        List<Transaction> result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertEquals(transactions.size(), result.size());
    }
}

