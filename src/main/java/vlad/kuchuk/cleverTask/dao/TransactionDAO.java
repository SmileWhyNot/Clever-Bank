package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс предоставляющий доступ к данным о проведенных транзакциях в базе данных
 */
public class TransactionDAO {
    private final Connection connection;

    /**
     *
     * @param connection Соединение с базой данных.
     */
    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Сохраняет проведенную транзакцию в базу данных.
     *
     * @param transaction объект, содержащий в себе всю информацию о проведенной транзакции.
     */
    public void saveTransaction(Transaction transaction) {
        String sql = "INSERT INTO transaction (transaction_type, amount, timestamp, sender_account_id, receiver_account_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, transaction.getTransactionType());
            preparedStatement.setBigDecimal(2, transaction.getAmount());
            preparedStatement.setTimestamp(3, transaction.getTimestamp());
            preparedStatement.setInt(4, transaction.getSenderAccountId());
            preparedStatement.setInt(5, transaction.getReceiverAccountId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает список всех транзакций, имеющихся в базе данных.
     *
     * @return Список объектов Transaction, представляющих все транзакции в базе данных.
     */
    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM transaction";
        List<Transaction> transactions = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactions.add(mapResultSetToTransaction(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Возвращает транзакцию с указанным идентификатором.
     *
     * @param transactionId Идентификатор транзакции, которую требуется получить.
     * @return Объект Transaction, представляющий транзакцию с указанным идентификатором.
     */
    public Transaction getTransactionById(int transactionId) {
        String sql = "SELECT * FROM transaction WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, transactionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToTransaction(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Добавляет новую транзакцию в базу данных.
     *
     * @param transaction Объект Transaction, представляющий новую транзакцию для добавления.
     */
    public void addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transaction (transaction_type, amount, timestamp, sender_account_id, receiver_account_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, transaction.getTransactionType());
            preparedStatement.setBigDecimal(2, transaction.getAmount());
            preparedStatement.setTimestamp(3, transaction.getTimestamp());
            preparedStatement.setInt(4, transaction.getSenderAccountId());
            preparedStatement.setInt(5, transaction.getReceiverAccountId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction added successfully.");
            } else {
                System.err.println("Failed to add transaction.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обновляет существующую транзакцию в базе данных.
     *
     * @param updatedTransaction Объект Transaction с обновленными данными.
     * @param transactionId      Идентификатор транзакции, которую требуется обновить.
     */
    public void updateTransaction(Transaction updatedTransaction, int transactionId) {
        String sql = "UPDATE transaction SET transaction_type = ?, amount = ?, timestamp = ?, sender_account_id = ?, receiver_account_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, updatedTransaction.getTransactionType());
            preparedStatement.setBigDecimal(2, updatedTransaction.getAmount());
            preparedStatement.setTimestamp(3, updatedTransaction.getTimestamp());
            preparedStatement.setInt(4, updatedTransaction.getSenderAccountId());
            preparedStatement.setInt(5, updatedTransaction.getReceiverAccountId());
            preparedStatement.setInt(6, transactionId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction updated successfully.");
            } else {
                System.err.println("Failed to update transaction.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаляет транзакцию из базы данных по ее идентификатору.
     *
     * @param transactionId Идентификатор транзакции, которую требуется удалить.
     */
    public void deleteTransactionById(int transactionId) {
        String sql = "DELETE FROM transaction WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, transactionId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Transaction deleted successfully.");
            } else {
                System.err.println("No transaction found with id: " + transactionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Преобразует строку результата из ResultSet в объект Transaction.
     *
     * @param resultSet Результат запроса к базе данных, содержащий данные транзакции.
     * @return Объект Transaction, созданный на основе данных из ResultSet.
     * @throws SQLException Если возникает ошибка при доступе к данным из ResultSet.
     */
    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        return new Transaction(
                resultSet.getString("transaction_type"),
                resultSet.getBigDecimal("amount"),
                resultSet.getTimestamp("timestamp"),
                resultSet.getInt("sender_account_id"),
                resultSet.getInt("receiver_account_id")

        );
    }
}
