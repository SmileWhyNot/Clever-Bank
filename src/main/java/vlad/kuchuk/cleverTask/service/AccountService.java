package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.TransactionDAO;
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
}
