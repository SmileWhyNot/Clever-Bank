package vlad.kuchuk.cleverTask.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private PersonDAO personDAO;

    @Test
    @DisplayName("getAllPeople")
    void testGetAllPeople() throws SQLException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("name")).thenReturn("John", "Alice");
        when(resultSet.getString("email")).thenReturn("john@example.com", "alice@example.com");
        when(resultSet.getInt("id")).thenReturn(1, 2);

        List<Person> people = personDAO.getAllPeople();

        assertEquals(2, people.size());
        Person person = people.get(0);
        assertEquals("John", person.getName());
        assertEquals("john@example.com", person.getEmail());
        assertEquals(1, person.getId());
    }

    @Test
    @DisplayName("getPersonById")
    void testGetPersonById() throws SQLException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("name")).thenReturn("John");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getInt("id")).thenReturn(1);

        Person person = personDAO.getPersonById(1);

        assertNotNull(person);
        assertEquals("John", person.getName());
        assertEquals("john@example.com", person.getEmail());
        assertEquals(1, person.getId());
    }

    @Test
    @DisplayName("getPersonByEmail")
    void testGetPersonByEmail() throws SQLException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("name")).thenReturn("John");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getInt("id")).thenReturn(1);

        Person person = personDAO.getPersonByEmail("john@example.com");

        assertNotNull(person);
        assertEquals("John", person.getName());
        assertEquals("john@example.com", person.getEmail());
        assertEquals(1, person.getId());
    }
}

