package vlad.kuchuk.cleverTask.models;

import org.junit.jupiter.api.Test;
import vlad.kuchuk.cleverTask.model.Person;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonTest {

    @Test
    void testGettersAndSetters() {
        // Создаем объект Person
        Person person = new Person("John Doe", "john@example.com");

        // Устанавливаем значения с помощью сеттеров
        person.setId(1);
        List<Integer> accountIds = new ArrayList<>();
        accountIds.add(101);
        accountIds.add(102);
        person.setAccountIds(accountIds);

        // Проверяем, что значения можно получить с помощью геттеров
        assertEquals(1, person.getId());
        assertEquals("John Doe", person.getName());
        assertEquals("john@example.com", person.getEmail());
        assertEquals(accountIds, person.getAccountIds());

        // Проверяем, что геттеры и сеттеры работают корректно
        assertEquals(101, person.getAccountIds().get(0));
        assertEquals(102, person.getAccountIds().get(1));
    }
}