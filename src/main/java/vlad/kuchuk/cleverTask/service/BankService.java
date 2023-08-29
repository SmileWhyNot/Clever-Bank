package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.model.Bank;

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
}
