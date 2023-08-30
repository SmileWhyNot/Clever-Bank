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
        // Создаем моки для зависимых объектов (accountDAO, bankDAO)
        AccountDAO accountDAO = Mockito.mock(AccountDAO.class);
        BankDAO bankDAO = Mockito.mock(BankDAO.class);

        // Создаем объект InterestCalculationTask
        InterestCalculationTask calculationTask = new InterestCalculationTask(accountDAO, bankDAO, 0.05);

        // Создаем мок объект счета, у которого lastInterestCalculationDate равно null
        Account account = new Account("12345", 1, 1);
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100.00));
        Bank bank = new Bank("Bank");
        bank.setId(1);
        account.setLastInterestCalculationDate(null);

        // Настраиваем моки их методов (accountDAO.getAllAccounts(), accountDAO.updateBalance())
        Mockito.when(accountDAO.getAllAccounts()).thenReturn(Collections.singletonList(account));
        Mockito.doNothing().when(accountDAO).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class)); // Use matchers

        // Mock the behavior of bankDAO.getBankNameByAccountNumber(...) to return a valid Bank object
        Mockito.when(bankDAO.getBankNameByAccountNumber(account.getAccountNumber())).thenReturn(bank);

        // Запускаем задачу начисления процентов
        calculationTask.run();

        // Проверяем, что updateBalance был вызван
        Mockito.verify(accountDAO, Mockito.times(1)).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class));
    }

    @Test
    void testInterestCalculationWhenLastCalculationDateIsInPreviousMonth() {
        // Создаем моки для зависимых объектов (accountDAO, bankDAO)
        AccountDAO accountDAO = Mockito.mock(AccountDAO.class);
        BankDAO bankDAO = Mockito.mock(BankDAO.class);

        // Создаем объект InterestCalculationTask
        InterestCalculationTask calculationTask = new InterestCalculationTask(accountDAO, bankDAO, 0.05);

        // Создаем мок объект счета с датой последнего начисления в предыдущем месяце
        Account account = new Account("12345", 1, 1);
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100.00));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1); // Перейти на предыдущий месяц
        account.setLastInterestCalculationDate(calendar.getTime());
        Bank bank = new Bank("Bank");
        bank.setId(1);


        // Настраиваем моки их методов (accountDAO.getAllAccounts(), accountDAO.updateBalance())
        Mockito.when(accountDAO.getAllAccounts()).thenReturn(Collections.singletonList(account));
        Mockito.doNothing().when(accountDAO).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class)); // Use matchers

        // Mock the behavior of bankDAO.getBankNameByAccountNumber(...) to return a valid Bank object
        Mockito.when(bankDAO.getBankNameByAccountNumber(account.getAccountNumber())).thenReturn(bank);

        // Запускаем задачу начисления процентов
        calculationTask.run();

        // Проверяем, что updateBalance был вызван
        Mockito.verify(accountDAO, Mockito.times(1)).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class));
    }

    @Test
    void testInterestCalculationWhenLastCalculationDateIsInCurrentMonth() {
        // Создаем моки для зависимых объектов (accountDAO, bankDAO)
        AccountDAO accountDAO = Mockito.mock(AccountDAO.class);
        BankDAO bankDAO = Mockito.mock(BankDAO.class);

        // Создаем объект InterestCalculationTask
        InterestCalculationTask calculationTask = new InterestCalculationTask(accountDAO, bankDAO, 0.05);

        // Создаем мок объект счета с датой последнего начисления в текущем месяце
        Account account = new Account("12345", 1, 1);
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100.00));
        Calendar calendar = Calendar.getInstance();
        account.setLastInterestCalculationDate(calendar.getTime());

        // Настраиваем моки их методов (accountDAO.getAllAccounts(), accountDAO.updateBalance())
        Mockito.when(accountDAO.getAllAccounts()).thenReturn(Collections.singletonList(account));

        // Запускаем задачу начисления процентов
        calculationTask.run();

        // Проверяем, что updateBalance не был вызван
        Mockito.verify(accountDAO, Mockito.times(0)).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class));
    }


}
