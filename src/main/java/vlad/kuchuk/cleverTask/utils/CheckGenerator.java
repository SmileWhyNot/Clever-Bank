package vlad.kuchuk.cleverTask.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckGenerator {

    public static void generateCheck(String transactionType, String senderBank,
                                     String receiverBank, String senderAccount,
                                     String receiverAccount, BigDecimal amount) {

        // Создаем уникальный номер чека, например, на основе текущей даты и времени
        String checkNumber = generateCheckNumber();

        // Получаем текущую дату и время
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = new Date();

        // Путь к директории "check"
        String directoryPath = "check";

        try {
            // Создаем директорию, если она не существует
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Открываем файл для записи чека
            BufferedWriter writer = new BufferedWriter(new FileWriter(directoryPath + "/" + checkNumber + ".txt"));

            // Записываем данные в чек
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

            // Закрываем файл
            writer.close();

            System.out.println("Чек успешно сформирован и сохранен.");
        } catch (IOException e) {
            System.err.println("Ошибка при создании чека: " + e.getMessage());
        }
    }

    private static String generateCheckNumber() {
        // Здесь можно реализовать генерацию уникального номера чека
        // Например, на основе текущей даты и времени
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}
