package vlad.kuchuk.cleverTask.utils;

import vlad.kuchuk.cleverTask.dao.AccountDAO;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InterestCalculationScheduler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    private final AccountDAO accountDAO;
    private final Double interestRate; // Значение процентов из конфигурационного файла

    public InterestCalculationScheduler(AccountDAO accountDAO, Double interestRate) {
        this.accountDAO = accountDAO;
        this.interestRate = interestRate;
    }

    public void startInterestCalculation(int intervalSeconds) {
        Runnable interestCalculationTask = new InterestCalculationTask(accountDAO, interestRate);

        // Запускаем задачу начисления процентов с интервалом в полминуты
        scheduler.scheduleAtFixedRate(
                interestCalculationTask,
                0, // Начало выполнения задачи сразу
                intervalSeconds,
                TimeUnit.MINUTES
        );
    }

    public void stopInterestCalculation() {
        scheduler.shutdown();
    }
}
