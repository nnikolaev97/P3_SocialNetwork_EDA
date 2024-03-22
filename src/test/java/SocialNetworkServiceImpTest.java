import exception.FriendshipAlreadyExistsException;
import exception.PersonAlreadyExistsException;
import exception.PersonNotExistException;
import model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.SocialNetworkService;
import service.SocialNetworkServiceImp;


import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

class SocialNetworkServiceImpTest {


    static SocialNetworkService socialNetwork = new SocialNetworkServiceImp();
    static Map<Person, Set<Person>> all = new LinkedHashMap<Person, Set<Person>>();

    @BeforeAll
    static void setUp() throws PersonAlreadyExistsException, PersonNotExistException, FriendshipAlreadyExistsException {
        List<Person> persons = new ArrayList<>();

        // Generate 100 Person
        for (int i = 1; i <= 100; i++) {
            String name = "Person " + i;
            Person person = new Person(name);
            persons.add(person);
        }

        int numPersons = persons.size();

        for (Person person : persons) {
            socialNetwork.addPerson(person);
        }
        for (int i = 0; i < numPersons; i++) {
            Person person = persons.get(i);
            Person friend1 = persons.get((i + 1) % numPersons);
            Person friend2 = persons.get((i + 2) % numPersons);

            Set<Person> friends = new HashSet<>();
            friends.add(friend1);
            friends.add(friend2);

            all.put(person, friends);

            socialNetwork.addFriendship(person, persons.get((i + 1) % numPersons));
            socialNetwork.addFriendship(person, persons.get((i + 2) % numPersons));

        }

        for (Person person : all.keySet()) {
            Set<Person> friends = all.get(person);
            for (Person friend : friends) {
                // Add mutual friendships
                all.computeIfAbsent(friend, k -> new HashSet<>()).add(person);
            }
        }
    }



    @Test
    void addPerson() throws PersonAlreadyExistsException {

        PersonAlreadyExistsException exceptionPersonExists = assertThrows(PersonAlreadyExistsException.class, () ->
                socialNetwork.addPerson(new Person("Person 1"))
        );

        Assertions.assertEquals("Person Person 1 already exists", exceptionPersonExists.getMessage(), "Exception message should match");

        socialNetwork.addPerson(new Person("Person 1001"));
        Assertions.assertEquals(101, socialNetwork.getPeople().size(), "Add Person should've added Person 101");
    }

    @Test
    void getPeople() {
        Assertions.assertEquals(all.keySet().size()+1, socialNetwork.getPeople().size());
    }

    @Test
    void addFriendship() throws PersonNotExistException, FriendshipAlreadyExistsException {
        PersonNotExistException exceptionPersonExistsNot = assertThrows(PersonNotExistException.class, () ->
                socialNetwork.addFriendship(new Person("Person 121"), new Person("Person 1"))
        );
        FriendshipAlreadyExistsException exceptionFriendshipExists = assertThrows(FriendshipAlreadyExistsException.class, () ->
                socialNetwork.addFriendship(new Person("Person 1"), new Person("Person 2"))
        );

        Assertions.assertEquals("Person Person 121 does not exist", exceptionPersonExistsNot.getMessage(), "Exception message should match");
        Assertions.assertEquals("The friendship between Person 1 and Person 2 is already exists", exceptionFriendshipExists.getMessage(), "Exception message should match");

        socialNetwork.addFriendship(new Person("Person 63"), new Person("Person 68"));
        Assertions.assertEquals(socialNetwork.getFriends(new Person("Person 63")).size(), socialNetwork.getFriends(new Person("Person 68")).size());
    }

    @Test
    void getFriends() throws PersonNotExistException {
        Assertions.assertEquals(all.get(new Person("Person 2")).size(), socialNetwork.getFriends(new Person("Person 2")).size());
        Assertions.assertEquals(all.get(new Person("Person 23")).size(), socialNetwork.getFriends(new Person("Person 23")).size());
    }

    @Test
    void getMinimumPath() throws PersonNotExistException {
        Assertions.assertEquals(8, socialNetwork.getMinimumPath(new Person("Person 1"), new Person("Person 15")).size());
        Assertions.assertEquals(2, socialNetwork.getMinimumPath(new Person("Person 1"), new Person("Person 100")).size());
        Assertions.assertEquals(12, socialNetwork.getMinimumPath(new Person("Person 23"), new Person("Person 44")).size());
        Assertions.assertEquals(20, socialNetwork.getMinimumPath(new Person("Person 23"), new Person("Person 60")).size());
    }
}