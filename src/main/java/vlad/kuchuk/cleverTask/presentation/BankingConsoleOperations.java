package vlad.kuchuk.cleverTask.presentation;

import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Person;
import vlad.kuchuk.cleverTask.service.AccountService;
import vlad.kuchuk.cleverTask.service.BankService;
import vlad.kuchuk.cleverTask.service.PersonService;
import vlad.kuchuk.cleverTask.service.TransactionService;
import vlad.kuchuk.cleverTask.utils.CheckGenerator;


import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;


/**
 * Класс, обеспечивающий консольное взаимодействие с банковской системой.
 * Позволяет пользователю войти в систему, выбрать операции с аккаунтом и проводить транзакции.
 */
public class BankingConsoleOperations {
    private final AccountService accountService;
    private final PersonService personService;
    private final TransactionService transactionService;
    private final BankService bankService;

    /**
     * Конструктор класса.
     *
     * @param accountService     Сервис для работы с аккаунтами.
     * @param personService      Сервис для работы с пользователями.
     * @param transactionService Сервис для проведения транзакций.
     * @param bankService        Сервис для работы с банками.
     */
    public BankingConsoleOperations(AccountService accountService,
                                    PersonService personService,
                                    TransactionService transactionService,
                                    BankService bankService) {
        this.accountService = accountService;
        this.personService = personService;
        this.transactionService = transactionService;
        this.bankService = bankService;
    }

    /**
     * Запускает консольное приложение банка.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        Person currentUser = login(scanner);

        if (currentUser != null) {
            System.out.println("Добро пожаловать, " + currentUser.getName() + "!");
            handleUserOperations(currentUser, scanner);
        } else {
            System.out.println("Ошибка аутентификации. Попробуйте снова или выйдите из системы.");
        }
    }

    /**
     * Запускает консольное приложение банка с использованием заданного объекта Scanner.
     * <p>
     * Используется для тестирования
     *
     * @param scanner Объект Scanner для ввода пользовательских данных.
     */
    public void start(Scanner scanner) {
        Person currentUser = login(scanner);

        if (currentUser != null) {
            System.out.println("Добро пожаловать, " + currentUser.getName() + "!");
            handleUserOperations(currentUser, scanner);
        } else {
            System.out.println("Ошибка аутентификации. Попробуйте снова или выйдите из системы.");
        }
    }

