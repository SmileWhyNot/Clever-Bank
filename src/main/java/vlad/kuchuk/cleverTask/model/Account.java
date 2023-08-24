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


}
