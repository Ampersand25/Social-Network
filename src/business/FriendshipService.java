package business;

import domain.SocialNetworkGraph;
import domain.User;
import domain.Friendship;
import exception.ValidationException;
import exception.RepoException;
import exception.ServiceException;
import validation.IValidator;
import infrastructure.IRepository;

import java.time.LocalDate;
import java.util.List;

public class FriendshipService {
    private final IValidator<Friendship> validator;
    private final IRepository<Long, Friendship> friendshipRepo;
    private final IRepository<Long, User> userRepo;
    private Long availableId;

    private void addFriendToUser(User firstUser, User secondUser) {
        List<User> firstUserFriendList = firstUser.getFriendList();
        firstUserFriendList.add(secondUser);
        firstUser.setFriendList(firstUserFriendList);

        List<User> secondUserFriendList = secondUser.getFriendList();
        secondUserFriendList.add(firstUser);
        secondUser.setFriendList(secondUserFriendList);
    }

    private void deleteFriendFromUser(User firstUser, User secondUser) {
        List<User> firstUserFriendList = firstUser.getFriendList();
        List<User> filteredFirstUserFriendList = firstUserFriendList.stream().filter(user -> !user.equals(secondUser)).toList();
        firstUser.setFriendList(filteredFirstUserFriendList);

        List<User> secondUserFriendList = secondUser.getFriendList();
        List<User> filteredSecondUserFriendList = secondUserFriendList.stream().filter(user -> !user.equals(firstUser)).toList();
        secondUser.setFriendList(filteredSecondUserFriendList);
    }

    private void validateId(Long id) throws ServiceException {
        if(id == null) {
            throw new ServiceException("[!]Invalid id (id must not be null)!\n");
        }

        if(id < 0L) {
            throw new ServiceException("[!]Invalid id (id must be greater or equal with 0)!\n");
        }
    }

    public FriendshipService(IValidator<Friendship> validator, IRepository<Long, Friendship> repo, IRepository<Long, User> userRepo) {
        this.validator = validator;
        this.friendshipRepo = repo;
        this.userRepo = userRepo;
        this.availableId = 0L;
    }

    public void add(Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException, ServiceException {
        if(userRepo.len() == 0) {
            throw new ServiceException("[!]There are no users in the social network!\n");
        }

        validateId(firstFriendId);
        validateId(secondFriendId);

        if(firstFriendId.equals(secondFriendId)) {
            throw new ServiceException("[!]A user cannot befriend himself!\n");
        }

        User firstFriend = userRepo.search(firstFriendId);
        User secondFriend = userRepo.search(secondFriendId);

        Friendship newFriendship;
        if(firstFriendId <= secondFriendId) {
            newFriendship = new Friendship(firstFriend, secondFriend);
        }
        else {
            newFriendship = new Friendship(secondFriend, firstFriend);
        }

        newFriendship.setId(availableId++);
        try{
            validator.validate(newFriendship);
        } catch(ValidationException ex) {
            --availableId;
            throw new ValidationException(ex.getMessage());
        }

        friendshipRepo.add(newFriendship);
        addFriendToUser(firstFriend, secondFriend);
    }

    public Friendship remove(Long friendshipId) throws RepoException, ServiceException {
        validateId(friendshipId);

        Friendship removedFriendship = friendshipRepo.remove(friendshipId);
        deleteFriendFromUser(removedFriendship.getFirstFriend(), removedFriendship.getSecondFriend());

        return removedFriendship;
    }

    public Friendship modify(Long friendshipId, Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException {
        User firstFriend = userRepo.search(firstFriendId);
        User secondFriend = userRepo.search(secondFriendId);

        Friendship friendship;
        if(firstFriendId <= secondFriendId) {
            friendship = new Friendship(friendshipId, firstFriend, secondFriend, LocalDate.now());
        }
        else {
            friendship = new Friendship(friendshipId, secondFriend, firstFriend, LocalDate.now());
        }

        validator.validate(friendship);

        Friendship modifiedFriendship = friendshipRepo.modify(friendship);
        if(!friendship.getFirstFriend().equals(modifiedFriendship.getFirstFriend()) || !friendship.getSecondFriend().equals(modifiedFriendship.getSecondFriend())) {
            deleteFriendFromUser(modifiedFriendship.getFirstFriend(), modifiedFriendship.getSecondFriend());
            addFriendToUser(friendship.getFirstFriend(), friendship.getSecondFriend());
        }

        return modifiedFriendship;
    }

    public Friendship modify(Long friendshipId, Long firstFriendId, Long secondFriendId, LocalDate date) throws ValidationException, RepoException {
        User firstFriend = userRepo.search(firstFriendId);
        User secondFriend = userRepo.search(secondFriendId);

        Friendship friendship;
        if(firstFriendId <= secondFriendId) {
            friendship = new Friendship(friendshipId, firstFriend, secondFriend, date);
        }
        else {
            friendship = new Friendship(friendshipId, secondFriend, firstFriend, date);
        }

        validator.validate(friendship);

        Friendship modifiedFriendship = friendshipRepo.modify(friendship);
        if(!friendship.getFirstFriend().equals(modifiedFriendship.getFirstFriend()) || !friendship.getSecondFriend().equals(modifiedFriendship.getSecondFriend())) {
            deleteFriendFromUser(modifiedFriendship.getFirstFriend(), modifiedFriendship.getSecondFriend());
            addFriendToUser(friendship.getFirstFriend(), friendship.getSecondFriend());
        }

        return modifiedFriendship;
    }

    public Friendship search(Long friendshipId) throws RepoException, ServiceException {
        validateId(friendshipId);
        return friendshipRepo.search(friendshipId);
    }

    public int len() {
        return friendshipRepo.len();
    }

    public Iterable<Friendship> getAll() throws RepoException {
        return friendshipRepo.getAll();
    }

    public int numberOfCommunities() {
        Iterable<User> allUsers;
        try {
            allUsers = userRepo.getAll();
        } catch(RepoException repoException) {
            return 0;
        }

        Iterable<Friendship> allFriendships;
        try {
            allFriendships = friendshipRepo.getAll();
        } catch(RepoException repoException) {
            return userRepo.len();
        }

        SocialNetworkGraph graph = new SocialNetworkGraph(allUsers, allFriendships);
        return graph.numberOfCommunities();
    }

    public List<List<Long>> getAllCommunities() throws RepoException {
        SocialNetworkGraph graph = new SocialNetworkGraph(userRepo.getAll(), friendshipRepo.getAll());
        return graph.getAllCommunities();
    }
}
