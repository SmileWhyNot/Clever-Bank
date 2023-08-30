package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.TransactionDAO;
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Transaction;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;


/**
 * Сервисный класс для управления транзакциями (пополнение, снятие, перевод).
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
        account.setBalance(account.getBalance().add(amount));
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
        account.setBalance(account.getBalance().subtract(amount));
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

        if (senderAccount == null || receiverAccount == null) {
            return "Транзакция отменена. Счет не найден";
        }

        if (senderAccount.getBalance().compareTo(amount) < 0) {
            return "Транзакция отменена. Недостаточно средств";
        }

        senderAccount.lock();
        receiverAccount.lock();

        try {
            boolean isSuccessful = accountDAO.transferFunds(senderAccountId, receiverAccount.getId(), amount);

            // Сохраняем записи о транзакции
            Transaction transaction = new Transaction(
                    new String("перевод".getBytes(), StandardCharsets.UTF_8),
                    amount,
                    new Timestamp(System.currentTimeMillis()),
                    senderAccountId,
                    receiverAccount.getId()
            );

            if (isSuccessful) {
                senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
                receiverAccount.setBalance(receiverAccount.getBalance().add(amount));
                transactionDAO.saveTransaction(transaction);
                return "Транзакция проведена успешно";
            } else {
                return "Возникла ошибка при проведении транзакции";
            }
        } finally {
            senderAccount.unlock();
            receiverAccount.unlock();
        }
    }
}

