package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.model.Bank;

import java.util.List;

/**
 * ��������� ����� ��� ���������� ����������� � ������.
 */
public class BankService {

    private final BankDAO bankDAO;

    /**
     * ����������� ������
     *
     * @param bankDAO ������ ������ BankDAO, ������� ������������� ������ � ������ � ������
     */
    public BankService(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    /**
     * �������� ��� ����� �� ������ �����
     *
     * @param accountNumber ����� ����� ��� ������ ����� �����
     * @return ��� �����, �������� ����������� ���� ����
     */
    public String getBankNameByAccountNumber(String accountNumber) {
        Bank bank = bankDAO.getBankNameByAccountNumber(accountNumber);
        return bank.getName();
    }

    /**
     * �������� ������ ���� ������.
     *
     * @return ������ ���� ������.
     */
    public List<Bank> getAllBanks() {
        return bankDAO.getAllBanks();
    }

    /**
     * �������� ���� �� ��� ��������������.
     *
     * @param bankId ������������� �����.
     * @return ���� � ��������� ��������������� ��� null, ���� ���� �� ������.
     */
    public Bank getBankById(int bankId) {
        return bankDAO.getBankById(bankId);
    }

    /**
     * ������� ����� ����.
     *
     * @param newBank ����� ����, ������� ���������� �������.
     */
    public void createBank(Bank newBank) {
        bankDAO.addBank(newBank);
    }

    /**
     * ��������� ������������ ���� �� ��� ��������������.
     *
     * @param updatedBank ����������� ������ �����.
     * @param bankId      ������������� �����, ������� ���������� ��������.
     */
    public void updateBankById(Bank updatedBank, int bankId) {
        bankDAO.updateBank(updatedBank, bankId);
    }

    /**
     * ������� ���� �� ��� ��������������.
     *
     * @param bankId ������������� �����, ������� ���������� �������.
     */
    public void deleteBank(int bankId) {
        bankDAO.deleteBankById(bankId);
    }
}
