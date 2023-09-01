package vlad.kuchuk.cleverTask.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vlad.kuchuk.cleverTask.dao.PersonDAO;
import vlad.kuchuk.cleverTask.database.DatabaseConnection;
import vlad.kuchuk.cleverTask.model.Person;
import vlad.kuchuk.cleverTask.service.PersonService;

import java.io.IOException;
import java.util.List;

/**
 * Сервлет для управления операциями CRUD (создание, чтение, обновление, удаление) сущностей Person.
 * Обрабатывает HTTP-запросы связанные с сущностью Person.
 */
@WebServlet("/person")
public class PersonServlet extends HttpServlet {
    private PersonService personService;

    /**
     * Инициализация сервлета, создание экземпляров PersonDAO и PersonService.
     *
     * @throws ServletException В случае ошибки при инициализации сервлета.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        PersonDAO personDAO = new PersonDAO(DatabaseConnection.getConnection());
        personService = new PersonService(personDAO);
    }

    /**
     * Обрабатывает HTTP GET-запросы, предоставляя информацию о сущности Person.
     *
     * @param request  Запрос от клиента.
     * @param response Ответ клиенту.
     * @throws IOException В случае ошибки ввода/вывода.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String action = request.getParameter("action");
        if (action == null) {
            List<Person> people = personService.getAllPeople();
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), people);
        } else if (action.equals("get")) {
            int personId = Integer.parseInt(request.getParameter("id"));
            Person person = personService.getPersonById(personId);
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), person);
        }
    }

    /**
     * Обрабатывает HTTP POST-запросы для создания, обновления и удаления сущности Person.
     *
     * @param request  Запрос от клиента.
     * @param response Ответ клиенту.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        switch (action) {
            case "create" -> {
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                Person newPerson = new Person(name, email);
                personService.createPerson(newPerson);
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
            case "update" -> {
                String newName = request.getParameter("name");
                String newEmail = request.getParameter("email");
                int personId = Integer.parseInt(request.getParameter("id"));
                Person updatedPerson = new Person(newName, newEmail);
                personService.updatePersonById(updatedPerson, personId);
                response.setStatus(HttpServletResponse.SC_OK);
            }
            case "delete" -> {
                int personId = Integer.parseInt(request.getParameter("id"));
                personService.deletePerson(personId);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}