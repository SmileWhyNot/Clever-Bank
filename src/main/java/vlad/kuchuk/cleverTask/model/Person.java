package vlad.kuchuk.cleverTask.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @JsonCreator
    public Person(@JsonProperty("id") int id,
                  @JsonProperty("name") String name,
                  @JsonProperty("email") String email,
                  @JsonProperty("accountIds") List<Integer> accountIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.accountIds = accountIds;
    }

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
