package business;

import domain.User;
import exception.RepoException;
import exception.ServiceException;
import exception.ValidationException;
import infrastructure.IRepository;
import validation.IValidator;

public class UserService {
    private final IValidator<User> validator;
    private final IRepository<Long, User> repo;
    private Long availableId;

    private void validateId(Long id) throws ServiceException {
        if(id == null) {
            throw new ServiceException("[!]Invalid id (id must not be null)!\n");
        }
        if(id < 0) {
            throw new ServiceException("[!]Invalid id (id must be greater or equal with 0)!\n");
        }
    }

    public UserService(IValidator<User> validator, IRepository<Long, User> repo) {
        this.validator = validator;
        this.repo = repo;
        this.availableId = 0L;
    }

    public void add(User user) throws ValidationException, RepoException {
        user.setId(availableId++);
        try{
            validator.validate(user);
        } catch(ValidationException ex) {
            --availableId;
            throw new ValidationException(ex.getMessage());
        }
        repo.add(user);
    }

    public User remove(Long id) throws RepoException, ServiceException {
        validateId(id);
        return repo.remove(id);
    }

    public User modify(User user) throws ValidationException, RepoException {
        validator.validate(user);
        return repo.modify(user);
    }

    public User search(Long id) throws RepoException, ServiceException {
        validateId(id);
        return repo.search(id);
    }

    public int len() {
        return repo.len();
    }

    public Iterable<User> getAll() throws RepoException {
        return repo.getAll();
    }
}
