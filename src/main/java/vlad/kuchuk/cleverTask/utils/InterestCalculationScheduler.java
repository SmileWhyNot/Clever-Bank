package vlad.kuchuk.cleverTask.utils;

import vlad.kuchuk.cleverTask.dao.AccountDAO;
import vlad.kuchuk.cleverTask.dao.BankDAO;

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
    private ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(2);
    private final AccountDAO accountDAO;
    private final BankDAO bankDAO;
    private final Double interestRate;

    /**
     * ����������� ������.
     *
     * @param accountDAO   ������ ������ AccountDAO ��� ������� � ������ ������.
     * @param bankDAO      ������ ������ BankDAO ��� ������� � ������ � ����� ������������.
     * @param interestRate �������� ���������� ������ ��� ���������� ���������.
     */
    public InterestCalculationScheduler(AccountDAO accountDAO, BankDAO bankDAO, Double interestRate) {
        this.accountDAO = accountDAO;
        this.bankDAO = bankDAO;
        this.interestRate = interestRate;
    }

    public InterestCalculationScheduler(AccountDAO accountDAO, BankDAO bankDAO,
                                        Double interestRate, ScheduledExecutorService scheduler) {
        this.accountDAO = accountDAO;
        this.bankDAO = bankDAO;
        this.interestRate = interestRate;
        this.scheduler = scheduler;
    }

    /**
     * ��������� ������������� ���������� ��������� � �������� ����������.
     *
     * @param intervalSeconds �������� � �������� ����� ������������ ���������.
     */
    public void startInterestCalculation(int intervalSeconds) {
        Runnable interestCalculationTask = new InterestCalculationTask(accountDAO, bankDAO, interestRate);

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
