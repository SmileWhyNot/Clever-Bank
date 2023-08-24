package vlad.kuchuk.cleverTask;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.PersonDAO;
import vlad.kuchuk.cleverTask.database.DatabaseConnection;
import vlad.kuchuk.cleverTask.presentation.BankingConsoleOperations;
import vlad.kuchuk.cleverTask.service.AccountService;
import vlad.kuchuk.cleverTask.service.PersonService;

public class Main {
    public static void main(String[] args) {
        // Создаем DAO классы
        PersonDAO personDAO = new PersonDAO(DatabaseConnection.getConnection());
        AccountDAO accountDAO = new AccountDAO(DatabaseConnection.getConnection());

        // Создаем сервисы
        PersonService personService = new PersonService(personDAO);
        AccountService accountService = new AccountService(accountDAO);

        // Создаем объект для взаимодействия с приложением
        BankingConsoleOperations bankingConsoleOperations =
                new BankingConsoleOperations(accountService, personService);

        try {
            bankingConsoleOperations.start();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}