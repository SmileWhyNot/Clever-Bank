package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.model.Bank;

import java.util.List;

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

    /**
     * Получает список всех банков.
     *
     * @return Список всех банков.
     */
    public List<Bank> getAllBanks() {
        return bankDAO.getAllBanks();
    }

    /**
     * Получает банк по его идентификатору.
     *
     * @param bankId Идентификатор банка.
     * @return Банк с указанным идентификатором или null, если банк не найден.
     */
    public Bank getBankById(int bankId) {
        return bankDAO.getBankById(bankId);
    }

    /**
     * Создает новый банк.
     *
     * @param newBank Новый банк, который необходимо создать.
     */
    public void createBank(Bank newBank) {
        bankDAO.addBank(newBank);
    }

    /**
     * Обновляет существующий банк по его идентификатору.
     *
     * @param updatedBank Обновленные данные банка.
     * @param bankId      Идентификатор банка, который необходимо обновить.
     */
    public void updateBankById(Bank updatedBank, int bankId) {
        bankDAO.updateBank(updatedBank, bankId);
    }

    /**
     * Удаляет банк по его идентификатору.
     *
     * @param bankId Идентификатор банка, который необходимо удалить.
     */
    public void deleteBank(int bankId) {
        bankDAO.deleteBankById(bankId);
    }
}
