package vlad.kuchuk.cleverTask.models;

import org.junit.jupiter.api.Test;
import vlad.kuchuk.cleverTask.model.Bank;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankTest {

    @Test
    void testGettersAndSetters() {
        // Создаем объект Bank
        Bank bank = new Bank("MyBank");

        // Устанавливаем значения с помощью сеттеров
        bank.setId(1);
        List<Integer> accountIds = new ArrayList<>();
        accountIds.add(101);
        accountIds.add(102);
        bank.setAccountIds(accountIds);

        // Проверяем, что значения можно получить с помощью геттеров
        assertEquals(1, bank.getId());
        assertEquals("MyBank", bank.getName());
        assertEquals(accountIds, bank.getAccountIds());

        // Проверяем, что геттеры и сеттеры работают корректно
        assertEquals(101, bank.getAccountIds().get(0));
        assertEquals(102, bank.getAccountIds().get(1));
    }
}
