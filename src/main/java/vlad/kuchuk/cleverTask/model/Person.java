package vlad.kuchuk.cleverTask.model;

import lombok.Data;

import java.util.List;

/**
 * �����, �������������� ���������� ���� � ���������� �������.
 *
 * <p>
 * ������ ���������� ���� ����� ���������� �������������, ���, ����������� �����
 * � ������ ��������������� ������, ������������� ����� ����.
 */
@Data
public class Person {
    private int id;
    private final String name;
    private final String email;
    private List<Integer> accountIds;
}
