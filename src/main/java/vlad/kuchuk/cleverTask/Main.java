package vlad.kuchuk.cleverTask;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.dao.PersonDAO;
import vlad.kuchuk.cleverTask.dao.TransactionDAO;
import vlad.kuchuk.cleverTask.database.DatabaseConnection;
import vlad.kuchuk.cleverTask.presentation.BankingConsoleOperations;
import vlad.kuchuk.cleverTask.service.AccountService;
import vlad.kuchuk.cleverTask.service.BankService;
import vlad.kuchuk.cleverTask.service.PersonService;
import vlad.kuchuk.cleverTask.service.TransactionService;
import vlad.kuchuk.cleverTask.utils.InterestCalculationScheduler;
import vlad.kuchuk.cleverTask.utils.YamlReader;

import java.util.Scanner;

/**
 * Главный класс приложения, который запускает его и управляет основными компонентами.
 * Включает в себя инициализацию DAO классов, настройку сервисов и запуск задачи начисления процентов.
 * После выполнения основной логики приложения закрывает соединение с базой данных и останавливает задачу.
 */
public class Main {
    /**
     * Метод main, который запускает консольное приложение.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        // Создаем DAO классы
        PersonDAO personDAO = new PersonDAO(DatabaseConnection.getConnection());
        AccountDAO accountDAO = new AccountDAO(DatabaseConnection.getConnection());
        TransactionDAO transactionDAO = new TransactionDAO(DatabaseConnection.getConnection());
        BankDAO bankDAO = new BankDAO(DatabaseConnection.getConnection());

        // Создаем сервисы
        PersonService personService = new PersonService(personDAO);
        AccountService accountService = new AccountService(accountDAO);
        TransactionService transactionService = new TransactionService(accountDAO, transactionDAO);
        BankService bankService = new BankService(bankDAO);

        // Запуск проверки начисления процентов
        InterestCalculationScheduler scheduler = new InterestCalculationScheduler(accountDAO, bankDAO, YamlReader.getDouble("interestRate"));
        scheduler.startInterestCalculation(30);

        // Создаем объект для взаимодействия с приложением
        BankingConsoleOperations bankingConsoleOperations =
                new BankingConsoleOperations(accountService, personService, transactionService, bankService);

        try {
            bankingConsoleOperations.start(new Scanner(System.in));
        } finally {
            DatabaseConnection.closeConnection();
            scheduler.stopInterestCalculation();
        }
    }
}