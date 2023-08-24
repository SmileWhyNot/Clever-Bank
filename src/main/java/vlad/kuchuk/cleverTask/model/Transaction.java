package vlad.kuchuk.cleverTask.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Transaction {
    private int id;
    private final String transactionType;
    private final BigDecimal amount;
    private final Timestamp timestamp;
    private final int senderAccountId;
    private final int receiverAccountId;
}
