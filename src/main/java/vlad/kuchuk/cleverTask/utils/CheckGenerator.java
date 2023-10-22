package vlad.kuchuk.cleverTask.utils;

import lombok.experimental.UtilityClass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс, предназначенный для генерации и сохранения банковских чеков в файлы.
 *
 * <p>
 * Для каждой операции создается уникальный чек в формате текстового файла. Чек
 * содержит информацию о типе транзакции, банках отправителя и получателя,
 * номерах счетов отправителя и получателя, сумме и времени совершения
 * транзакции.
 */
@UtilityClass
public class CheckGenerator {

    /**
     * Генерирует и сохраняет чек для банковской операции.
     *
     * @param transactionType Тип транзакции, например, "перевод", "пополнение", "снятие".
     * @param senderBank      Название банка отправителя.
     * @param receiverBank    Название банка получателя.
     * @param senderAccount   Номер счета отправителя.
     * @param receiverAccount Номер счета получателя.
     * @param amount          Сумма, связанная с транзакцией.
     */
    public static void generateCheck(String transactionType, String senderBank,
                                     String receiverBank, String senderAccount,
                                     String receiverAccount, BigDecimal amount,
                                     String directoryPath) {

        String checkNumber = generateCheckNumber();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = new Date();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(directoryPath + "/" + checkNumber + ".txt"))) {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }



            writer.write("-------------------------------------------------------------");
            writer.newLine();
            writer.write("|                      Банковский Чек                        |");
            writer.newLine();
            writer.write("| Чек:                                        " + new String(checkNumber.getBytes(), StandardCharsets.UTF_8) + " |");
            writer.newLine();
            writer.write("| " + dateFormat.format(currentDate) + "                                        " + timeFormat.format(currentDate) + " |");
            writer.newLine();
            writer.write("| Тип транзакции:                                 " + transactionType + " |");
            writer.newLine();
            writer.write("| Банк отправителя:                          " + senderBank + " |");
            writer.newLine();
            writer.write("| Банк получателя:                           " + receiverBank + " |");
            writer.newLine();
            writer.write("| счет отправителя:            " + senderAccount + " |");
            writer.newLine();
            writer.write("| счет получателя:             " + receiverAccount + " |");
            writer.newLine();
            writer.write("| Сумма:                                              " + amount + " BYN |");
            writer.newLine();
            writer.write("-------------------------------------------------------------");

            writer.close();

            System.out.println("Чек успешно сформирован и сохранен.");
        } catch (IOException e) {
            System.err.println("Ошибка при создании чека: " + e.getMessage());
        }
    }

    /**
     * Генерирует уникальный номер чека на основе текущей даты и времени.
     *
     * @return Уникальный номер чека.
     */
    private static String generateCheckNumber() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}
