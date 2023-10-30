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
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.service.AccountService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AccountServletTest {

    @InjectMocks
    private AccountServlet accountServlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AccountService accountService;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
    }

    @Test
    @DisplayName("getAllAccounts")
    void testDoGetAllAccounts() throws IOException {
        try {
            PrintWriter writer = new PrintWriter(stringWriter);
            Mockito.when(response.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Account> mockAccounts = Arrays.asList(
                new Account("12345", 1, 1),
                new Account("54321", 2, 2)
        );
        Mockito.when(accountService.getAllAccounts()).thenReturn(mockAccounts);
        Mockito.when(request.getParameter("action")).thenReturn(null);

        accountServlet.doGet(request, response);

        String responseContent = stringWriter.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Account> accounts = objectMapper.readValue(responseContent, new TypeReference<>() {
        });

        assertEquals(2, accounts.size());

        Account account1 = accounts.get(0);
        assertEquals("12345", account1.getAccountNumber());
        assertEquals(1, account1.getPersonId());
        assertEquals(1, account1.getBankId());

        Account account2 = accounts.get(1);
        assertEquals("54321", account2.getAccountNumber());
        assertEquals(2, account2.getPersonId());
        assertEquals(2, account2.getBankId());

    }

    @Test
    @DisplayName("getAccountById")
    void testDoGetAccountById() throws IOException {
        try {
            PrintWriter writer = new PrintWriter(stringWriter);
            Mockito.when(response.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mockito.when(accountService.getAccountById(1)).thenReturn(new Account("12345", 1, 1));
        Mockito.when(request.getParameter("action")).thenReturn("get");
        Mockito.when(request.getParameter("id")).thenReturn("1");

        accountServlet.doGet(request, response);

        String responseContent = stringWriter.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        Account account1 = objectMapper.readValue(responseContent, Account.class);

        assertEquals("12345", account1.getAccountNumber());
        assertEquals(1, account1.getPersonId());
        assertEquals(1, account1.getBankId());
    }

    @Test
    @DisplayName("createAccount")
    void testDoPostCreateAccount() {
        Mockito.when(request.getParameter("action")).thenReturn("create");
        Mockito.when(request.getParameter("accountNumber")).thenReturn("12345");
        Mockito.when(request.getParameter("personId")).thenReturn("1");
        Mockito.when(request.getParameter("bankId")).thenReturn("1");

        accountServlet.doPost(request, response);

        Mockito.verify(accountService).createAccount(Mockito.any(Account.class));
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("updateAccount")
    void testDoPostUpdateAccount() {
        Mockito.when(request.getParameter("action")).thenReturn("update");
        Mockito.when(request.getParameter("accountId")).thenReturn("1");
        Mockito.when(request.getParameter("accountNumber")).thenReturn("12345");
        Mockito.when(request.getParameter("personId")).thenReturn("1");
        Mockito.when(request.getParameter("bankId")).thenReturn("1");

        accountServlet.doPost(request, response);

        Mockito.verify(accountService).updateAccountById(Mockito.any(Account.class), Mockito.anyInt());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("deleteAccount")
    void testDoPostDeleteAccount() {
        Mockito.when(request.getParameter("action")).thenReturn("delete");
        Mockito.when(request.getParameter("accountId")).thenReturn("1");

        accountServlet.doPost(request, response);

        Mockito.verify(accountService).deleteAccountById(Mockito.anyInt());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
