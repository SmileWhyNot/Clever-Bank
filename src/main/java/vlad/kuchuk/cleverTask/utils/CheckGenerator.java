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
 * �����, ��������������� ��� ��������� � ���������� ���������� ����� � �����.
 *
 * <p>
 * ��� ������ �������� ��������� ���������� ��� � ������� ���������� �����. ���
 * �������� ���������� � ���� ����������, ������ ����������� � ����������,
 * ������� ������ ����������� � ����������, ����� � ������� ����������
 * ����������.
 */
@UtilityClass
public class CheckGenerator {

    /**
     * ���������� � ��������� ��� ��� ���������� ��������.
     *
     * @param transactionType ��� ����������, ��������, "�������", "����������", "������".
     * @param senderBank      �������� ����� �����������.
     * @param receiverBank    �������� ����� ����������.
     * @param senderAccount   ����� ����� �����������.
     * @param receiverAccount ����� ����� ����������.
     * @param amount          �����, ��������� � �����������.
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
            writer.write("|                      ���������� ���                        |");
            writer.newLine();
            writer.write("| ���:                                        " + new String(checkNumber.getBytes(), StandardCharsets.UTF_8) + " |");
            writer.newLine();
            writer.write("| " + dateFormat.format(currentDate) + "                                        " + timeFormat.format(currentDate) + " |");
            writer.newLine();
            writer.write("| ��� ����������:                                 " + transactionType + " |");
            writer.newLine();
            writer.write("| ���� �����������:                          " + senderBank + " |");
            writer.newLine();
            writer.write("| ���� ����������:                           " + receiverBank + " |");
            writer.newLine();
            writer.write("| ���� �����������:            " + senderAccount + " |");
            writer.newLine();
            writer.write("| ���� ����������:             " + receiverAccount + " |");
            writer.newLine();
            writer.write("| �����:                                              " + amount + " BYN |");
            writer.newLine();
            writer.write("-------------------------------------------------------------");

            writer.close();

            System.out.println("��� ������� ����������� � ��������.");
        } catch (IOException e) {
            System.err.println("������ ��� �������� ����: " + e.getMessage());
        }
    }

    /**
     * ���������� ���������� ����� ���� �� ������ ������� ���� � �������.
     *
     * @return ���������� ����� ����.
     */
    private static String generateCheckNumber() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}
