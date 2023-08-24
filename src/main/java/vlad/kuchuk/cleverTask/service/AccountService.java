package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.model.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервисный класс для управления счетами.
 */
public class AccountService {
    private final AccountDAO accountDAO;

    /**
     * Конструктор класса.
     *
     * @param accountDAO Объект класса AccountDAO, который предоставляет доступ к данным счетов.
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Пополняет счет на указанную сумму.
     *
     * @param accountId Идентификатор счета, который нужно пополнить.
     * @param amount    Сумма, на которую следует увеличить баланс счета.
     */
    public void deposit(int accountId, BigDecimal amount) {
        Account account = accountDAO.getById(accountId);
        accountDAO.updateBalance(accountId, account.getBalance().add(amount));
    }

    /**
     * Снимает средства с указанного счета.
     *
     * @param accountId Идентификатор счета, с которого нужно снять средства.
     * @param amount    Сумма, которую следует снять со счета.
     */
    public void withdraw(int accountId, BigDecimal amount) {
        Account account = accountDAO.getById(accountId);
        accountDAO.updateBalance(accountId, account.getBalance().subtract(amount));
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
