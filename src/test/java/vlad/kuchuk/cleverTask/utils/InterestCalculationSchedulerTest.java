package vlad.kuchuk.cleverTask.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.BankDAO;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


public class InterestCalculationSchedulerTest {

    private InterestCalculationScheduler scheduler;
    private AccountDAO accountDAO;
    private BankDAO bankDAO;
    private ScheduledExecutorService executorService;

    @BeforeEach
    void setUp() {
        // Создаем моки для AccountDAO, BankDAO и ScheduledExecutorService
        accountDAO = Mockito.mock(AccountDAO.class);
        bankDAO = Mockito.mock(BankDAO.class);
        executorService = Mockito.mock(ScheduledExecutorService.class);

        // Создаем объект InterestCalculationScheduler с моком ScheduledExecutorService
        scheduler = new InterestCalculationScheduler(accountDAO, bankDAO, 0.05, executorService);
    }

    @Test
    void testStartAndStopInterestCalculation() throws InterruptedException {
        // Запускаем начисление процентов с интервалом 60 секунд
        scheduler.startInterestCalculation(60);

        // Проверяем, что задача была запущена с правильными параметрами
        verify(executorService).scheduleAtFixedRate(any(Runnable.class), Mockito.eq(0L), Mockito.eq(60L), Mockito.eq(TimeUnit.SECONDS));

        // Останавливаем начисление процентов
        scheduler.stopInterestCalculation();

        // Проверяем, что планировщик задач был остановлен
        verify(executorService).shutdown();
    }
}
