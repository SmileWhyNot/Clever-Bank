package vlad.kuchuk.cleverTask.model;


import lombok.Data;

import java.util.List;

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
}
