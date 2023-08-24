package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, предоставляющий доступ к данным счетов в базе данных.
 */
public class AccountDAO {
    private final Connection connection;

    /**
     * Конструктор класса.
     *
     * @param connection Соединение с базой данных.
     */
    public AccountDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Получает информацию о счете по его идентификатору.
     *
     * @param accountId Идентификатор счета.
     * @return Объект счета или null, если счет не найден.
     */
    public Account getById(int accountId) {
        String sql = "SELECT * FROM account WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToAccount(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Получает список всех счетов для указанного пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список счетов пользователя.
     */
    public List<Account> getAllAccountsForUser(int userId) {
        String sql = "SELECT * FROM account WHERE person_id = ?";
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accounts.add(mapResultSetToAccount(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    /**
     * Обновляет баланс счета.
     *
     * @param accountId  Идентификатор счета.
     * @param newBalance Новый баланс счета.
     */
    public void updateBalance(int accountId, BigDecimal newBalance) {
        String sql = "UPDATE account SET balance = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, newBalance);
            preparedStatement.setInt(2, accountId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Преобразует результат запроса из ResultSet в объект счета (Account).
     *
     * @param resultSet Результат SQL-запроса, содержащий данные о счете.
     * @return Объект счета (Account) с данными из ResultSet.
     * @throws SQLException В случае возникновения ошибок при доступе к данным из ResultSet.
     */
    private Account mapResultSetToAccount(ResultSet resultSet) throws SQLException {
         Account account = new  Account(
                resultSet.getString("account_number"),
                resultSet.getInt("person_id"),
                resultSet.getInt("bank_id")
        );
         account.setBalance(resultSet.getBigDecimal("balance"));
         account.setId(resultSet.getInt("id"));

         return account;
    }
}
