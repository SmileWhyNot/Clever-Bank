package vlad.kuchuk.cleverTask.presentation;

import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Person;
import vlad.kuchuk.cleverTask.service.AccountService;
import vlad.kuchuk.cleverTask.service.PersonService;
import vlad.kuchuk.cleverTask.service.TransactionService;
import vlad.kuchuk.cleverTask.utils.InterestCalculationScheduler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class BankingConsoleOperations {
    private final AccountService accountService;
    private final PersonService personService;
    private final TransactionService transactionService;

    public BankingConsoleOperations(AccountService accountService,
                                    PersonService personService,
                                    TransactionService transactionService) {
        this.accountService = accountService;
        this.personService = personService;
        this.transactionService = transactionService;
    }

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

    private void displayUserMenu() {
        System.out.println("Выберите операцию:");
        System.out.println("1. Пополнить счет");
        System.out.println("2. Снять средства со счета");
        System.out.println("3. Перевод средств на счет");
        System.out.println("4. Выйти");
    }


    private Person login(Scanner scanner) {
        System.out.println("Введите email:");
        String email = scanner.next();
        return personService.authenticate(email);
    }

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
        System.out.println("Счет успешно пополнен.");
    }

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
        System.out.println("Средства успешно сняты со счета.");
    }

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
        System.out.println(result);
    }

}
