package vlad.kuchuk.cleverTask.model;


import lombok.Data;

import java.util.List;


@Data
public class Bank {
    private int id;
    private final String name;
    private List<Integer> accountIds;
}
