package vlad.kuchuk.cleverTask.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Bank;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;

class InterestCalculationTaskTest {

    @Test
    @DisplayName("interestCalculationNullDate")
    void testInterestCalculationWhenLastCalculationDateIsNull() {
        AccountDAO accountDAO = Mockito.mock(AccountDAO.class);
        BankDAO bankDAO = Mockito.mock(BankDAO.class);

        InterestCalculationTask calculationTask = new InterestCalculationTask(accountDAO, bankDAO, 0.05);

        Account account = new Account("12345", 1, 1);
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100.00));
        Bank bank = new Bank("Bank");
        bank.setId(1);
        account.setLastInterestCalculationDate(null);

        Mockito.when(accountDAO.getAllAccounts()).thenReturn(Collections.singletonList(account));
        Mockito.doNothing().when(accountDAO).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class)); // Use matchers

        Mockito.when(bankDAO.getBankNameByAccountNumber(account.getAccountNumber())).thenReturn(bank);

        calculationTask.run();

        Mockito.verify(accountDAO, Mockito.times(1)).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class));
    }

    @Test
    @DisplayName("interestCalculationPrevMonthDate")
    void testInterestCalculationWhenLastCalculationDateIsInPreviousMonth() {
        AccountDAO accountDAO = Mockito.mock(AccountDAO.class);
        BankDAO bankDAO = Mockito.mock(BankDAO.class);

        InterestCalculationTask calculationTask = new InterestCalculationTask(accountDAO, bankDAO, 0.05);

        Account account = new Account("12345", 1, 1);
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100.00));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1); // Перейти на предыдущий месяц
        account.setLastInterestCalculationDate(calendar.getTime());
        Bank bank = new Bank("Bank");
        bank.setId(1);


        Mockito.when(accountDAO.getAllAccounts()).thenReturn(Collections.singletonList(account));
        Mockito.doNothing().when(accountDAO).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class)); // Use matchers

        Mockito.when(bankDAO.getBankNameByAccountNumber(account.getAccountNumber())).thenReturn(bank);

        calculationTask.run();

        Mockito.verify(accountDAO, Mockito.times(1)).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class));
    }

    @Test
    @DisplayName("interestCalculationCurMonthDate")
    void testInterestCalculationWhenLastCalculationDateIsInCurrentMonth() {
        AccountDAO accountDAO = Mockito.mock(AccountDAO.class);
        BankDAO bankDAO = Mockito.mock(BankDAO.class);

        InterestCalculationTask calculationTask = new InterestCalculationTask(accountDAO, bankDAO, 0.05);

        Account account = new Account("12345", 1, 1);
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100.00));
        Calendar calendar = Calendar.getInstance();
        account.setLastInterestCalculationDate(calendar.getTime());

        Mockito.when(accountDAO.getAllAccounts()).thenReturn(Collections.singletonList(account));

        calculationTask.run();

        Mockito.verify(accountDAO, Mockito.times(0)).updateBalance(Mockito.anyInt(), Mockito.any(BigDecimal.class));
    }
}
