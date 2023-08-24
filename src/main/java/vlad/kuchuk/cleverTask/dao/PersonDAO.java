package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonDAO {

    private final Connection connection;

    public PersonDAO(Connection connection) {
        this.connection = connection;
    }

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

    private Person mapResultSetToPerson(ResultSet resultSet) throws SQLException {
        Person person = new Person(
                resultSet.getString("name"),
                resultSet.getString("email")
                );
        person.setId(resultSet.getInt("id"));
        return person;
    }
}
