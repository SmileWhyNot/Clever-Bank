package vlad.kuchuk.cleverTask.models;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vlad.kuchuk.cleverTask.model.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    @Test
    @DisplayName("TransactionGettersSetters")
    void testGettersAndSetters() {
        Transaction transaction = new Transaction("Deposit", BigDecimal.valueOf(100.0)
                , Timestamp.valueOf("2023-08-29 12:00:00"), 101, 102);

        transaction.setId(1);
        Timestamp timestamp = Timestamp.valueOf("2023-08-29 12:00:00");


        assertEquals(1, transaction.getId());
        assertEquals("Deposit", transaction.getTransactionType());
        assertEquals(BigDecimal.valueOf(100.0), transaction.getAmount());
        assertEquals(timestamp, transaction.getTimestamp());
        assertEquals(101, transaction.getSenderAccountId());
        assertEquals(102, transaction.getReceiverAccountId());
    }

    @Test
    @DisplayName("isEqualsWorksCorrectly")
    void testEquals() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Transaction transaction1 = new Transaction("Перевод", BigDecimal.valueOf(12L),
                time, 1, 2);
        Transaction transaction2 = new Transaction("Перевод", BigDecimal.valueOf(12L),
                time, 1, 2);


        Transaction transaction3 = new Transaction("Пополнение", BigDecimal.valueOf(12L),
                new Timestamp(System.currentTimeMillis()), 3, 2);

        assertTrue(transaction1.equals(transaction2));
        assertTrue(transaction1.equals(transaction1));
        assertFalse(transaction1.equals(null));
        assertFalse(transaction1.equals(transaction3));
        transaction2.setId(999);
        assertFalse(transaction2.equals(transaction1));
    }

    @Test
    @DisplayName("isHashCodeWorksCorrectly")
    void testHashCode() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Transaction transaction1 = new Transaction("Перевод", BigDecimal.valueOf(12L),
                time, 1, 2);
        Transaction transaction2 = new Transaction("Перевод", BigDecimal.valueOf(12L),
                time, 1, 2);


        Transaction transaction3 = new Transaction("Пополнение", BigDecimal.valueOf(12L),
                new Timestamp(System.currentTimeMillis()), 3, 2);

        assertTrue(transaction1.hashCode() == transaction2.hashCode());
        Assumptions.assumeTrue(transaction1.hashCode() != transaction3.hashCode());
    }
}
