package vlad.kuchuk.cleverTask.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.BankDAO;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class InterestCalculationSchedulerTest {

    @InjectMocks
    private InterestCalculationScheduler scheduler;
    @Mock
    private AccountDAO accountDAO;
    @Mock
    private BankDAO bankDAO;
    @Mock
    private ScheduledExecutorService executorService;


    @Test
    @DisplayName("startStopInterestCalculation")
    void testStartAndStopInterestCalculation() {
        scheduler.startInterestCalculation(60);

        verify(executorService).scheduleAtFixedRate(any(Runnable.class),
                Mockito.eq(0L), Mockito.eq(60L), Mockito.eq(TimeUnit.SECONDS));

        scheduler.stopInterestCalculation();

        verify(executorService).shutdown();
    }
}
