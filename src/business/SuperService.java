package business;

import domain.User;
import domain.Friendship;
import exception.ValidationException;
import exception.RepoException;
import exception.ServiceException;

import java.time.LocalDate;
import java.util.List;

public class SuperService {
    private final UserService userService;
    private final FriendshipService friendshipService;

    /**
     * Constructor public al unui obiect de clasa SuperService
     * @param userService obiect de clasa UserService (service-ul de utilizatori din reteaua de socializare)
     * @param friendshipService obiect de clasa FriendshipService (service-ul de prietenii din reteaua de socializare)
     */
    public SuperService(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    /**
     *
     * @param firstName
     * @param lastName
     * @param birthday
     * @param email
     * @throws ValidationException
     * @throws RepoException
     * @throws IllegalArgumentException
     */
    public void addUser(String firstName, String lastName, LocalDate birthday, String email) throws ValidationException, RepoException, IllegalArgumentException {
        userService.add(firstName, lastName, birthday, email);
    }

    /**
     *
     * @param userId
     * @return
     * @throws RepoException
     * @throws ServiceException
     * @throws IllegalArgumentException
     */
    public User removeUser(Long userId) throws RepoException, ServiceException, IllegalArgumentException {
        return userService.remove(userId);
    }

    /**
     *
     * @param userId
     * @param firstName
     * @param lastName
     * @return
     * @throws ValidationException
     * @throws RepoException
     * @throws IllegalArgumentException
     */
    public User modifyUser(Long userId, String firstName, String lastName) throws ValidationException, RepoException, IllegalArgumentException {
        return userService.modify(userId, firstName, lastName);
    }

    /**
     *
     * @param userId
     * @return
     * @throws RepoException
     * @throws ServiceException
     * @throws IllegalArgumentException
     */
    public User searchUser(Long userId) throws RepoException, ServiceException, IllegalArgumentException {
        return userService.search(userId);
    }

    /**
     *
     * @return
     */
    public int numberOfUsers() {
        return userService.len();
    }

    /**
     *
     * @return
     * @throws RepoException
     */
    public Iterable<User> getAllUsers() throws RepoException {
        return userService.getAll();
    }

    /**
     *
     * @param userId
     * @return
     * @throws RepoException
     * @throws ServiceException
     * @throws IllegalArgumentException
     */
    public List<User> getFriendsOfUser(Long userId) throws RepoException, ServiceException, IllegalArgumentException {
        return userService.getFriendsOfUser(userId);
    }

    /**
     *
     * @param firstFriendId
     * @param secondFriendId
     * @throws ValidationException
     * @throws RepoException
     * @throws ServiceException
     * @throws IllegalArgumentException
     */
    public void addFriendship(Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException, ServiceException, IllegalArgumentException {
        friendshipService.add(firstFriendId, secondFriendId);
    }

    /**
     *
     * @param friendshipId
     * @return
     * @throws RepoException
     * @throws ServiceException
     * @throws IllegalArgumentException
     */
    public Friendship removeFriendship(Long friendshipId) throws RepoException, ServiceException, IllegalArgumentException {
        return friendshipService.remove(friendshipId);
    }

    /**
     *
     * @param friendshipId
     * @param firstFriendId
     * @param secondFriendId
     * @return
     * @throws ValidationException
     * @throws RepoException
     * @throws IllegalArgumentException
     */
    public Friendship modifyFriendship(Long friendshipId, Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException, IllegalArgumentException {
        return friendshipService.modify(friendshipId, firstFriendId, secondFriendId);
    }

    /**
     *
     * @param friendshipId
     * @param firstFriendId
     * @param secondFriendId
     * @param date
     * @return
     * @throws ValidationException
     * @throws RepoException
     * @throws IllegalArgumentException
     */
    public Friendship modifyFriendship(Long friendshipId, Long firstFriendId, Long secondFriendId, LocalDate date) throws ValidationException, RepoException, IllegalArgumentException {
        return friendshipService.modify(friendshipId, firstFriendId, secondFriendId, date);
    }

    /**
     *
     * @param friendshipId
     * @return
     * @throws RepoException
     * @throws ServiceException
     * @throws IllegalArgumentException
     */
    public Friendship searchFriendship(Long friendshipId) throws RepoException, ServiceException, IllegalArgumentException {
        return friendshipService.search(friendshipId);
    }

    /**
     *
     * @return
     */
    public int numberOfFriendships() {
        return friendshipService.len();
    }

    /**
     *
     * @return
     * @throws RepoException
     */
    public Iterable<Friendship> getAllFriendships() throws RepoException {
        return friendshipService.getAll();
    }

    /**
     * Metoda publica de tip int (integer = intreg) care returneaza numarul de comunitati din reteaua de socializare (adica numarul de componente conexe din grafului retelei)
     * @return valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) reprezentand numarul de comunitati din retea
     */
    public int numberOfCommunities() {
        return friendshipService.numberOfCommunities();
    }

    /**
     * Metoda publica de tip operand (rezultat) care returneaza/intoarce o lista cu toate comunitatile din reteaua de socializare (o comunitate reprezinta o componenta conexa din graful retelei)
     * @return lista de elemente de tipul lista cu elemente numere intregi de tip long (obiecte de clasa Long) ce reprezinta lista tuturor comunitatilor din reteaua de socializare
     * @throws RepoException daca nu exista utilizatori (obiecte de clasa User) in reteaua de socializare
     */
    public List<List<Long>> getAllCommunities() throws RepoException {
        return friendshipService.getAllCommunities();
    }
}
