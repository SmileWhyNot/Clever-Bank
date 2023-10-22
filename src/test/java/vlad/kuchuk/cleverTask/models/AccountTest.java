package vlad.kuchuk.cleverTask.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vlad.kuchuk.cleverTask.model.Account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("12345", 1, 2);
        account.setBalance(BigDecimal.ZERO);
        account.setLastInterestCalculationDate(new Date());
        account.setTransactionIds(List.of(1, 2));
    }

    @Test
    @DisplayName("AccountLockUnlock")
    void testLockAndUnlock() throws InterruptedException {
        int numThreads = 5;
        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                account.lock();
                account.unlock();
                latch.countDown();
            });
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("UnlockResource")
    void testUnlock() {
        Lock lock = account.getLock();
        account.lock();

        account.unlock();
        assertTrue(lock.tryLock());
    }

    @Test
    @DisplayName("AccountGetters")
    void testGetters() {
        assertEquals("12345", account.getAccountNumber());
        assertEquals(BigDecimal.ZERO, account.getBalance());
        assertNotNull(account.getLastInterestCalculationDate());
        assertEquals(1, account.getPersonId());
        assertEquals(2, account.getBankId());
        assertEquals(List.of(1, 2), account.getTransactionIds());
    }

    @Test
    @DisplayName("AccountSetters")
    void testSetters() {
        Account account1 = new Account("12345", 1, 2);
        account1.setBalance(new BigDecimal(123));
        assertEquals(123, account1.getBalance().intValueExact());

        account1.setId(3);
        assertEquals(3, account1.getId());

        Date date = new Date();
        account1.setLastInterestCalculationDate(date);
        assertEquals(new Date(), account1.getLastInterestCalculationDate());

    }
}

