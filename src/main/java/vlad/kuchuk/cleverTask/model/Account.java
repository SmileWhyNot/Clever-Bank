package vlad.kuchuk.cleverTask.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class Account {
    private int id;
    private final String accountNumber;
    private BigDecimal balance;
    private Date lastInterestCalculationDate;
    private final int personId;
    private final int bankId;
    private List<Integer> transactionIds;

    // TODO JavaDoc
    private final Lock lock = new ReentrantLock(); // Используйте ReentrantLock для блокировки

    // Метод для блокировки счета
    public void lock() {
        lock.lock(); // Блокировка счета
    }

    // Метод для разблокировки счета
    public void unlock() {
        lock.unlock(); // Разблокировка счета
    }

}
