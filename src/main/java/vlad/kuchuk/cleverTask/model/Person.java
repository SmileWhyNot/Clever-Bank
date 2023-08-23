package vlad.kuchuk.cleverTask.model;

import lombok.Data;

import java.util.List;

@Data
public class Person {
    private int id;
    private final String name;
    private final String email;
    private List<Account> accounts;
}
