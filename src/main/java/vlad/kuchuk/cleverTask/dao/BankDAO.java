package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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

    private Bank  mapResultSetToBank(ResultSet resultSet) throws SQLException {

        return new Bank(resultSet.getString("name"));
    }


}
