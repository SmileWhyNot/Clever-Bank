package vlad.kuchuk.cleverTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.TransactionDAO;
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.service.TransactionService;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountDAO accountDAO;

    @Mock
    private TransactionDAO transactionDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TODO ����� ���� �� ��������
    @Test
    public void testDepositMoney() {
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
    public void testWithdrawMoney() {
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
    public void testExecuteMoneyTransferSuccessful() {
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
        String resultUTF8 = new String(result.getBytes(), StandardCharsets.UTF_8);
        assertEquals("���������� ��������� �������", resultUTF8);
    }

    @Test
    public void testExecuteMoneyTransferAccountNotFound() {
        int senderAccountId = 1;
        String receiverAccountNumber = "54321";
        BigDecimal amount = new BigDecimal("50.00");

        when(accountDAO.getById(senderAccountId)).thenReturn(null);
        when(accountDAO.getByAccountNumber(receiverAccountNumber)).thenReturn(null);

        String result = transactionService.executeMoneyTransfer(senderAccountId, receiverAccountNumber, amount);
        String resultUTF8 = new String(result.getBytes(), StandardCharsets.UTF_8);
        assertEquals("���������� ��������. ���� �� ������", resultUTF8);
    }

    @Test
    public void testExecuteMoneyTransferInsufficientFunds() {
        int senderAccountId = 1;
        String receiverAccountNumber = "54321";
        BigDecimal amount = new BigDecimal("150.00");

        Account senderAccount = new Account("12345", senderAccountId, 1);
        senderAccount.setBalance(new BigDecimal("100.00"));
        Account receiverAccount = new Account(receiverAccountNumber, 2, 1);

        when(accountDAO.getById(senderAccountId)).thenReturn(senderAccount);
        when(accountDAO.getByAccountNumber(receiverAccountNumber)).thenReturn(receiverAccount);

        String result = transactionService.executeMoneyTransfer(senderAccountId, receiverAccountNumber, amount);
        String resultUTF8 = new String(result.getBytes(), StandardCharsets.UTF_8);
        assertEquals("���������� ��������. ������������ �������", resultUTF8);
    }

    @Test
    public void testExecuteMoneyTransferError() {
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
        String resultUTF8 = new String(result.getBytes(), StandardCharsets.UTF_8);
        assertEquals("�������� ������ ��� ���������� ����������", resultUTF8);
    }


}

