package vlad.kuchuk.cleverTask.utils;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.model.Account;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * �����, �������������� ������ ���������� ��������� �� ������ �������������
 * � ������ �������� ���������� ������.
 *
 * <p>
 * ���� ����� ��������� ��������� {@link Runnable}, ����� ��� ����������
 * ����� ���� ��������� � �������� ������ � �������������� ������������.
 *
 * <p>
 * ��� ���������� ��������� �� �����, ������ ��������� ���� ���������� ����������
 * ��������� ��� ������� ����� �, ���� ������ ����� � ������� ���������� ����������,
 * ������������ � ��������� �������� �� ������� ������ �����.
 */
public class InterestCalculationTask implements Runnable{

    private final AccountDAO accountDAO;
    private final Double interestRate; // �������� ��������� �� ����������������� �����

    /**
     * ����������� ������.
     *
     * @param accountDAO   ������ ������ AccountDAO ��� ������� � ������ ������.
     * @param interestRate �������� ���������� ������ ��� ���������� ���������.
     */
    public InterestCalculationTask(AccountDAO accountDAO, Double interestRate) {
        this.accountDAO = accountDAO;
        this.interestRate = interestRate;
    }

    /**
     * ��������� ������ ���������� ��������� �� ������ �������������.
     * ������ ��������� ���� ���������� ���������� ��������� ��� ������� �����
     * �, ���� ������ ����� � ������� ���������� ����������, ������������ � ���������
     * �������� �� ������� ������ �����.
     */
    @Override
    public void run() {
        List<Account> accounts = accountDAO.getAllAccounts();

        Date currentDate = new Date();

        for (Account account : accounts) {
            account.lock();

            Date lastInterestCalculationDate = account.getLastInterestCalculationDate();

            try {
                if (!isSameMonth(lastInterestCalculationDate, currentDate)) {
                    // ����������� ���������� ���������
                    BigDecimal currentBalance = account.getBalance();
                    BigDecimal interest = currentBalance.multiply(BigDecimal.valueOf(interestRate));

                    // �������� ������ � ������ ���������� ���������
                    BigDecimal newBalance = currentBalance.add(interest);
                    accountDAO.updateBalance(account.getId(), newBalance);

                    account.setLastInterestCalculationDate(currentDate);
                    java.sql.Date curSQLDate = new java.sql.Date(currentDate.getTime());
                    accountDAO.updateLastInterestCalculationDate(account.getId(), curSQLDate);

                    // ��������� ���������� ���������
                    System.out.println("���������� ��������� ��� ����� " + account.getAccountNumber() +
                            ": " + interest + " (�������� ������: " + newBalance + ")");
                }

            } finally {
                account.unlock();
            }
        }
    }

    /**
     * ���������, �������� �� ��� ���� ����� � ��� �� ������� � �����.
     *
     * @param date1 ������ ���� ��� ���������.
     * @param date2 ������ ���� ��� ���������.
     * @return true, ���� ���� ����������� ������ ������ � ����, � ��������� ������ - false.
     */
    private boolean isSameMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }
}
