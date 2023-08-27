package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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


}
