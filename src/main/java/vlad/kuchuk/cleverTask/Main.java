package vlad.kuchuk.cleverTask;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.PersonDAO;
import vlad.kuchuk.cleverTask.dao.TransactionDAO;
import vlad.kuchuk.cleverTask.database.DatabaseConnection;
import vlad.kuchuk.cleverTask.presentation.BankingConsoleOperations;
import vlad.kuchuk.cleverTask.service.AccountService;
import vlad.kuchuk.cleverTask.service.PersonService;
import vlad.kuchuk.cleverTask.service.TransactionService;
import vlad.kuchuk.cleverTask.utils.InterestCalculationScheduler;
import vlad.kuchuk.cleverTask.utils.YamlReader;


public class Main {
    public static void main(String[] args) {
        // Создаем DAO классы
        PersonDAO personDAO = new PersonDAO(DatabaseConnection.getConnection());
        AccountDAO accountDAO = new AccountDAO(DatabaseConnection.getConnection());
        TransactionDAO transactionDAO = new TransactionDAO(DatabaseConnection.getConnection());

        // Создаем сервисы
        PersonService personService = new PersonService(personDAO);
        AccountService accountService = new AccountService(accountDAO);
        TransactionService transactionService = new TransactionService(accountDAO, transactionDAO);

        // Запуск проверки начисления процентов
        InterestCalculationScheduler scheduler = new InterestCalculationScheduler(accountDAO, YamlReader.getDouble("interestRate"));
        scheduler.startInterestCalculation(1);

        // Создаем объект для взаимодействия с приложением
        BankingConsoleOperations bankingConsoleOperations =
                new BankingConsoleOperations(accountService, personService, transactionService);

        try {
            bankingConsoleOperations.start();
        } finally {
            DatabaseConnection.closeConnection();
            scheduler.stopInterestCalculation();
        }
    }
}