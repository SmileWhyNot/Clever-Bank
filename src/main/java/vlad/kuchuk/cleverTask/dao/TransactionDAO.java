package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// TODO JavaDoc
public class TransactionDAO {
    private final Connection connection;

    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

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
            // Обработка ошибок, например, логирование или выброс исключения
        }
    }
}
