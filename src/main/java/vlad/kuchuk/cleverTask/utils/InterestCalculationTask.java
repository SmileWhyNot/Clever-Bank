package vlad.kuchuk.cleverTask.utils;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.model.Account;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Класс, представляющий задачу начисления процентов на счетах пользователей
 * с учетом заданной процентной ставки.
 *
 * <p>
 * Этот класс реализует интерфейс {@link Runnable}, чтобы его экземпляры
 * могли быть выполнены в качестве задачи с использованием планировщика.
 *
 * <p>
 * Для начисления процентов на счета, задача проверяет дату последнего начисления
 * процентов для каждого счета и, если прошел месяц с момента последнего начисления,
 * рассчитывает и начисляет проценты на текущий баланс счета.
 */
public class InterestCalculationTask implements Runnable{

    private final AccountDAO accountDAO;
    private final Double interestRate; // Значение процентов из конфигурационного файла

    /**
     * Конструктор класса.
     *
     * @param accountDAO   Объект класса AccountDAO для доступа к данным счетов.
     * @param interestRate Значение процентной ставки для начисления процентов.
     */
    public InterestCalculationTask(AccountDAO accountDAO, Double interestRate) {
        this.accountDAO = accountDAO;
        this.interestRate = interestRate;
    }

    /**
     * Выполняет задачу начисления процентов на счетах пользователей.
     * Задача проверяет дату последнего начисления процентов для каждого счета
     * и, если прошел месяц с момента последнего начисления, рассчитывает и начисляет
     * проценты на текущий баланс счета.
     */
    @Override
    public void run() {
        List<Account> accounts = accountDAO.getAllAccounts();

        Date currentDate = new Date();

        for (Account account : accounts) {
            account.lock();

            Date lastInterestCalculationDate = account.getLastInterestCalculationDate();

            try {
                if (!isSameMonth(lastInterestCalculationDate, currentDate)) {
                    // Рассчитайте начисление процентов
                    BigDecimal currentBalance = account.getBalance();
                    BigDecimal interest = currentBalance.multiply(BigDecimal.valueOf(interestRate));

                    // Обновите баланс с учетом начисления процентов
                    BigDecimal newBalance = currentBalance.add(interest);
                    accountDAO.updateBalance(account.getId(), newBalance);

                    account.setLastInterestCalculationDate(currentDate);
                    java.sql.Date curSQLDate = new java.sql.Date(currentDate.getTime());
                    accountDAO.updateLastInterestCalculationDate(account.getId(), curSQLDate);

                    // Логируйте начисление процентов
                    System.out.println("Начисление процентов для счета " + account.getAccountNumber() +
                            ": " + interest + " (Итоговый баланс: " + newBalance + ")");
                }

            } finally {
                account.unlock();
            }
        }
    }

    /**
     * Проверяет, являются ли две даты одним и тем же месяцем и годом.
     *
     * @param date1 Первая дата для сравнения.
     * @param date2 Вторая дата для сравнения.
     * @return true, если даты принадлежат одному месяцу и году, в противном случае - false.
     */
    private boolean isSameMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }
}
