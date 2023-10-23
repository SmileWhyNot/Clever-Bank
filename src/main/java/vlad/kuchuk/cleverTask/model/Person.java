package vlad.kuchuk.cleverTask.model;

import lombok.Data;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && Objects.equals(name, person.name) && Objects.equals(email, person.email) && Objects.equals(accountIds, person.accountIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
