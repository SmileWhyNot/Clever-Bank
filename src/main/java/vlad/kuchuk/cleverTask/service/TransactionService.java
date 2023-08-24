package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.TransactionDAO;
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Сервисный класс для управления транзакциями.
 */
public class TransactionService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    /**
     * Конструктор класса.
     *
     * @param accountDAO     Объект класса AccountDAO, который предоставляет доступ к данным счетов.
     * @param transactionDAO Объект класса TransactionDAO, который предоставляет доступ к осуществляемым транзакциям.
     */
    public TransactionService(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    /**
     * Пополняет счет на указанную сумму.
     *
     * @param accountId Идентификатор счета, который нужно пополнить.
     * @param amount    Сумма, на которую следует увеличить баланс счета.
     */
    public void depositMoney(int accountId, BigDecimal amount) {
        Account account = accountDAO.getById(accountId);
        accountDAO.updateBalance(accountId, account.getBalance().add(amount));
    }

    /**
     * Снимает средства с указанного счета.
     *
     * @param accountId Идентификатор счета, с которого нужно снять средства.
     * @param amount    Сумма, которую следует снять со счета.
     */
    public void withdrawMoney(int accountId, BigDecimal amount) {
        Account account = accountDAO.getById(accountId);
        accountDAO.updateBalance(accountId, account.getBalance().subtract(amount));
    }

    /**
     * Выполняет перевод средств между счетами.
     *
     * @param senderAccountId      Идентификатор счета отправителя.
     * @param receiverAccountNumber Номер счета получателя.
     * @param amount               Сумма перевода.
     * @return Сообщение об успешной или неудачной транзакции.
     */
    public String executeMoneyTransfer(int senderAccountId, String receiverAccountNumber, BigDecimal amount) {
        Account senderAccount = accountDAO.getById(senderAccountId);
        Account receiverAccount = accountDAO.getByAccountNumber(receiverAccountNumber);

        if (senderAccount != null && receiverAccount != null) {
            if (senderAccount.getBalance().compareTo(amount) >= 0) {
                try {
                    senderAccount.lock();
                    receiverAccount.lock();

                    senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
                    receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

                    // Сохраняем записи о транзакции
                    Transaction transaction = new Transaction(
                            "Перевод",
                            amount,
                            new Timestamp(System.currentTimeMillis()),
                            senderAccountId,
                            receiverAccount.getId()
                    );

                    transactionDAO.saveTransaction(transaction);
                } finally {
                    senderAccount.unlock();
                    receiverAccount.unlock();
                }
            }
            else {
                return "Транзакция отменена. Недостаточно средств";
            }
        } else {
            return "Транзакция отменена. Счет не найден";
        }

        return "Транзакция проведена успешно";
    }
}

