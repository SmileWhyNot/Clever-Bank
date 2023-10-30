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
import vlad.kuchuk.cleverTask.model.Bank;
import vlad.kuchuk.cleverTask.service.BankService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BankServletTest {

    @InjectMocks
    private BankServlet bankServlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private BankService bankService;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
    }

    @Test
    @DisplayName("getAllBanks")
    void testDoGetAllBanks() throws IOException {
        try {
            PrintWriter writer = new PrintWriter(stringWriter);
            Mockito.when(response.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Bank> mockBanks = Arrays.asList(
                new Bank(1, "Bank A", Arrays.asList(1, 2, 3)),
                new Bank(2, "Bank B", Arrays.asList(4, 5))
        );
        Mockito.when(bankService.getAllBanks()).thenReturn(mockBanks);
        Mockito.when(request.getParameter("action")).thenReturn(null);

        bankServlet.doGet(request, response);

        String responseContent = stringWriter.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Bank> banks = objectMapper.readValue(responseContent, new TypeReference<>() {
        });

        assertEquals(2, banks.size());

        Bank bank1 = banks.get(0);
        assertEquals(1, bank1.getId());
        assertEquals("Bank A", bank1.getName());
        assertEquals(Arrays.asList(1, 2, 3), bank1.getAccountIds());

        Bank bank2 = banks.get(1);
        assertEquals(2, bank2.getId());
        assertEquals("Bank B", bank2.getName());
        assertEquals(Arrays.asList(4, 5), bank2.getAccountIds());
    }

    @Test
    @DisplayName("getBankById")
    void testDoGetBankById() throws IOException {
        try {
            PrintWriter writer = new PrintWriter(stringWriter);
            Mockito.when(response.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mockito.when(bankService.getBankById(1)).thenReturn(new Bank(1, "Bank A", Arrays.asList(1, 2, 3)));
        Mockito.when(request.getParameter("action")).thenReturn("get");
        Mockito.when(request.getParameter("id")).thenReturn("1");

        bankServlet.doGet(request, response);

        String responseContent = stringWriter.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        Bank bank = objectMapper.readValue(responseContent, Bank.class);

        assertEquals(1, bank.getId());
        assertEquals("Bank A", bank.getName());
        assertEquals(Arrays.asList(1, 2, 3), bank.getAccountIds());
    }

    @Test
    @DisplayName("createBank")
    void testDoPostCreateBank() {
        Mockito.when(request.getParameter("action")).thenReturn("create");
        Mockito.when(request.getParameter("name")).thenReturn("New Bank");

        bankServlet.doPost(request, response);

        Mockito.verify(bankService).createBank(Mockito.any(Bank.class));
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("updateBank")
    void testDoPostUpdateBank() {
        Mockito.when(request.getParameter("action")).thenReturn("update");
        Mockito.when(request.getParameter("id")).thenReturn("1");
        Mockito.when(request.getParameter("name")).thenReturn("Updated Bank");

        bankServlet.doPost(request, response);

        Mockito.verify(bankService).updateBankById(Mockito.any(Bank.class), Mockito.anyInt());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("deleteBank")
    void testDoPostDeleteBank() {
        Mockito.when(request.getParameter("action")).thenReturn("delete");
        Mockito.when(request.getParameter("id")).thenReturn("1");

        bankServlet.doPost(request, response);

        Mockito.verify(bankService).deleteBank(Mockito.anyInt());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
