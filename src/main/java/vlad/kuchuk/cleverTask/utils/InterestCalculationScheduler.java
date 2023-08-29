package vlad.kuchuk.cleverTask.utils;

import vlad.kuchuk.cleverTask.dao.AccountDAO;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * �����, ���������� �� ������������ � ���������� ������������� ���������� ���������
 * �� ������ ������������� � ������ �������� ���������� ������.
 *
 * <p>
 * ��� ������� �������������� ���������� ��������� ���������� ������� �����
 * {@link #startInterestCalculation(int)} � ������� �������� � �������� �����
 * ������������. ��� ��������� ���������� �������������� ���������� ����� �������
 * ����� {@link #stopInterestCalculation()}.
 */
public class InterestCalculationScheduler {
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(2);
    private final AccountDAO accountDAO;
    private final Double interestRate;

    /**
     * ����������� ������.
     *
     * @param accountDAO   ������ ������ AccountDAO ��� ������� � ������ ������.
     * @param interestRate �������� ���������� ������ ��� ���������� ���������.
     */
    public InterestCalculationScheduler(AccountDAO accountDAO, Double interestRate) {
        this.accountDAO = accountDAO;
        this.interestRate = interestRate;
    }

    /**
     * ��������� ������������� ���������� ��������� � �������� ����������.
     *
     * @param intervalSeconds �������� � �������� ����� ������������ ���������.
     */
    public void startInterestCalculation(int intervalSeconds) {
        Runnable interestCalculationTask = new InterestCalculationTask(accountDAO, interestRate);

        // ��������� ������ ���������� ��������� � ���������� � ���������
        scheduler.scheduleAtFixedRate(
                interestCalculationTask,
                0, // ������ ���������� ������ �����
                intervalSeconds,
                TimeUnit.SECONDS
        );
    }

    /**
     * ������������� ���������� �������������� ���������� ���������.
     */
    public void stopInterestCalculation() {
        scheduler.shutdown();
    }
}
