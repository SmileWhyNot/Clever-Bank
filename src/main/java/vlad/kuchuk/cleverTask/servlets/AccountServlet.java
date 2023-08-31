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

// TODO JavaDoc
@WebServlet("/account")
public class AccountServlet extends HttpServlet {
    private AccountService accountService;
    private AccountDAO accountDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        accountDAO = new AccountDAO(DatabaseConnection.getConnection());
        accountService = new AccountService(accountDAO);
    }

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
        }

    }
}
