package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс, предоставляющий доступ к данным банков в базе данных.
 */
public class BankDAO {

    private final Connection connection;

    /**
     * Конструктор класса.
     *
     * @param connection Соединение с базой данных.
     */
    public BankDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Получает название банка по номеру счета пользователя
     *
     * @param accountNumber Номер счета пользователя банка
     * @return Объект банка с его наименованием
     */
    public Bank getBankNameByAccountNumber(String accountNumber) {
        String sql = "SELECT name FROM bank JOIN account ON bank.id = account.bank_id WHERE account_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToBank(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Преобразует результат запроса из ResultSet в объект банка (Bank).
     *
     * @param resultSet Результат SQL-запроса, содержащий данные о банке.
     * @return Объект банка (Bank) с данными из ResultSet.
     * @throws SQLException В случае возникновения ошибок при доступе к данным из ResultSet.
     */
    private Bank  mapResultSetToBank(ResultSet resultSet) throws SQLException {
        return new Bank(resultSet.getString("name"));
    }


}
