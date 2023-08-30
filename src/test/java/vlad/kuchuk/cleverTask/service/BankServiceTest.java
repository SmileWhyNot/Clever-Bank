package vlad.kuchuk.cleverTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.model.Bank;
import vlad.kuchuk.cleverTask.service.BankService;

public class BankServiceTest {

    @InjectMocks
    private BankService bankService;

    @Mock
    private BankDAO bankDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBankNameByAccountNumber() {
        String accountNumber = "12345";
        String bankName = "Example Bank";

        // ������� ��� ������� Bank, ������� ����� ������������ ������� bankDAO.getBankNameByAccountNumber()
        Bank bank = new Bank(bankName);

        // ���������, ��� ��� ������ ������ bankDAO.getBankNameByAccountNumber() � ���������� "12345",
        // ������ ���� ��������� ��������� ��� ������� Bank
        when(bankDAO.getBankNameByAccountNumber(accountNumber)).thenReturn(bank);

        // �������� �����, ������� �� ����� ��������������
        String result = bankService.getBankNameByAccountNumber(accountNumber);

        // ���������, ��� ��������� ��������� � ��������� � ��� ����� bankDAO.getBankNameByAccountNumber()
        // ��� ������ ����� ���� ��� � ���������� "12345"
        assertEquals(bankName, result);
        verify(bankDAO, times(1)).getBankNameByAccountNumber(accountNumber);
    }
}

