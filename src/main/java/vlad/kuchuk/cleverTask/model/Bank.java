package vlad.kuchuk.cleverTask.model;


import lombok.Data;

import java.util.List;

/**
 * �����, �������������� ���� � ���������� �������.
 *
 * <p>
 * ������ ���� ����� ���������� �������������, �������� � ������ ��������������� ������,
 * ������������� ����� �����.
 */
@Data
public class Bank {
    private int id;
    private final String name;
    private List<Integer> accountIds;
}
