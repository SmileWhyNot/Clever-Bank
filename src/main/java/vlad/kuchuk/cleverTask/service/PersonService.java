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
     * Аутентифицирует пользователя по почте.
     *
     * @param email Почта, по которой осуществляется поиск аккаунта пользователя
     * @return Данные о пользователе банка
     */
    public Person authenticate(String email) {
        return personDAO.getPersonByEmail(email);
    }

    /**
     * Получает список всех людей из базы данных.
     *
     * @return Список объектов Person, представляющих всех людей.
     */
    public List<Person> getAllPeople() {
        return personDAO.getAllPeople();
    }

    /**
     * Получает информацию о человеке по его идентификатору.
     *
     * @param personId Идентификатор человека.
     * @return Объект Person, представляющий информацию о человеке, или null, если человек с указанным идентификатором не найден.
     */
    public Person getPersonById(int personId) {
        return personDAO.getPersonById(personId);
    }

    /**
     * Создает нового человека в базе данных.
     *
     * @param newPerson Объект Person, представляющий нового человека.
     */
    public void createPerson(Person newPerson) {
        personDAO.addPerson(newPerson);
    }

    /**
     * Обновляет информацию о человеке по его идентификатору.
     *
     * @param updatedPerson Объект Person, представляющий обновленную информацию о человеке.
     * @param personId     Идентификатор человека, информацию о котором нужно обновить.
     */
    public void updatePersonById(Person updatedPerson, int personId) {
        personDAO.updatePerson(updatedPerson, personId);
    }

    /**
     * Удаляет человека из базы данных по его идентификатору.
     *
     * @param personId Идентификатор человека, которого нужно удалить.
     */
    public void deletePerson(int personId) {
        personDAO.deletePersonById(personId);
    }
}
