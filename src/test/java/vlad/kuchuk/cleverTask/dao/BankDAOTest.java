package vlad.kuchuk.cleverTask.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.model.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankDAOTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private BankDAO bankDAO;

    @Test
    @DisplayName("getBankNameByAccountNumber")
    void testGetBankNameByAccountNumber() throws SQLException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("name")).thenReturn("BankName");

        Bank bank = bankDAO.getBankNameByAccountNumber("12345");

        assertNotNull(bank);
        assertEquals("BankName", bank.getName());
    }

    @Test
    @DisplayName("getAllBanks")
    void testGetAllBanks() throws SQLException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("name")).thenReturn("Bank1", "Bank2");

        List<Bank> banks = bankDAO.getAllBanks();

        assertEquals(2, banks.size());
        assertEquals("Bank1", banks.get(0).getName());
        assertEquals("Bank2", banks.get(1).getName());
    }
}

