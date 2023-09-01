package vlad.kuchuk.cleverTask.model;

import lombok.Data;

import java.util.List;

/**
 * Класс, представляющий физическое лицо в банковской системе.
 *
 * <p>
 * Каждое физическое лицо имеет уникальный идентификатор, имя, электронную почту
 * и список идентификаторов счетов, принадлежащих этому лицу.
 */
@Data
public class Person {
    private int id;
    private final String name;
    private final String email;
    private List<Integer> accountIds;
}
