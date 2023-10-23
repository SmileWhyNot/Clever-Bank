package vlad.kuchuk.cleverTask.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;


class CheckGeneratorTest {

    private static final String DIRECTORY_PATH = "checkTest";

    @BeforeEach
    public void setUp() throws IOException {
        Files.createDirectories(Paths.get(DIRECTORY_PATH));
    }

    @Test
    @DisplayName("shouldGenerateCheck")
    void testGenerateCheck() throws IOException {
        String transactionType = "перевод";
        String senderBank = "Банк1";
        String receiverBank = "Банк2";
        String senderAccount = "12345";
        String receiverAccount = "54321";
        BigDecimal amount = new BigDecimal("100.00");

        Path dir = Paths.get(DIRECTORY_PATH);
        int initialFileCount = Files.list(dir).toArray().length;

        CheckGenerator.generateCheck(transactionType, senderBank, receiverBank, senderAccount, receiverAccount, amount, "checkTest");

        int newFileCount = Files.list(dir).toArray().length;
        assertEquals(initialFileCount + 1, newFileCount);
    }
}