package vlad.kuchuk.cleverTask.dao;

import vlad.kuchuk.cleverTask.model.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * �����, ��������������� ������ � ������ ������ � ���� ������.
 */
public class BankDAO {

    private final Connection connection;

    /**
     * ����������� ������.
     *
     * @param connection ���������� � ����� ������.
     */
    public BankDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * �������� �������� ����� �� ������ ����� ������������
     *
     * @param accountNumber ����� ����� ������������ �����
     * @return ������ ����� � ��� �������������
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

    //TODO JavaDoc
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

    // TODO JavaDoc
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

    // TODO JavaDoc
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

    // TODO JavaDoc
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

    // TODO JavaDoc
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
     * ����������� ��������� ������� �� ResultSet � ������ ����� (Bank).
     *
     * @param resultSet ��������� SQL-�������, ���������� ������ � �����.
     * @return ������ ����� (Bank) � ������� �� ResultSet.
     * @throws SQLException � ������ ������������� ������ ��� ������� � ������ �� ResultSet.
     */
    private Bank  mapResultSetToBank(ResultSet resultSet) throws SQLException {
        return new Bank(resultSet.getString("name"));
    }
}
