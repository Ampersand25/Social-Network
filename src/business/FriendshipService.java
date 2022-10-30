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

public class FriendshipService {
    private final IValidator<Friendship> validator;
    private final IRepository<Long, Friendship> friendshipRepo;
    private final IRepository<Long, User> userRepo;
    private Long availableId;

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
        validateId(firstFriendId);
        validateId(secondFriendId);

        if(firstFriendId == secondFriendId) {
            throw new ServiceException("[!]A user cannot befriend himself!\n");
        }

        User firstFriend = userRepo.search(firstFriendId);
        User secondFriend = userRepo.search(secondFriendId);

        Friendship newFriendship = new Friendship(firstFriend, secondFriend);
        newFriendship.setId(availableId++);
        try{
            validator.validate(newFriendship);
        } catch(ValidationException ex) {
            --availableId;
            throw new ValidationException(ex.getMessage());
        }

        friendshipRepo.add(newFriendship);
    }

    public Friendship remove(Long friendshipId) throws RepoException, ServiceException {
        validateId(friendshipId);
        return friendshipRepo.remove(friendshipId);
    }

    public Friendship modify(Long friendshipId, Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException {
        User firstFriend = userRepo.search(firstFriendId);
        User secondFriend = userRepo.search(secondFriendId);

        Friendship friendship = new Friendship(friendshipId, firstFriend, secondFriend, LocalDate.now());
        validator.validate(friendship);

        return friendshipRepo.modify(friendship);
    }

    public Friendship modify(Long friendshipId, Long firstFriendId, Long secondFriendId, LocalDate date) throws ValidationException, RepoException {
        User firstFriend = userRepo.search(firstFriendId);
        User secondFriend = userRepo.search(secondFriendId);

        Friendship friendship = new Friendship(friendshipId, firstFriend, secondFriend, date);
        validator.validate(friendship);

        return friendshipRepo.modify(friendship);
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
}
