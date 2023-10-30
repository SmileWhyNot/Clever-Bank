package vlad.kuchuk.cleverTask.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.database.DatabaseConnection;
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.service.AccountService;

import java.io.IOException;
import java.util.List;

/**
 * Сервлет для управления операциями CRUD (создание, чтение, обновление, удаление) сущностей Account.
 */
@WebServlet("/account")
public class AccountServlet extends HttpServlet {
    private AccountService accountService;

    /**
     * Инициализация сервлета, создание экземпляров AccountDAO и AccountService.
     *
     * @throws ServletException В случае ошибки при инициализации сервлета.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        AccountDAO accountDAO = new AccountDAO(DatabaseConnection.getConnection());
        accountService = new AccountService(accountDAO);
    }

    /**
     * Обрабатывает GET-запросы для получения информации о счетах.
     *
     * @param request  Запрос от клиента.
     * @param response Ответ клиенту.
     * @throws IOException В случае ошибки ввода/вывода.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String action = request.getParameter("action");
        if (action == null) {
            List<Account> accounts = accountService.getAllAccounts();
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), accounts);
        } else if (action.equals("get")) {
            int accountId = Integer.parseInt(request.getParameter("id"));
            Account account = accountService.getAccountById(accountId);
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), account);
        }
    }

    /**
     * Обрабатывает POST-запросы для создания, обновления и удаления счетов.
     *
     * @param request  Запрос от клиента.
     * @param response Ответ клиенту.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        switch (action) {
            case "create" -> {
                String accountNumber = request.getParameter("accountNumber");
                int personId = Integer.parseInt(request.getParameter("personId"));
                int bankId = Integer.parseInt(request.getParameter("bankId"));
                Account account = new Account(accountNumber, personId, bankId);
                accountService.createAccount(account);
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
            case "update" -> {
                int accountId = Integer.parseInt(request.getParameter("accountId"));
                String newAccountNumber = request.getParameter("accountNumber");
                int newPersonId = Integer.parseInt(request.getParameter("personId"));
                int newBankId = Integer.parseInt(request.getParameter("bankId"));
                Account updatedAccount = new Account(newAccountNumber, newPersonId, newBankId);
                accountService.updateAccountById(updatedAccount, accountId);
                response.setStatus(HttpServletResponse.SC_OK);
            }
            case "delete" -> {
                int accountId = Integer.parseInt(request.getParameter("accountId"));
                accountService.deleteAccountById(accountId);
                response.setStatus(HttpServletResponse.SC_OK);
            }
            default -> response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
