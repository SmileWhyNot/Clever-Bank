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

    // TODO JavaDoc
    public List<Bank> getAllBanks() {
        return bankDAO.getAllBanks();
    }
    // TODO JavaDoc
    public Bank getBankById(int bankId) {
        return bankDAO.getBankById(bankId);
    }
    // TODO JavaDoc
    public void createBank(Bank newBank) {
        bankDAO.addBank(newBank);
    }
    // TODO JavaDoc
    public void updateBankById(Bank updatedBank, int bankId) {
        bankDAO.updateBank(updatedBank, bankId);
    }
    // TODO JavaDoc
    public void deleteBank(int bankId) {
        bankDAO.deleteBankById(bankId);
    }
}
