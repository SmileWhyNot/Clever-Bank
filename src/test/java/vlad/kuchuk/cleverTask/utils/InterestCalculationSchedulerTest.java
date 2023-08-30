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
        accountDAO = Mockito.mock(AccountDAO.class);
        bankDAO = Mockito.mock(BankDAO.class);
        executorService = Mockito.mock(ScheduledExecutorService.class);

        scheduler = new InterestCalculationScheduler(accountDAO, bankDAO, 0.05, executorService);
    }

    @Test
    void testStartAndStopInterestCalculation() throws InterruptedException {
        scheduler.startInterestCalculation(60);

        verify(executorService).scheduleAtFixedRate(any(Runnable.class), Mockito.eq(0L), Mockito.eq(60L), Mockito.eq(TimeUnit.SECONDS));

        scheduler.stopInterestCalculation();

        verify(executorService).shutdown();
    }
}
