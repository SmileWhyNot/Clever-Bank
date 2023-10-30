package vlad.kuchuk.cleverTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.dao.PersonDAO;
import vlad.kuchuk.cleverTask.model.Person;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonDAO personDAO;

    @Test
    @DisplayName("shouldAuthenticateWithEmail")
    void testAuthenticateWithValidEmail() {
        String validEmail = "john.doe@example.com";
        Person person = new Person(validEmail, "John Doe");

        when(personDAO.getPersonByEmail(validEmail)).thenReturn(person);

        Person result = personService.authenticate(validEmail);

        assertNotNull(result);
        assertEquals(person.getName(), result.getName());
    }

    @Test
    @DisplayName("tryAuthenticateInvalidEmail")
    void testAuthenticateWithInvalidEmail() {
        String invalidEmail = "nonexistent@example.com";

        when(personDAO.getPersonByEmail(invalidEmail)).thenReturn(null);

        Person result = personService.authenticate(invalidEmail);

        assertNull(result);
    }

    @Test
    @DisplayName("updatePersonById")
    void testUpdatePersonById() {
        int personId = 1;
        Person updatedPerson = new Person("newemail@example.com", "Updated Name");

        doNothing().when(personDAO).updatePerson(updatedPerson, personId);

        personService.updatePersonById(updatedPerson, personId);

        verify(personDAO, times(1)).updatePerson(updatedPerson, personId);
    }

    @Test
    @DisplayName("getPersonById")
    void testGetPersonById() {
        int personId = 1;
        Person person = new Person("jane.doe@example.com", "Jane Doe");

        when(personDAO.getPersonById(personId)).thenReturn(person);

        Person result = personService.getPersonById(personId);

        assertNotNull(result);
        assertEquals(person.getName(), result.getName());
    }

    @Test
    @DisplayName("createPerson")
    void testCreatePerson() {
        Person newPerson = new Person("newuser@example.com", "New User");

        doNothing().when(personDAO).addPerson(newPerson);

        personService.createPerson(newPerson);

        verify(personDAO, times(1)).addPerson(newPerson);
    }

    @Test
    @DisplayName("deletePerson")
    void testDeletePerson() {
        int personId = 1;

        doNothing().when(personDAO).deletePersonById(personId);

        personService.deletePerson(personId);

        verify(personDAO, times(1)).deletePersonById(personId);
    }

    @Test
    @DisplayName("getAllPeople")
    void testGetAllPeople() {
        List<Person> people = new ArrayList<>();
        people.add(new Person("user1@example.com", "User 1"));
        people.add(new Person("user2@example.com", "User 2"));

        when(personDAO.getAllPeople()).thenReturn(people);

        List<Person> result = personService.getAllPeople();

        assertNotNull(result);
        assertEquals(people.size(), result.size());
    }

}

