package business;

import domain.Friendship;
import domain.User;
import exception.ValidationException;
import exception.RepoException;
import exception.ServiceException;

import java.time.LocalDate;

public class SuperService {
    private final UserService userService;
    private final FriendshipService friendshipService;

    public SuperService(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    public void addUser(String firstName, String lastName) throws ValidationException, RepoException {
        userService.add(firstName, lastName);
    }

    public User removeUser(Long userId) throws RepoException, ServiceException {
        return userService.remove(userId);
    }

    public User modifyUser(Long userId, String firstName, String lastName) throws ValidationException, RepoException {
        return userService.modify(userId, firstName, lastName);
    }

    public User searchUser(Long userId) throws RepoException, ServiceException {
        return userService.search(userId);
    }

    public int numberOfUsers() {
        return userService.len();
    }

    public Iterable<User> getAllUsers() throws RepoException {
        return userService.getAll();
    }

    public void addFriendship(Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException, ServiceException {
        friendshipService.add(firstFriendId, secondFriendId);
    }

    public Friendship removeFriendship(Long friendshipId) throws RepoException, ServiceException {
        return friendshipService.remove(friendshipId);
    }

    public Friendship modifyFriendship(Long friendshipId, Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException {
        return friendshipService.modify(friendshipId, firstFriendId, secondFriendId);
    }

    public Friendship modifyFriendship(Long friendshipId, Long firstFriendId, Long secondFriendId, LocalDate date) throws ValidationException, RepoException {
        return friendshipService.modify(friendshipId, firstFriendId, secondFriendId, date);
    }

    public Friendship searchFriendship(Long friendshipId) throws RepoException, ServiceException {
        return friendshipService.search(friendshipId);
    }

    public int numberOfFriendships() {
        return friendshipService.len();
    }

    public Iterable<Friendship> getAllFriendships() throws RepoException {
        return friendshipService.getAll();
    }

    public int numberOfCommunities() {
        return friendshipService.numberOfCommunities();
    }

    public Iterable<User> getMostSociableCommunity() throws ServiceException {
        return friendshipService.getMostSociableCommunity();
    }
}
