package business;

import domain.Friendship;
import domain.User;
import exception.RepoException;
import exception.ServiceException;
import exception.ValidationException;
import infrastructure.IRepository;
import validation.IValidator;

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

    public Friendship remove(Long id) throws RepoException, ServiceException {
        validateId(id);
        return friendshipRepo.remove(id);
    }

    public Friendship modify(Friendship friendship) throws ValidationException, RepoException {
        validator.validate(friendship);
        return friendshipRepo.modify(friendship);
    }

    public Friendship search(Long id) throws RepoException, ServiceException {
        validateId(id);
        return friendshipRepo.search(id);
    }

    public int len() {
        return friendshipRepo.len();
    }

    public Iterable<Friendship> getAll() throws RepoException {
        return friendshipRepo.getAll();
    }
}
