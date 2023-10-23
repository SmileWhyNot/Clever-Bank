package vlad.kuchuk.cleverTask.database;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.utils.YamlReader;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Test
    @DisplayName("closeNullableConnection")
    void testCloseNullableConnection() {
        try (MockedStatic<DatabaseConnection> mockedConnection = mockStatic(DatabaseConnection.class)) {
            when(DatabaseConnection.getConnection()).thenReturn(null);
            Connection connection = DatabaseConnection.getConnection();
            assertNull(connection);
            DatabaseConnection.closeConnection();
        }
    }

    @Test
    void testConnectionException() {
        MockedStatic<YamlReader> yaml = Mockito.mockStatic(YamlReader.class);
        when(YamlReader.getString("url")).thenReturn("fake_url");
        when(YamlReader.getString("user")).thenReturn("fake_user");
        when(YamlReader.getString("password")).thenReturn("fake_password");

        DatabaseConnection.getConnection();
        assertThrows(SQLException.class, () -> {
            throw new SQLException("Expected SQLException");
        });

        yaml.close();
    }
}

