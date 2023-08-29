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
}
