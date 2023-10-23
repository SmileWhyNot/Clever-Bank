package vlad.kuchuk.cleverTask.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Класс, представляющий банковскую транзакцию.
 *
 * <p>
 * Каждая транзакция имеет уникальный идентификатор, тип транзакции, сумму,
 * временную метку, идентификатор счета отправителя и идентификатор счета получателя.
 */
@Data
public class Transaction {
    private int id;
    private final String transactionType;
    private final BigDecimal amount;
    private final Timestamp timestamp;
    private final int senderAccountId;
    private final int receiverAccountId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && senderAccountId == that.senderAccountId && receiverAccountId == that.receiverAccountId && Objects.equals(transactionType, that.transactionType) && Objects.equals(amount, that.amount) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, timestamp, senderAccountId, receiverAccountId);
    }
}
