package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
     * Возвращает список всех банков, имеющихся в базе данных.
     *
     * @return Список объектов Bank, представляющих все банки в базе данных.
     */
    public List<Bank> getAllBanks() {
        String sql = "SELECT * FROM bank";
        List<Bank> banks = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                banks.add(mapResultSetToBank(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banks;
    }

    /**
     * Возвращает банк с указанным идентификатором.
     *
     * @param bankId Идентификатор банка, который требуется получить.
     * @return Объект Bank, представляющий банк с указанным идентификатором.
     */
    public Bank getBankById(int bankId) {
        String sql = "SELECT * FROM bank WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bankId);
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
     * Добавляет новый банк в базу данных.
     *
     * @param newBank Объект Bank, представляющий новый банк для добавления.
     */
    public void addBank(Bank newBank) {
        String sql = "INSERT INTO bank (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newBank.getName());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bank added successfully.");
            } else {
                System.err.println("Failed to add bank.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обновляет существующий банк в базе данных.
     *
     * @param updatedBank Объект Bank с обновленными данными.
     * @param bankId      Идентификатор банка, который требуется обновить.
     */
    public void updateBank(Bank updatedBank, int bankId) {
        String sql = "UPDATE bank SET name = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, updatedBank.getName());
            preparedStatement.setInt(2, bankId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bank updated successfully.");
            } else {
                System.err.println("Failed to update bank.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаляет банк из базы данных по его идентификатору.
     *
     * @param bankId Идентификатор банка, который требуется удалить.
     */
    public void deleteBankById(int bankId) {
        String sql = "DELETE FROM bank WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, bankId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Bank deleted successfully.");
            } else {
                System.err.println("No bank found with id: " + bankId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
