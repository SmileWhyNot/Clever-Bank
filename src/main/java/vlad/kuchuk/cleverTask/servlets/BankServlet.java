package vlad.kuchuk.cleverTask.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.database.DatabaseConnection;
import vlad.kuchuk.cleverTask.model.Bank;
import vlad.kuchuk.cleverTask.service.BankService;

import java.io.IOException;
import java.util.List;

@WebServlet("/bank")
public class BankServlet extends HttpServlet {

    private BankService bankService;
    private BankDAO bankDAO;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        bankDAO = new BankDAO(DatabaseConnection.getConnection());
        bankService = new BankService(bankDAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            List<Bank> banks = bankService.getAllBanks();
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), banks);
        } else if (action.equals("get")) {
            int bankId = Integer.parseInt(request.getParameter("id"));
            Bank bank = bankService.getBankById(bankId);
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), bank);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "create" -> {
                String name = request.getParameter("name");
                Bank newBank = new Bank(name);
                bankService.createBank(newBank);
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
            case "update" -> {
                String newName = request.getParameter("name");
                int bankId = Integer.parseInt(request.getParameter("id"));
                Bank updatedBank = new Bank(newName);
                bankService.updateBankById(updatedBank, bankId);
                response.setStatus(HttpServletResponse.SC_OK);
            }
            case "delete" -> {
                int bankId = Integer.parseInt(request.getParameter("id"));
                bankService.deleteBank(bankId);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
