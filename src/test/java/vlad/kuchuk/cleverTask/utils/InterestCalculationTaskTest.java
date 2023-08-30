package vlad.kuchuk.cleverTask.utils;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Bank;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;

public class InterestCalculationTaskTest {

    @Test
    void testInterestCalculationWhenLastCalculationDateIsNull() {
        // ������� ���� ��� ��������� �������� (accountDAO, bankDAO)
        AccountDAO accountDAO = Mockito.mock(AccountDAO.class);
        BankDAO bankDAO = Mockito.mock(BankDAO.class);

        // ������� ������ InterestCalculationTask
        InterestCalculationTask calculationTask = new InterestCalculationTask(accountDAO, bankDAO, 0.05);

        // ������� ��� ������ �����, � �������� lastInterestCalculationDate ����� null
        Account account = new Account("12345", 1, 1);
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100.00));
        Bank bank = new Bank("Bank");
        bank.setId(1);
        account.setLastInterestCalculationDate(null);

        // ����������� ���� �� ������� (accountDAO.getAllAccounts(), accountDAO.updateBalance())
        Mockito.when(accountDAO.getAllAccounts()).thenReturn(Collections.singletonList(account));
        Mockito.doNothing().when(accountDAO).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class)); // Use matchers

        // Mock the behavior of bankDAO.getBankNameByAccountNumber(...) to return a valid Bank object
        Mockito.when(bankDAO.getBankNameByAccountNumber(account.getAccountNumber())).thenReturn(bank);

        // ��������� ������ ���������� ���������
        calculationTask.run();

        // ���������, ��� updateBalance ��� ������
        Mockito.verify(accountDAO, Mockito.times(1)).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class));
    }

    @Test
    void testInterestCalculationWhenLastCalculationDateIsInPreviousMonth() {
        // ������� ���� ��� ��������� �������� (accountDAO, bankDAO)
        AccountDAO accountDAO = Mockito.mock(AccountDAO.class);
        BankDAO bankDAO = Mockito.mock(BankDAO.class);

        // ������� ������ InterestCalculationTask
        InterestCalculationTask calculationTask = new InterestCalculationTask(accountDAO, bankDAO, 0.05);

        // ������� ��� ������ ����� � ����� ���������� ���������� � ���������� ������
        Account account = new Account("12345", 1, 1);
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100.00));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1); // ������� �� ���������� �����
        account.setLastInterestCalculationDate(calendar.getTime());
        Bank bank = new Bank("Bank");
        bank.setId(1);


        // ����������� ���� �� ������� (accountDAO.getAllAccounts(), accountDAO.updateBalance())
        Mockito.when(accountDAO.getAllAccounts()).thenReturn(Collections.singletonList(account));
        Mockito.doNothing().when(accountDAO).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class)); // Use matchers

        // Mock the behavior of bankDAO.getBankNameByAccountNumber(...) to return a valid Bank object
        Mockito.when(bankDAO.getBankNameByAccountNumber(account.getAccountNumber())).thenReturn(bank);

        // ��������� ������ ���������� ���������
        calculationTask.run();

        // ���������, ��� updateBalance ��� ������
        Mockito.verify(accountDAO, Mockito.times(1)).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class));
    }

    @Test
    void testInterestCalculationWhenLastCalculationDateIsInCurrentMonth() {
        // ������� ���� ��� ��������� �������� (accountDAO, bankDAO)
        AccountDAO accountDAO = Mockito.mock(AccountDAO.class);
        BankDAO bankDAO = Mockito.mock(BankDAO.class);

        // ������� ������ InterestCalculationTask
        InterestCalculationTask calculationTask = new InterestCalculationTask(accountDAO, bankDAO, 0.05);

        // ������� ��� ������ ����� � ����� ���������� ���������� � ������� ������
        Account account = new Account("12345", 1, 1);
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100.00));
        Calendar calendar = Calendar.getInstance();
        account.setLastInterestCalculationDate(calendar.getTime());

        // ����������� ���� �� ������� (accountDAO.getAllAccounts(), accountDAO.updateBalance())
        Mockito.when(accountDAO.getAllAccounts()).thenReturn(Collections.singletonList(account));

        // ��������� ������ ���������� ���������
        calculationTask.run();

        // ���������, ��� updateBalance �� ��� ������
        Mockito.verify(accountDAO, Mockito.times(0)).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class));
    }


}
