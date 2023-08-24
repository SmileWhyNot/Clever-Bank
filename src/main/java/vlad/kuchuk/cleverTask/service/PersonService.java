package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.PersonDAO;
import vlad.kuchuk.cleverTask.model.Person;

public class PersonService {

    private final PersonDAO personDAO;

    public PersonService(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    public Person authenticate(String email) {
        return personDAO.getPersonByEmail(email);
    }
}
