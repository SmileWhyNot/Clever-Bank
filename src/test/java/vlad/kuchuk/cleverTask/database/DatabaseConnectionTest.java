package vlad.kuchuk.cleverTask.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @BeforeEach
    public void setUp() {
        DatabaseConnection.closeConnection();
    }

    @AfterEach
    public void tearDown() {
        DatabaseConnection.closeConnection();
    }

    @Test
    // ��� ������� ��������� �� ����� �������
    // ���� ���� �������������
    // �������� �������� ����������� �������� ���������
    public void testGetConnection() {
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection);

        try {
            assertTrue(connection.isValid(1));
        } catch (SQLException e) {
            fail("SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testCloseConnection() {
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

