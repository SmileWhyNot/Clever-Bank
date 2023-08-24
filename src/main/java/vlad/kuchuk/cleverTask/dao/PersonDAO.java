package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
