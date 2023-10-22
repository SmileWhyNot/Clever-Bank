package vlad.kuchuk.cleverTask.database;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    @DisplayName("getBDConnection")
    void testGetConnection() {
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection);
    }

    @Test
    @DisplayName("closeBDConnection")
    void testCloseConnection() {
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection);

        DatabaseConnection.closeConnection();

        try {
            assertFalse(connection.isValid(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

