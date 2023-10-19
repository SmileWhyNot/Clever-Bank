package vlad.kuchuk.cleverTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.dao.PersonDAO;
import vlad.kuchuk.cleverTask.model.Person;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonDAO personDAO;

    @Test
    void testAuthenticateWithValidEmail() {
        String validEmail = "john.doe@example.com";
        Person person = new Person(validEmail, "John Doe");

        when(personDAO.getPersonByEmail(validEmail)).thenReturn(person);

        Person result = personService.authenticate(validEmail);

        assertNotNull(result);
        assertEquals(person.getName(), result.getName());
    }

    @Test
    void testAuthenticateWithInvalidEmail() {
        String invalidEmail = "nonexistent@example.com";

        when(personDAO.getPersonByEmail(invalidEmail)).thenReturn(null);

        Person result = personService.authenticate(invalidEmail);

        assertNull(result);
    }

}

