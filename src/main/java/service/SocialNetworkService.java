package service;

import exception.FriendshipAlreadyExistsException;
import exception.PersonAlreadyExistsException;
import exception.PersonNotExistException;
import model.Person;

import java.util.List;
import java.util.Set;

public interface SocialNetworkService {
    void addPerson(Person person) throws PersonAlreadyExistsException;
    Set<Person> getPeople();
    void addFriendship(Person p1, Person p2) throws PersonNotExistException, FriendshipAlreadyExistsException;
    Set<Person> getFriends(Person person) throws PersonNotExistException;
    List<Person> getMinimumPath(Person src, Person dst) throws PersonNotExistException;
}