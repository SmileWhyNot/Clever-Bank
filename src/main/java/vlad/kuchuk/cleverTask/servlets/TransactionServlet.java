package vlad.kuchuk.cleverTask.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.TransactionDAO;
import vlad.kuchuk.cleverTask.database.DatabaseConnection;
import vlad.kuchuk.cleverTask.model.Transaction;
import vlad.kuchuk.cleverTask.service.TransactionService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * ������� ��� ���������� ���������� CRUD (��������, ������, ����������, ��������) ��������� Transaction.
 * ������������ HTTP-�������, ��������� � ��������� Transaction.
 */
@WebServlet("/transaction")
public class TransactionServlet extends HttpServlet {

    private TransactionService transactionService;

    /**
     * ������������� ��������, �������� ����������� TransactionDAO � TransactionService.
     *
     * @param config ������������ ��������.
     * @throws ServletException � ������ ������ ��� ������������� ��������.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        TransactionDAO transactionDAO = new TransactionDAO(DatabaseConnection.getConnection());
        AccountDAO accountDAO = new AccountDAO(DatabaseConnection.getConnection());
        transactionService = new TransactionService(accountDAO, transactionDAO);
    }

    /**
     * ������������ HTTP GET-�������, ������������ ���������� � �������� Transaction.
     *
     * @param request  ������ �� �������.
     * @param response ����� �������.
     * @throws IOException � ������ ������ �����/������.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        if (action == null) {
            List<Transaction> transactions = transactionService.getAllTransactions();
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), transactions);
        } else if (action.equals("get")) {
            int transactionId = Integer.parseInt(request.getParameter("id"));
            Transaction transaction = transactionService.getTransactionById(transactionId);
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), transaction);
        }
    }

    /**
     * ������������ HTTP POST-������� ��� ��������, ���������� � �������� �������� Transaction.
     *
     * @param request  ������ �� �������.
     * @param response ����� �������.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        switch (action) {
            case "create" -> {
                Transaction transaction = getTransactionWithParams(request);

                transactionService.createTransaction(transaction);
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
            case "update" -> {
                Transaction updatedTransaction = getTransactionWithParams(request);
                int transactionId = Integer.parseInt(request.getParameter("id"));
                transactionService.updatePersonById(updatedTransaction, transactionId);
                response.setStatus(HttpServletResponse.SC_OK);
            }
            case "delete" -> {
                int transactionId = Integer.parseInt(request.getParameter("id"));
                transactionService.deleteTransaction(transactionId);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    /**
     * �������� ��������� �� HTTP-�������, � ������� ��������� Transaction �� ������ ���� ����������.
     *
     * @param request ������ �� �������.
     * @return ��������� Transaction, ��������� �� ������ ���������� �� �������.
     */
    private Transaction getTransactionWithParams(HttpServletRequest request) {
        String transactionType = request.getParameter("transaction_type");
        BigDecimal amount = new BigDecimal(request.getParameter("amount"));
        Timestamp timestamp = Timestamp.valueOf(request.getParameter("timestamp"));
        int senderAccountId = Integer.parseInt(request.getParameter("sender_account_id"));
        int receiverAccountId = Integer.parseInt(request.getParameter("receiver_account_id"));
        return new Transaction(transactionType, amount, timestamp,
                senderAccountId, receiverAccountId);
    }
}
