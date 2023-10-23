package vlad.kuchuk.cleverTask.model;


import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * Класс, представляющий банк в банковской системе.
 *
 * <p>
 * Каждый банк имеет уникальный идентификатор, название и список идентификаторов счетов,
 * принадлежащих этому банку.
 */
@Data
public class Bank {
    private int id;
    private final String name;
    private List<Integer> accountIds;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return id == bank.id && Objects.equals(name, bank.name) && Objects.equals(accountIds, bank.accountIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, accountIds);
    }


}
