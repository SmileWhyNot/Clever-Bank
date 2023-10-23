package vlad.kuchuk.cleverTask.models;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vlad.kuchuk.cleverTask.model.Account;
import vlad.kuchuk.cleverTask.model.Bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("isEqualsWorksCorrectly")
    void testEquals() {
        Bank bank1 = new Bank("Clever");
        Bank bank2 = new Bank("Clever");


        Bank bank3 = new Bank("Clever");
        bank3.setAccountIds(List.of(1, 2, 3));

        assertTrue(bank1.equals(bank2));
        assertTrue(bank1.equals(bank1));
        assertFalse(bank1.equals(null));
        assertFalse(bank1.equals(bank3));
        bank2.setAccountIds(List.of(1, 2));
        assertFalse(bank2.equals(bank1));
    }

    @Test
    @DisplayName("isHashCodeWorksCorrectly")
    void testHashCode() {
        Bank bank1 = new Bank("Clever");
        Bank bank2 = new Bank("Clever");


        Bank bank3 = new Bank("Clever");
        bank3.setAccountIds(List.of(1, 2, 3));

        assertTrue(bank1.hashCode() == bank2.hashCode());
        Assumptions.assumeTrue(bank1.hashCode() != bank3.hashCode());
    }
}
