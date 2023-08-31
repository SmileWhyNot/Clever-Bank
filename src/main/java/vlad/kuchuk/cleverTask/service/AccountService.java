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

    /**
     * Получает список всех счетов.
     *
     * @return Список всех счетов.
     */
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    /**
     * Получает счет по его идентификатору.
     *
     * @param accountId Идентификатор счета.
     * @return Счет с указанным идентификатором или null, если счет не найден.
     */
    public Account getAccountById(int accountId) {
        return accountDAO.getById(accountId);
    }

    /**
     * Создает новый счет.
     *
     * @param account Новый счет, который необходимо создать.
     */
    public void createAccount(Account account) {
        accountDAO.addAccount(account);
    }

    /**
     * Обновляет существующий счет по его идентификатору.
     *
     * @param updatedAccount Обновленные данные счета.
     * @param accountId      Идентификатор счета, который необходимо обновить.
     */
    public void updateAccountById(Account updatedAccount, int accountId) {
        accountDAO.updateAccount(updatedAccount, accountId);
    }

    /**
     * Удаляет счет по его идентификатору.
     *
     * @param accountId Идентификатор счета, который необходимо удалить.
     */
    public void deleteAccountById(int accountId) {
        accountDAO.deleteById(accountId);
    }
}
