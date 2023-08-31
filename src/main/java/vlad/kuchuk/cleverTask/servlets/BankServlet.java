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

/**
 * Класс BankServlet представляет сервлет для управления операциями с банками.
 * Он обрабатывает HTTP GET и POST запросы, связанные с банками, такие как получение списка банков,
 * получение информации о конкретном банке, создание нового банка, обновление существующего банка и удаление банка.
 */
@WebServlet("/bank")
public class BankServlet extends HttpServlet {

    private BankService bankService;

    /**
     * Метод инициализации сервлета, который создает экземпляры BankService и BankDAO для выполнения операций с банками.
     *
     * @param config Объект, предоставляющий информацию о конфигурации сервлета.
     * @throws ServletException Если возникает ошибка при инициализации сервлета.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        BankDAO bankDAO = new BankDAO(DatabaseConnection.getConnection());
        bankService = new BankService(bankDAO);
    }

    /**
     * Обрабатывает HTTP GET запросы, связанные с операциями с банками.
     *
     * @param request  Объект HttpServletRequest, представляющий HTTP запрос.
     * @param response Объект HttpServletResponse, представляющий HTTP ответ.
     * @throws IOException      Если возникает ошибка ввода/вывода при отправке ответа.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    /**
     * Обрабатывает HTTP POST запросы, связанные с операциями создания, обновления и удаления банков.
     *
     * @param request  Объект HttpServletRequest, представляющий HTTP запрос.
     * @param response Объект HttpServletResponse, представляющий HTTP ответ.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
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
