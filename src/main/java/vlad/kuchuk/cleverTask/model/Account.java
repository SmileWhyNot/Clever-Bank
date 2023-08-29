package vlad.kuchuk.cleverTask.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс, представляющий счет в банковской системе.
 *
 * <p>
 * Каждый счет имеет уникальный номер, баланс, дату последнего начисления процентов,
 * идентификатор владельца счета (personId), идентификатор банка (bankId) и список
 * идентификаторов транзакций, связанных с этим счетом.
 *
 * <p>
 * Счет может быть заблокирован или разблокирован для выполнения операций.
 * Для блокировки и разблокировки счета используются механизмы блокировки.
 *
 * @see Lock
 * @see ReentrantLock
 */
@Data
public class Account {
    private int id;
    private final String accountNumber;
    private BigDecimal balance;
    private Date lastInterestCalculationDate;
    private final int personId;
    private final int bankId;
    private List<Integer> transactionIds;

    private final Lock lock = new ReentrantLock();

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

}
