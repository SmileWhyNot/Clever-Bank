package vlad.kuchuk.cleverTask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vlad.kuchuk.cleverTask.dao.BankDAO;
import vlad.kuchuk.cleverTask.model.Bank;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @InjectMocks
    private BankService bankService;

    @Mock
    private BankDAO bankDAO;

    @Test
    @DisplayName("getBankNameByNumber")
    void testGetBankNameByAccountNumber() {
        String accountNumber = "12345";
        String bankName = "Example Bank";

        Bank bank = new Bank(bankName);

        when(bankDAO.getBankNameByAccountNumber(accountNumber)).thenReturn(bank);

        String result = bankService.getBankNameByAccountNumber(accountNumber);

        assertEquals(bankName, result);
        verify(bankDAO, times(1)).getBankNameByAccountNumber(accountNumber);
    }

    @Test
    @DisplayName("updateBankById")
    void testUpdateBankById() {
        int bankId = 1;
        Bank updatedBank = new Bank("Updated Bank");

        doNothing().when(bankDAO).updateBank(updatedBank, bankId);

        bankService.updateBankById(updatedBank, bankId);

        verify(bankDAO, times(1)).updateBank(updatedBank, bankId);
    }

    @Test
    @DisplayName("getBankById")
    void testGetBankById() {
        int bankId = 1;
        Bank bank = new Bank("Bank 1");

        when(bankDAO.getBankById(bankId)).thenReturn(bank);

        Bank result = bankService.getBankById(bankId);

        assertNotNull(result);
        assertEquals(bank.getName(), result.getName());
    }

    @Test
    @DisplayName("createBank")
    void testCreateBank() {
        Bank newBank = new Bank("New Bank");

        doNothing().when(bankDAO).addBank(newBank);

        bankService.createBank(newBank);

        verify(bankDAO, times(1)).addBank(newBank);
    }

    @Test
    @DisplayName("deleteBank")
    void testDeleteBank() {
        int bankId = 1;

        doNothing().when(bankDAO).deleteBankById(bankId);

        bankService.deleteBank(bankId);

        verify(bankDAO, times(1)).deleteBankById(bankId);
    }

    @Test
    @DisplayName("getAllBanks")
    void testGetAllBanks() {
        List<Bank> banks = new ArrayList<>();
        banks.add(new Bank("Bank 1"));
        banks.add(new Bank("Bank 2"));

        when(bankDAO.getAllBanks()).thenReturn(banks);

        List<Bank> result = bankService.getAllBanks();

        assertNotNull(result);
        assertEquals(banks.size(), result.size());
    }
}

