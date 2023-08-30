package vlad.kuchuk.cleverTask.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    // Перед каждым тестом убедимся, что соединение закрыто
    @BeforeEach
    public void setUp() {
        DatabaseConnection.closeConnection();
    }

    // После каждого теста убедимся, что соединение закрыто
    @AfterEach
    public void tearDown() {
        DatabaseConnection.closeConnection();
    }

    @Test
    public void testGetConnection() {
        // Проверим, что получаемое соединение не null
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection);

        // Проверим, что получаемое соединение работает (например, выполним запрос)
        try {
            assertTrue(connection.isValid(1));
        } catch (SQLException e) {
            fail("SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testCloseConnection() {
        // Убедимся, что после закрытия соединения, оно больше не является действительным
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection);

        DatabaseConnection.closeConnection();

        try {
            assertFalse(connection.isValid(1));
        } catch (SQLException e) {
            // Ожидается, что после закрытия соединения будет SQLException
        }
    }
}

