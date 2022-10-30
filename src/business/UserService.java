package business;

import domain.User;
import exception.RepoException;
import exception.ServiceException;
import exception.ValidationException;
import validation.IValidator;
import infrastructure.IRepository;

public class UserService {
    private final IValidator<User> validator;
    private final IRepository<Long, User> repo;
    private Long availableId;

    private void validateId(Long id) throws ServiceException {
        if(id == null) {
            throw new ServiceException("[!]Invalid id (id must not be null)!\n");
        }
        if(id < 0L) {
            throw new ServiceException("[!]Invalid id (id must be greater or equal with 0)!\n");
        }
    }

    public UserService(IValidator<User> validator, IRepository<Long, User> repo) {
        this.validator = validator;
        this.repo = repo;
        this.availableId = 0L;
    }

    public void add(String firstName, String lastName) throws ValidationException, RepoException {
        User user = new User(firstName, lastName);
        user.setId(availableId++);
        try{
            validator.validate(user);
        } catch(ValidationException ex) {
            --availableId;
            throw new ValidationException(ex.getMessage());
        }
        repo.add(user);
    }

    public User remove(Long userId) throws RepoException, ServiceException {
        validateId(userId);
        return repo.remove(userId);
    }

    public User modify(Long userId, String firstName, String lastName) throws ValidationException, RepoException {
        User user = new User(userId, firstName, lastName);
        validator.validate(user);
        return repo.modify(user);
    }

    public User search(Long userId) throws RepoException, ServiceException {
        validateId(userId);
        return repo.search(userId);
    }

    public int len() {
        return repo.len();
    }

    public Iterable<User> getAll() throws RepoException {
        return repo.getAll();
    }
}
