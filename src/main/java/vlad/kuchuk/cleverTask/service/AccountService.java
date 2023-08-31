package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.model.Account;

import java.util.List;

/**
 * Сервисный класс для управления счетами.
 */
public class AccountService {
    private final AccountDAO accountDAO;

    /**
     * Конструктор класса.
     *
     * @param accountDAO     Объект класса AccountDAO, который предоставляет доступ к данным счетов.
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Получает список счетов, принадлежащих указанному владельцу.
     *
     * @param id Идентификатор владельца счетов.
     * @return Список счетов, принадлежащих указанному владельцу.
     */
    public List<Account> getAccountsByOwner(int id) {
        return accountDAO.getAllAccountsForUser(id);
    }

    /**
     * Получает всю информацию о счете по его номеру
     *
     * @param accountNumber Номер счета в банке
     * @return Всю информацию об этом счете
     */
    public Account getAccountDataByNumber(String accountNumber) {
        return accountDAO.getByAccountNumber(accountNumber);
    }

    //TODO JavaDoc
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }
    //TODO JavaDoc
    public Account getAccountById(int accountId) {
        return accountDAO.getById(accountId);
    }
    //TODO JavaDoc
    public void createAccount(Account account) {
        accountDAO.addAccount(account);
    }
    //TODO JavaDoc
    public void updateAccountById(Account updatedAccount, int accountId) {
        accountDAO.updateAccount(updatedAccount, accountId);
    }
    //TODO JavaDoc
    public void deleteAccountById(int accountId) {
        accountDAO.deleteById(accountId);
    }
}
