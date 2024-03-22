package service;

import exception.FriendshipAlreadyExistsException;
import exception.PersonAlreadyExistsException;
import exception.PersonNotExistException;
import exception.PersonSubclassNotExistException;
import model.Person;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

public class SocialNetworkServiceImp implements SocialNetworkService {

    private Map<Person, Set<Person>> friendships = new LinkedHashMap<>();

    @Override
    public void addPerson(@NotNull Person person) throws PersonAlreadyExistsException {
        //TODO: Logic: Person has free or premium plan?
        if (friendships.containsKey(person)){
            throw new PersonAlreadyExistsException(person);
        }
        friendships.put(person, new LinkedHashSet<>());

    }

    @Override
    public Set<Person> getPeople() {
        return Collections.unmodifiableSet(friendships.keySet());
    }

    @Override
    public void addFriendship(@NotNull Person p1, @NotNull Person p2)
            throws PersonNotExistException, FriendshipAlreadyExistsException{

        checkPersonExists(p1);
        checkPersonExists(p2);
        var friends1 = friendships.get(p1);
        var friends2 = friendships.get(p2);
        if(friends1.contains(p2)){
            throw new FriendshipAlreadyExistsException(p1,p2);
        }

        friends1.add(p2);
        friends2.add(p1);

    }

    @Override
    public Set<Person> getFriends(@NotNull Person person) throws PersonNotExistException {
        checkPersonExists(person);
        return Collections.unmodifiableSet(friendships.get(person));
    }

    private Set<Person> friends(@NotNull Person person) {
        return friendships.get(person);
    }

    private void checkPersonExists(Person person) throws PersonNotExistException {
        if(!friendships.containsKey(person)){
            throw new PersonNotExistException(person);
        }
    }

    @Override
    public List<Person> getMinimumPath(Person src, Person dst) throws PersonNotExistException {
        checkPersonExists(src);
        checkPersonExists(dst);

        Person currentPerson = src;

        Set<Person> visitedPeople = new HashSet<>();
        Map<Person, Person> predecesor = new HashMap<>();
        Queue<Person> bsfQueue = new LinkedList();

        bsfQueue.add(currentPerson);
        visitedPeople.add(currentPerson);

        boolean found = false;

        while (!bsfQueue.isEmpty()) {
            currentPerson = bsfQueue.poll();
            if (currentPerson.equals(dst)) {
                found = true;
                break;
            } else {
                for (Person friend : friends(currentPerson)) {
                    if (!visitedPeople.contains(friend)) {
                        bsfQueue.add(friend);
                        visitedPeople.add(friend);
                        predecesor.put(friend, currentPerson);
                    }
                }
            }
        }

        return buildPath(dst, predecesor, found);

    }

    private List<Person> buildPath(Person dst, Map<Person, Person> predecesor, boolean found) {
        List<Person> path = new ArrayList<>();
        if(found){
            for (Person person = dst; person != null; person = predecesor.get(person)) {
                path.add(person);
            }

            Collections.reverse(path);
        }

        return path;
    }
}
