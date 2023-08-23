package vlad.kuchuk.cleverTask.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Account {
    private int id;
    private final String accountNumber;
    private BigDecimal balance;
    private final Person person;
    private final Bank bank;
    private List<Transaction> transactions;

}
