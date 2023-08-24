package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.model.Account;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void deposit(int accountId, BigDecimal amount) {
        Account account = accountDAO.getById(accountId);
        accountDAO.updateBalance(accountId, account.getBalance().add(amount));
    }

    public void withdraw(int accountId, BigDecimal amount) {
        Account account = accountDAO.getById(accountId);
        accountDAO.updateBalance(accountId, account.getBalance().subtract(amount));
    }

    public List<Account> getAccountsByOwner(int id) {
        return accountDAO.getAllAccountsForUser(id);
    }
}