    /**
     * Обрабатывает операции, выполняемые пользователем в консольном интерфейсе банка.
     *
     * @param currentUser Зарегистрированный пользователь, выполнивший вход в систему.
     * @param scanner     Объект Scanner для ввода пользовательских данных.
     */
    private void handleUserOperations(Person currentUser, Scanner scanner) {
        while (true) {
            displayUserMenu();
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> depositFunds(currentUser, scanner);
                case 2 -> withdrawFunds(currentUser, scanner);
                case 3 -> performMoneyTransfer(currentUser, scanner);
                case 4 -> {
                    System.out.println("Выход из системы.");
                    return;
                }
                default -> System.out.println("Некорректный выбор, попробуйте снова.");
            }
        }
    }

    /**
     * Отображает меню операций пользователя в консоли.
     */
    private void displayUserMenu() {
        System.out.println("Выберите операцию:");
        System.out.println("1. Пополнить счет");
        System.out.println("2. Снять средства со счета");
        System.out.println("3. Перевод средств на счет");
        System.out.println("4. Выйти");
    }

    /**
     * Выполняет процесс аутентификации пользователя по электронной почте.
     * Формат входа через почту выполнен для упрощения инициализации.
     *
     * @param scanner Объект Scanner для ввода email пользователя.
     * @return Возвращает объект Person, представляющий зарегистрированного пользователя, или null, если аутентификация не удалась.
     */
    private Person login(Scanner scanner) {
        System.out.println("Введите email:");
        String email = scanner.next();
        return personService.authenticate(email);
    }

    /**
     * Позволяет пользователю выбрать аккаунт для выполнения операций.
     *
     * @param userAccounts Список аккаунтов пользователя.
     * @param scanner      Объект Scanner для выбора аккаунта.
     * @return Выбранный пользователем аккаунт.
     */
    private Account chooseAccountToWorkWith(List<Account> userAccounts, Scanner scanner) {
        while (true) {
            for (int i = 0; i < userAccounts.size(); i++) {
                System.out.println((i + 1) + ". " + userAccounts.get(i).getAccountNumber());
            }

            int accountChoice = scanner.nextInt();
            if (accountChoice >= 1 && accountChoice <= userAccounts.size()) {
                return userAccounts.get(accountChoice - 1);
            } else {
                System.out.println("Некорректный выбор счета. Попробуйте снова.");
            }
        }
    }

    /**
     * Выполняет операцию пополнения счета пользователя.
     *
     * @param currentUser Зарегистрированный пользователь.
     * @param scanner     Объект Scanner для ввода данных операции.
     */
    private void depositFunds(Person currentUser, Scanner scanner) {
        List<Account> userAccounts = accountService.getAccountsByOwner(currentUser.getId());
        if (userAccounts.isEmpty()) {
            System.out.println("У вас нет счетов.");
            return;
        }

        System.out.println("Выберите счет для пополнения:");
        Account selectedAccount = chooseAccountToWorkWith(userAccounts, scanner);

        System.out.println("Введите сумму для пополнения:");
        BigDecimal amount = scanner.nextBigDecimal();

        transactionService.depositMoney(selectedAccount.getId(), amount);

        String senderBankName = bankService.getBankNameByAccountNumber(selectedAccount.getAccountNumber());
        CheckGenerator.generateCheck("Пополнение", senderBankName, senderBankName,
                selectedAccount.getAccountNumber(), selectedAccount.getAccountNumber(), amount, "check");
        System.out.println("Счет успешно пополнен.");
    }

    /**
     * Выполняет операцию снятия средств со счета пользователя.
     *
     * @param currentUser Зарегистрированный пользователь.
     * @param scanner     Объект Scanner для ввода данных операции.
     */
    private void withdrawFunds(Person currentUser, Scanner scanner) {
        List<Account> userAccounts = accountService.getAccountsByOwner(currentUser.getId());
        if (userAccounts.isEmpty()) {
            System.out.println("У вас нет счетов.");
            return;
        }

        System.out.println("Выберите счет для снятия средств:");
        Account selectedAccount = chooseAccountToWorkWith(userAccounts, scanner);

        System.out.println("Введите сумму для снятия:");
        BigDecimal amount = scanner.nextBigDecimal();

        transactionService.withdrawMoney(selectedAccount.getId(), amount);

        String senderBankName = bankService.getBankNameByAccountNumber(selectedAccount.getAccountNumber());
        CheckGenerator.generateCheck("Снятие", senderBankName, senderBankName,
                selectedAccount.getAccountNumber(), selectedAccount.getAccountNumber(), amount, "check");
        System.out.println("Средства успешно сняты со счета.");
    }

    /**
     * Выполняет операцию перевода средств между счетами.
     *
     * @param currentUser Зарегистрированный пользователь.
     * @param scanner     Объект Scanner для ввода данных операции.
     */
    private void performMoneyTransfer(Person currentUser, Scanner scanner) {
        List<Account> userAccounts = accountService.getAccountsByOwner(currentUser.getId());
        if (userAccounts.isEmpty()) {
            System.out.println("У вас нет счетов.");
            return;
        }

        System.out.println("Выберите свой счет для отправки перевода:");
        Account senderAccount = chooseAccountToWorkWith(userAccounts, scanner);

        System.out.println("Введите номер счета получателя:");
        String receiverAccountNumber = scanner.next();

        System.out.println("Введите сумму для перевода:");
        BigDecimal amount = scanner.nextBigDecimal();

        String result = transactionService.executeMoneyTransfer(senderAccount.getId(), receiverAccountNumber, amount);

        String senderBankName = bankService.getBankNameByAccountNumber(senderAccount.getAccountNumber());
        Account receiverAccount = accountService.getAccountDataByNumber(receiverAccountNumber);
        String receiverBankName = bankService.getBankNameByAccountNumber(receiverAccount.getAccountNumber());

        CheckGenerator.generateCheck("Перевод", senderBankName, receiverBankName,
                senderAccount.getAccountNumber(), receiverAccount.getAccountNumber(), amount, "check");
        System.out.println(result);
    }
}
