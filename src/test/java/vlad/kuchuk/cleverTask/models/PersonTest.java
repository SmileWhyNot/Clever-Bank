package vlad.kuchuk.cleverTask.models;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vlad.kuchuk.cleverTask.model.Bank;
import vlad.kuchuk.cleverTask.model.Person;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    @DisplayName("PersonGettersSetters")
    void testGettersAndSetters() {
        Person person = new Person("John Doe", "john@example.com");

        person.setId(1);
        List<Integer> accountIds = new ArrayList<>();
        accountIds.add(101);
        accountIds.add(102);
        person.setAccountIds(accountIds);

        assertEquals(1, person.getId());
        assertEquals("John Doe", person.getName());
        assertEquals("john@example.com", person.getEmail());
        assertEquals(accountIds, person.getAccountIds());

        assertEquals(101, person.getAccountIds().get(0));
        assertEquals(102, person.getAccountIds().get(1));
    }

    @Test
    @DisplayName("isEqualsWorksCorrectly")
    void testEquals() {
        Person person1 = new Person("vlad", "example");
        Person person2 = new Person("vlad", "example");


        Person person3 = new Person("Max", "max@mail.ru");
        person3.setAccountIds(List.of(1, 2, 3));

        assertTrue(person1.equals(person1));
        assertTrue(person1.equals(person2));
        assertFalse(person1.equals(null));
        assertFalse(person1.equals(person3));
        person2.setAccountIds(List.of(1,2));
        assertFalse(person2.equals(person1));
    }

    @Test
    @DisplayName("isHashCodeWorksCorrectly")
    void testHashCode() {
        Person person1 = new Person("vlad", "example");
        Person person2 = new Person("vlad", "example");

        Person person3 = new Person("Max", "max@mail.ru");
        person3.setAccountIds(List.of(1, 2, 3));

        assertTrue(person1.hashCode() == person2.hashCode());
        Assumptions.assumeTrue(person1.hashCode() != person3.hashCode());
    }
}