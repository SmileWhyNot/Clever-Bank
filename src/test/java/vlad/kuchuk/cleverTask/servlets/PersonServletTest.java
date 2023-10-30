package vlad.kuchuk.cleverTask.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.model.Person;
import vlad.kuchuk.cleverTask.service.PersonService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PersonServletTest {

    @InjectMocks
    private PersonServlet personServlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PersonService personService;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
    }

    @Test
    @DisplayName("getAllPeople")
    void testDoGetAllPeople() throws IOException {
        try {
            PrintWriter writer = new PrintWriter(stringWriter);
            Mockito.when(response.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Person> mockPeople = Arrays.asList(
                new Person(1, "John Doe", "john@example.com", Arrays.asList(1, 2, 3)),
                new Person(2, "Jane Smith", "jane@example.com", Arrays.asList(4, 5))
        );
        Mockito.when(personService.getAllPeople()).thenReturn(mockPeople);
        Mockito.when(request.getParameter("action")).thenReturn(null);

        personServlet.doGet(request, response);

        String responseContent = stringWriter.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Person> people = objectMapper.readValue(responseContent, new TypeReference<>() {
        });

        assertEquals(2, people.size());

        Person person1 = people.get(0);
        assertEquals(1, person1.getId());
        assertEquals("John Doe", person1.getName());
        assertEquals("john@example.com", person1.getEmail());
        assertEquals(Arrays.asList(1, 2, 3), person1.getAccountIds());

        Person person2 = people.get(1);
        assertEquals(2, person2.getId());
        assertEquals("Jane Smith", person2.getName());
        assertEquals("jane@example.com", person2.getEmail());
        assertEquals(Arrays.asList(4, 5), person2.getAccountIds());
    }

    @Test
    @DisplayName("getPersonById")
    void testDoGetPersonById() throws IOException {
        try {
            PrintWriter writer = new PrintWriter(stringWriter);
            Mockito.when(response.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mockito.when(personService.getPersonById(1)).thenReturn(
                new Person(1, "John Doe", "john@example.com", Arrays.asList(1, 2, 3)));
        Mockito.when(request.getParameter("action")).thenReturn("get");
        Mockito.when(request.getParameter("id")).thenReturn("1");

        personServlet.doGet(request, response);

        String responseContent = stringWriter.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = objectMapper.readValue(responseContent, Person.class);

        assertEquals(1, person.getId());
        assertEquals("John Doe", person.getName());
        assertEquals("john@example.com", person.getEmail());
        assertEquals(Arrays.asList(1, 2, 3), person.getAccountIds());
    }

    @Test
    @DisplayName("createPerson")
    void testDoPostCreatePerson() {
        Mockito.when(request.getParameter("action")).thenReturn("create");
        Mockito.when(request.getParameter("name")).thenReturn("New Person");
        Mockito.when(request.getParameter("email")).thenReturn("new@example.com");

        personServlet.doPost(request, response);

        Mockito.verify(personService).createPerson(Mockito.any(Person.class));
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("updatePerson")
    void testDoPostUpdatePerson() {
        Mockito.when(request.getParameter("action")).thenReturn("update");
        Mockito.when(request.getParameter("id")).thenReturn("1");
        Mockito.when(request.getParameter("name")).thenReturn("Updated Person");
        Mockito.when(request.getParameter("email")).thenReturn("updated@example.com");

        personServlet.doPost(request, response);

        Mockito.verify(personService).updatePersonById(Mockito.any(Person.class), Mockito.anyInt());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("deletePerson")
    void testDoPostDeletePerson() {
        Mockito.when(request.getParameter("action")).thenReturn("delete");
        Mockito.when(request.getParameter("id")).thenReturn("1");

        personServlet.doPost(request, response);

        Mockito.verify(personService).deletePerson(Mockito.anyInt());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
