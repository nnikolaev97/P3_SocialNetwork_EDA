import exception.FriendshipAlreadyExistsException;
import exception.PersonAlreadyExistsException;
import exception.PersonNotExistException;
import model.Person;
import service.SocialNetworkService;
import service.SocialNetworkServiceImp;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws PersonNotExistException {

        SocialNetworkService socialNetwork = new SocialNetworkServiceImp();
        List<Person> people = List.of(
                new Person("0"),
                new Person("1"),
                new Person("2"),
                new Person("3"),
                new Person("4"),
                new Person("5"),
                new Person("6"),
                new Person("7"));

        var test = people.contains(new Person("0"));

        for (Person person:people) {
            try {
                socialNetwork.addPerson(person);
            } catch (PersonAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
        }
        var allPeople = socialNetwork.getPeople();

        System.out.println(allPeople);

        try{
            socialNetwork.addFriendship(people.get(0), people.get(1));
            socialNetwork.addFriendship(people.get(0), people.get(2));
            socialNetwork.addFriendship(people.get(7), people.get(4));
            socialNetwork.addFriendship(people.get(5), people.get(2));
            socialNetwork.addFriendship(people.get(2), people.get(4));
            socialNetwork.addFriendship(people.get(4), people.get(6));
            socialNetwork.addFriendship(people.get(3), people.get(0));
            socialNetwork.addFriendship(people.get(1), people.get(2));
            socialNetwork.addFriendship(people.get(4), people.get(0));

        }catch (FriendshipAlreadyExistsException | PersonNotExistException e){
            System.out.println(e.getMessage());
        }
        allPeople.forEach(person ->
        {
            try {
                System.out.println(person.getName() + " Friends " + ": " +socialNetwork.getFriends(person));
            } catch (PersonNotExistException e) {
                System.out.println(e.getMessage());
            }
        });

        System.out.println(socialNetwork.getMinimumPath(people.get(0), people.get(6)));

    }
}