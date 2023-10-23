package vlad.kuchuk.cleverTask.utils;

import lombok.Generated;
import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.BankDAO;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Класс, отвечающий за планирование и выполнение периодических начислений процентов
 * на счетах пользователей с учетом заданной процентной ставки.
 *
 * <p>
 * Для запуска периодического начисления процентов необходимо вызвать метод
 * {@link #startInterestCalculation(int)} и указать интервал в секундах между
 * начислениями. Для остановки выполнения периодического начисления можно вызвать
 * метод {@link #stopInterestCalculation()}.
 */
public class InterestCalculationScheduler {
    private ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(2);
    private final AccountDAO accountDAO;
    private final BankDAO bankDAO;
    private final Double interestRate;

    /**
     * Конструктор класса.
     *
     * @param accountDAO   Объект класса AccountDAO для доступа к данным счетов.
     * @param bankDAO      Объект класса BankDAO для доступа к данным о банке пользователя.
     * @param interestRate Значение процентной ставки для начисления процентов.
     */
    @Generated
    public InterestCalculationScheduler(AccountDAO accountDAO, BankDAO bankDAO, Double interestRate) {
        this.accountDAO = accountDAO;
        this.bankDAO = bankDAO;
        this.interestRate = interestRate;
    }

    /**
     * Конструктор класса, принимающий дополнительный параметр - объект
     * ScheduledExecutorService для планирования и управления задачами на выполнение.
     * <p>
     * Конструктор используется для тестирования
     *
     * @param accountDAO   Объект класса AccountDAO для доступа к данным счетов.
     * @param bankDAO      Объект класса BankDAO для доступа к данным о банке пользователя.
     * @param interestRate Значение процентной ставки для начисления процентов.
     * @param scheduler    Объект класса ScheduledExecutorService для планирования выполнения задач.
     */
    public InterestCalculationScheduler(AccountDAO accountDAO, BankDAO bankDAO,
                                        Double interestRate, ScheduledExecutorService scheduler) {
        this.accountDAO = accountDAO;
        this.bankDAO = bankDAO;
        this.interestRate = interestRate;
        this.scheduler = scheduler;
    }

    /**
     * Запускает периодическое начисление процентов с заданным интервалом.
     *
     * @param intervalSeconds Интервал в секундах между начислениями процентов.
     */
    public void startInterestCalculation(int intervalSeconds) {
        Runnable interestCalculationTask = new InterestCalculationTask(accountDAO, bankDAO, interestRate);

        scheduler.scheduleAtFixedRate(
                interestCalculationTask,
                0,
                intervalSeconds,
                TimeUnit.SECONDS
        );
    }

    /**
     * Останавливает выполнение периодического начисления процентов.
     */
    public void stopInterestCalculation() {
        scheduler.shutdown();
    }
}
