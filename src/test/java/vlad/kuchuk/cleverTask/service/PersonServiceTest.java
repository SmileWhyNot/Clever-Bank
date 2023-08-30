package vlad.kuchuk.cleverTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vlad.kuchuk.cleverTask.dao.PersonDAO;
import vlad.kuchuk.cleverTask.model.Person;

public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonDAO personDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticateWithValidEmail() {
        String validEmail = "john.doe@example.com";
        Person person = new Person(validEmail, "John Doe");

        when(personDAO.getPersonByEmail(validEmail)).thenReturn(person);

        Person result = personService.authenticate(validEmail);

        assertNotNull(result);
        assertEquals(person.getName(), result.getName());
    }

    @Test
    public void testAuthenticateWithInvalidEmail() {
        String invalidEmail = "nonexistent@example.com";

        when(personDAO.getPersonByEmail(invalidEmail)).thenReturn(null);

        Person result = personService.authenticate(invalidEmail);

        assertNull(result);
    }

}

