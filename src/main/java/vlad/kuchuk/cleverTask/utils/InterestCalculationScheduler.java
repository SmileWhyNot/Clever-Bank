package vlad.kuchuk.cleverTask.utils;

import vlad.kuchuk.cleverTask.dao.AccountDAO;

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
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(2);
    private final AccountDAO accountDAO;
    private final Double interestRate;

    /**
     * Конструктор класса.
     *
     * @param accountDAO   Объект класса AccountDAO для доступа к данным счетов.
     * @param interestRate Значение процентной ставки для начисления процентов.
     */
    public InterestCalculationScheduler(AccountDAO accountDAO, Double interestRate) {
        this.accountDAO = accountDAO;
        this.interestRate = interestRate;
    }

    /**
     * Запускает периодическое начисление процентов с заданным интервалом.
     *
     * @param intervalSeconds Интервал в секундах между начислениями процентов.
     */
    public void startInterestCalculation(int intervalSeconds) {
        Runnable interestCalculationTask = new InterestCalculationTask(accountDAO, interestRate);

        // Запускаем задачу начисления процентов с интервалом в полминуты
        scheduler.scheduleAtFixedRate(
                interestCalculationTask,
                0, // Начало выполнения задачи сразу
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
