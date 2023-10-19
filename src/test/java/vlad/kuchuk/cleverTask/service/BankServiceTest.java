package vlad.kuchuk.cleverTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.model.Bank;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @InjectMocks
    private BankService bankService;

    @Mock
    private BankDAO bankDAO;

    @Test
    void testGetBankNameByAccountNumber() {
        String accountNumber = "12345";
        String bankName = "Example Bank";

        Bank bank = new Bank(bankName);

        when(bankDAO.getBankNameByAccountNumber(accountNumber)).thenReturn(bank);

        String result = bankService.getBankNameByAccountNumber(accountNumber);

        assertEquals(bankName, result);
        verify(bankDAO, times(1)).getBankNameByAccountNumber(accountNumber);
    }
}

