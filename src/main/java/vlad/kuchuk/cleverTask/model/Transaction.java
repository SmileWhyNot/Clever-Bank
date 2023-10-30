package vlad.kuchuk.cleverTask.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public Transaction(String transactionType, BigDecimal amount, Timestamp timestamp, int senderAccountId, int receiverAccountId) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = timestamp;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
    }

    @JsonCreator
    public Transaction(@JsonProperty("id") int id,
                       @JsonProperty("transactionType") String transactionType,
                       @JsonProperty("amount") BigDecimal amount,
                       @JsonProperty("timestamp") Timestamp timestamp,
                       @JsonProperty("senderAccountId") int senderAccountId,
                       @JsonProperty("receiverAccountId") int receiverAccountId) {
        this.id = id;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = timestamp;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
    }

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
