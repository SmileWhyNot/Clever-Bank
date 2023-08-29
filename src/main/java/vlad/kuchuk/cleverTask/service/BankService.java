package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.model.Bank;

public class BankService {

    private final BankDAO bankDAO;

    public BankService(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    public String getBankNameByAccountNumber(String accountNumber) {
        Bank bank = bankDAO.getBankNameByAccountNumber(accountNumber);
        return bank.getName();
    }
}
