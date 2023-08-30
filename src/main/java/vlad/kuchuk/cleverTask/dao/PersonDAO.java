package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, предоставляющий доступ к данным о пользователях банков в базе данных.
 */
public class PersonDAO {

    private final Connection connection;

    /**
     * Конструктор класса.
     *
     * @param connection Соединение с базой данных.
     */
    public PersonDAO(Connection connection) {
        this.connection = connection;
    }


    /**
     * Получает список всех людей из базы данных.
     *
     * @return Список объектов Person, представляющих всех людей.
     */
    public List<Person> getAllPeople() {
        String sql = "SELECT * FROM person";
        List<Person> people = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                people.add(mapResultSetToPerson(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    /**
     * Получает информацию о человеке по его идентификатору.
     *
     * @param personId Идентификатор человека.
     * @return Объект Person, представляющий информацию о человеке, или null, если человек с указанным идентификатором не найден.
     */
    public Person getPersonById(int personId) {
        String sql = "SELECT * FROM person WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToPerson(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Получает информацию о пользователе банка по его почте.
     *
     * @param email значение почты пользователя.
     * @return Объект пользователя или null, если пользователь не найден.
     */
    public Person getPersonByEmail(String email) {
        String sql = "SELECT * FROM person WHERE email = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToPerson(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Добавляет нового человека в базу данных.
     *
     * @param newPerson Объект Person, представляющий нового человека.
     */
    public void addPerson(Person newPerson) {
        String sql = "INSERT INTO person (name, email) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newPerson.getName());
            preparedStatement.setString(2, newPerson.getEmail());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Person added successfully.");
            } else {
                System.err.println("Failed to add person.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Обновляет информацию о человеке по его идентификатору.
     *
     * @param updatedPerson Объект Person, представляющий обновленную информацию о человеке.
     * @param personId     Идентификатор человека, информацию о котором нужно обновить.
     */
    public void updatePerson(Person updatedPerson, int personId) {
        String sql = "UPDATE person SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setString(2, updatedPerson.getEmail());
            preparedStatement.setInt(3, personId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Person updated successfully.");
            } else {
                System.err.println("Failed to update person.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаляет человека из базы данных по его идентификатору.
     *
     * @param personId Идентификатор человека, которого нужно удалить.
     */
    public void deletePersonById(int personId) {
        String sql = "DELETE FROM person WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, personId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Person deleted successfully.");
            } else {
                System.err.println("No person found with id: " + personId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Преобразует результат запроса из ResultSet в объект пользователя (Person).
     *
     * @param resultSet Результат SQL-запроса, содержащий данные о пользователе.
     * @return Объект человека (Person) с данными из ResultSet.
     * @throws SQLException В случае возникновения ошибок при доступе к данным из ResultSet.
     */
    private Person mapResultSetToPerson(ResultSet resultSet) throws SQLException {
        Person person = new Person(
                resultSet.getString("name"),
                resultSet.getString("email")
                );
        person.setId(resultSet.getInt("id"));
        return person;
    }
}
