package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Account;

import java.math.BigDecimal;
import java.sql.*;
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
     * Получает информацию о счет по его номеру
     *
     * @param accountNumber Содержит номер счета пользователя
     * @return Объект счета или null, если счет не найден.
     */
    public Account getByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM account WHERE account_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountNumber);
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
     * Получает список всех счетов из БД
     *
     * @return Список счетов пользователей
     */
    public List<Account> getAllAccounts() {
        String sql = "SELECT * FROM account";
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
     * Обновляет значение даты последнего начисления процентов по балансу
     *
     * @param id Идентификатор счета
     * @param currentDate Значение текущей даты
     */
    public void updateLastInterestCalculationDate(int id, Date currentDate) {
        String sql = "UPDATE account SET last_interest_calculation_date = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, currentDate);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO JavaDoc
    public void addAccount(Account account) {
        String sql = "INSERT INTO account (account_number, person_id, bank_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, account.getAccountNumber());
            preparedStatement.setInt(2, account.getPersonId());
            preparedStatement.setInt(3, account.getBankId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account added successfully.");
            } else {
                System.err.println("Failed to add account.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // TODO JavaDoc
    public void updateAccount(Account updatedAccount, int accountId) {
        String sql = "UPDATE account SET account_number = ?, person_id = ?, bank_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, updatedAccount.getAccountNumber());
            preparedStatement.setInt(2, updatedAccount.getPersonId());
            preparedStatement.setInt(3, updatedAccount.getBankId());
            preparedStatement.setInt(4, accountId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account updated successfully.");
            } else {
                System.err.println("Failed to update account.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO JavaDoc
    public void deleteById(int accountId) {
        String sql = "DELETE FROM account WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, accountId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Account deleted successfully.");
            } else {
                System.err.println("No account found with id: " + accountId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Переводит средства между двумя счетами в рамках одной транзакции.
     *
     * @param senderAccountId   Идентификатор счета отправителя.
     * @param receiverAccountId Идентификатор счета получателя.
     * @param amount            Сумма средств для перевода.
     * @return true, если перевод средств успешно завершен; false в противном случае.
     */
    public boolean transferFunds(int senderAccountId, int receiverAccountId, BigDecimal amount) {
        String sql = "UPDATE account SET balance = balance - ? WHERE id = ?; " +
                     "UPDATE account SET balance = balance + ? WHERE id = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setInt(2, senderAccountId);

            preparedStatement.setBigDecimal(3, amount);
            preparedStatement.setInt(4, receiverAccountId);

            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);

            return true;
        } catch (SQLException e) {
            try {
                // В случае ошибки откатываем транзакцию
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false; // Ошибка при переводе средств
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
         account.setLastInterestCalculationDate(resultSet.getDate("last_interest_calculation_date"));
         account.setId(resultSet.getInt("id"));

         return account;
    }
}
