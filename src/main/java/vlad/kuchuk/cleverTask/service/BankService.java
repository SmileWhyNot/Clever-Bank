package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.model.Bank;

/**
 * Сервисный класс для управления информацией о банках.
 */
public class BankService {

    private final BankDAO bankDAO;

    /**
     * Конструктор класса
     *
     * @param bankDAO Объект класса BankDAO, который предоставляет доступ к данным о банках
     */
    public BankService(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    /**
     * Получает имя банка по номеру счета
     *
     * @param accountNumber Номер счета для поиска имени банка
     * @return Имя банка, которому принадлежит этот счет
     */
    public String getBankNameByAccountNumber(String accountNumber) {
        Bank bank = bankDAO.getBankNameByAccountNumber(accountNumber);
        return bank.getName();
    }
}
