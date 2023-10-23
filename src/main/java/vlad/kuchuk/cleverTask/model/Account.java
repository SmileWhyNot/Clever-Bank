package vlad.kuchuk.cleverTask.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && personId == account.personId && bankId == account.bankId && Objects.equals(accountNumber, account.accountNumber) && Objects.equals(balance, account.balance) && Objects.equals(lastInterestCalculationDate, account.lastInterestCalculationDate) && Objects.equals(transactionIds, account.transactionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, balance, lastInterestCalculationDate);
    }
}
