package vlad.kuchuk.cleverTask.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.model.Transaction;
import vlad.kuchuk.cleverTask.service.TransactionService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TransactionServletTest {

    @InjectMocks
    private TransactionServlet transactionServlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private TransactionService transactionService;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
    }

    @Test
    @DisplayName("getAllTransactions")
    void testDoGetAllTransactions() throws IOException {
        try {
            PrintWriter writer = new PrintWriter(stringWriter);
            Mockito.when(response.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(1, "Deposit", new BigDecimal("100.00"), Timestamp.valueOf("2023-10-01 10:00:00"), 1, 2),
                new Transaction(2, "Withdraw", new BigDecimal("50.00"), Timestamp.valueOf("2023-10-02 15:30:00"), 3, 1)
        );
        Mockito.when(transactionService.getAllTransactions()).thenReturn(mockTransactions);
        Mockito.when(request.getParameter("action")).thenReturn(null);

        transactionServlet.doGet(request, response);

        String responseContent = stringWriter.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Transaction> transactions = objectMapper.readValue(responseContent, new TypeReference<>() {
        });

        assertEquals(2, transactions.size());

        Transaction transaction1 = transactions.get(0);
        assertEquals(1, transaction1.getId());
        assertEquals("Deposit", transaction1.getTransactionType());
        assertEquals(new BigDecimal("100.00"), transaction1.getAmount());
        assertEquals(Timestamp.valueOf("2023-10-01 10:00:00"), transaction1.getTimestamp());
        assertEquals(1, transaction1.getSenderAccountId());
        assertEquals(2, transaction1.getReceiverAccountId());

        Transaction transaction2 = transactions.get(1);
        assertEquals(2, transaction2.getId());
        assertEquals("Withdraw", transaction2.getTransactionType());
        assertEquals(new BigDecimal("50.00"), transaction2.getAmount());
        assertEquals(Timestamp.valueOf("2023-10-02 15:30:00"), transaction2.getTimestamp());
        assertEquals(3, transaction2.getSenderAccountId());
        assertEquals(1, transaction2.getReceiverAccountId());
    }

    @Test
    @DisplayName("getTransactionById")
    void testDoGetTransactionById() throws IOException {
        try {
            PrintWriter writer = new PrintWriter(stringWriter);
            Mockito.when(response.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mockito.when(transactionService.getTransactionById(1)).thenReturn(new Transaction(1, "Deposit", new BigDecimal("100.00"), Timestamp.valueOf("2023-10-01 10:00:00"), 1, 2));
        Mockito.when(request.getParameter("action")).thenReturn("get");
        Mockito.when(request.getParameter("id")).thenReturn("1");

        transactionServlet.doGet(request, response);

        String responseContent = stringWriter.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction transaction = objectMapper.readValue(responseContent, Transaction.class);

        assertEquals(1, transaction.getId());
        assertEquals("Deposit", transaction.getTransactionType());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertEquals(Timestamp.valueOf("2023-10-01 10:00:00"), transaction.getTimestamp());
        assertEquals(1, transaction.getSenderAccountId());
        assertEquals(2, transaction.getReceiverAccountId());
    }

    @Test
    @DisplayName("createTransaction")
    void testDoPostCreateTransaction() {
        Mockito.when(request.getParameter("action")).thenReturn("create");
        Mockito.when(request.getParameter("transaction_type")).thenReturn("Deposit");
        Mockito.when(request.getParameter("amount")).thenReturn("100.00");
        Mockito.when(request.getParameter("timestamp")).thenReturn("2023-10-01 10:00:00");
        Mockito.when(request.getParameter("sender_account_id")).thenReturn("1");
        Mockito.when(request.getParameter("receiver_account_id")).thenReturn("2");

        transactionServlet.doPost(request, response);

        Mockito.verify(transactionService).createTransaction(Mockito.any(Transaction.class));
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("updateTransaction")
    void testDoPostUpdateTransaction() {
        Mockito.when(request.getParameter("action")).thenReturn("update");
        Mockito.when(request.getParameter("id")).thenReturn("1");
        Mockito.when(request.getParameter("transaction_type")).thenReturn("Withdraw");
        Mockito.when(request.getParameter("amount")).thenReturn("50.00");
        Mockito.when(request.getParameter("timestamp")).thenReturn("2023-10-02 15:30:00");
        Mockito.when(request.getParameter("sender_account_id")).thenReturn("3");
        Mockito.when(request.getParameter("receiver_account_id")).thenReturn("1");

        transactionServlet.doPost(request, response);

        Mockito.verify(transactionService).updateTransactionById(Mockito.any(Transaction.class), Mockito.anyInt());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("deleteTransaction")
    void testDoPostDeleteTransaction() {
        Mockito.when(request.getParameter("action")).thenReturn("delete");
        Mockito.when(request.getParameter("id")).thenReturn("1");

        transactionServlet.doPost(request, response);

        Mockito.verify(transactionService).deleteTransaction(Mockito.anyInt());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
