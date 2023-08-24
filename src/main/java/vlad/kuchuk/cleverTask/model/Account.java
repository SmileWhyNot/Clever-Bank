package vlad.kuchuk.cleverTask.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Account {
    private int id;
    private final String accountNumber;
    private BigDecimal balance;
    private final int personId;
    private final int bankId;
    private List<Integer> transactionIds;

    // TODO JavaDoc
    private final Object lock = new Object(); // Объект-мьютекс для блокировки счета

    // Метод для блокировки счета
    public void lock() {
        synchronized (lock) {
            // Блокировка счета
            try {
                lock.wait(); // Ждем, пока счет не будет разблокирован
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Метод для разблокировки счета
    public void unlock() {
        synchronized (lock) {
            // Разблокировка счета
            lock.notifyAll(); // Разблокируем все потоки, ожидающие блокировку
        }
    }


}
