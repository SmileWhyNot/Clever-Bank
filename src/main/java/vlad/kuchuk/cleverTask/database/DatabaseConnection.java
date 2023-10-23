package vlad.kuchuk.cleverTask.database;

import lombok.experimental.UtilityClass;
import vlad.kuchuk.cleverTask.utils.YamlReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс DatabaseConnection предоставляет методы для установки и закрытия
 * соединения с базой данных. Соединение с базой данных инициализируется при первом
 * вызове метода getConnection() и закрывается при вызове метода closeConnection().
 */
@UtilityClass
public class DatabaseConnection {

    /** Соединение с базой данных. */
    private static Connection connection;

    /**
     * Получить соединение с базой данных. Если соединение еще не установлено,
     * оно будет инициализировано на основе конфигурации из файла "application.yml".
     *
     * @return Объект Connection для взаимодействия с базой данных.
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                String url = YamlReader.getString("url");
                String username = YamlReader.getString("user");
                String password = YamlReader.getString("password");
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Закрыть соединение с базой данных, если оно открыто.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
