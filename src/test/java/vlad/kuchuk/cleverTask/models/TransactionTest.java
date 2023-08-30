package vlad.kuchuk.cleverTask.models;

import org.junit.jupiter.api.Test;
import vlad.kuchuk.cleverTask.model.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {
    @Test
    void testGettersAndSetters() {
        // Создаем объект Transaction
        Transaction transaction = new Transaction("Deposit", BigDecimal.valueOf(100.0)
                , Timestamp.valueOf("2023-08-29 12:00:00"), 101, 102);

        // Устанавливаем значения с помощью сеттеров
        transaction.setId(1);
        Timestamp timestamp = Timestamp.valueOf("2023-08-29 12:00:00");


        // Проверяем, что значения можно получить с помощью геттеров
        assertEquals(1, transaction.getId());
        assertEquals("Deposit", transaction.getTransactionType());
        assertEquals(BigDecimal.valueOf(100.0), transaction.getAmount());
        assertEquals(timestamp, transaction.getTimestamp());
        assertEquals(101, transaction.getSenderAccountId());
        assertEquals(102, transaction.getReceiverAccountId());
    }
}
