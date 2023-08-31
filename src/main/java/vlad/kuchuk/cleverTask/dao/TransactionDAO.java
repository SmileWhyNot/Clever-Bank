package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Account;
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

    // TODO JavaDoc
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


    // TODO JavaDoc
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

    // TODO JavaDoc
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
    // TODO JavaDoc
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

    // TODO JavaDoc
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

    // TODO JavaDoc
    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction(
                resultSet.getString("transaction_type"),
                resultSet.getBigDecimal("amount"),
                resultSet.getTimestamp("timestamp"),
                resultSet.getInt("sender_account_id"),
                resultSet.getInt("receiver_account_id")

        );
        return transaction;
    }
}
