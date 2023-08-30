package vlad.kuchuk.cleverTask.service;

import vlad.kuchuk.cleverTask.dao.PersonDAO;
import vlad.kuchuk.cleverTask.model.Person;

import java.util.List;

/**
 * Сервисный класс для управления пользователями банка.
 */
public class PersonService {

    private final PersonDAO personDAO;

    /**
     * Конструктор класса.
     *
     * @param personDAO Объект класса PersonDAO, который предоставляет доступ к данным пользователей.
     */
    public PersonService(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    /**
     *
     * @param email Почта, по которой осуществляется поиск аккаунта пользователя
     * @return Данные о пользователе банка
     */
    public Person authenticate(String email) {
        return personDAO.getPersonByEmail(email);
    }

    // TODO JavaDoc
    public List<Person> getAllPeople() {
        return personDAO.getAllPeople();
    }

    // TODO JavaDoc
    public Person getPersonById(int personId) {
        return personDAO.getPersonById(personId);
    }

    public void createPerson(Person newPerson) {
        personDAO.addPerson(newPerson);
    }

    public void updatePersonById(Person updatedPerson, int personId) {
        personDAO.updatePerson(updatedPerson, personId);
    }

    public void deletePerson(int personId) {
        personDAO.deletePersonById(personId);
    }
}
