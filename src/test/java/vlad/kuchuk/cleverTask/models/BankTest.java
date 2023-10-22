package vlad.kuchuk.cleverTask.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vlad.kuchuk.cleverTask.model.Bank;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankTest {

    @Test
    @DisplayName("BankGettersSetters")
    void testGettersAndSetters() {
        Bank bank = new Bank("MyBank");

        bank.setId(1);
        List<Integer> accountIds = new ArrayList<>();
        accountIds.add(101);
        accountIds.add(102);
        bank.setAccountIds(accountIds);

        assertEquals(1, bank.getId());
        assertEquals("MyBank", bank.getName());
        assertEquals(accountIds, bank.getAccountIds());

        assertEquals(101, bank.getAccountIds().get(0));
        assertEquals(102, bank.getAccountIds().get(1));
    }
}
