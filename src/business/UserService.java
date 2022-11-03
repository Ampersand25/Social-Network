package business;

import domain.User;
import exception.ValidationException;
import exception.RepoException;
import exception.ServiceException;
import validation.IValidator;
import infrastructure.IRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final IValidator<User> validator;
    private final IRepository<Long, User> repo;
    private Long availableId;

    /**
     * Metoda privata de tip void (procedura) care valideaza un obiect de clasa Long (verifica daca acesta este un identificator valid pentru un utilizator (obiect de clasa User))
     * @param id obiect de clasa Long pe care vrem sa il validam
     * @throws ServiceException daca parametrul formal/simbolic de intrare id este null (are valoare nula) sau daca este o valoare intreaga negativa (este mai mic strict decat 0)
     */
    private void validateId(Long id) throws ServiceException {
        if(id == null) {
            throw new ServiceException("[!]Invalid id (id must not be null)!\n");
        }

        if(id < 0L) {
            throw new ServiceException("[!]Invalid id (id must be greater or equal with 0)!\n");
        }
    }

    /**
     * Constructor public al unui obiect de clasa UserService care primeste doi parametri de intrare: validator si repo
     * @param validator obiect de clasa IValidator (interfata de tip template care are User ca si parametru) folosit pentru validarea utilizatorilor (obiectelor de clasa User)
     * @param repo obiect de clasa IRepository (interfata de tip IRepository care are Long si User ca si parametri) folosit pentru stocarea utilizatorilor (obiectelor de clasa User) in memorie (repozitoriu)
     */
    public UserService(IValidator<User> validator, IRepository<Long, User> repo) {
        this.validator = validator;
        this.repo = repo;
        this.availableId = 0L;
    }

    public void add(String firstName, String lastName, LocalDate birthday, String email) throws ValidationException, RepoException, IllegalArgumentException {
        User user = new User(firstName, lastName, birthday, email);
        user.setId(availableId++);

        try{
            validator.validate(user);
        } catch(ValidationException ex) {
            --availableId;
            throw new ValidationException(ex.getMessage());
        }

        repo.add(user);
    }

    public User remove(Long userId) throws RepoException, ServiceException, IllegalArgumentException {
        validateId(userId);

        User removedUser = repo.remove(userId);
        Iterable<User> allUsers = repo.getAll();
        allUsers.forEach(user -> {
            List<User> friendListOfCurrentUser = user.getFriendList();
            List<User> filteredFriendListOfCurrentUser = friendListOfCurrentUser.stream().filter(friendOfUser -> !friendOfUser.equals(removedUser)).collect(Collectors.toList());
            user.setFriendList(filteredFriendListOfCurrentUser);
        });

        return removedUser;
    }

    public User modify(Long userId, String firstName, String lastName) throws ValidationException, RepoException, IllegalArgumentException {
        LocalDate birthday = LocalDate.now();
        String email = "";
        List<User> friendList = new ArrayList<>();
        try {
            User searchedUser = search(userId);
            birthday = searchedUser.getBirthday();
            email = searchedUser.getEmail();
            friendList = searchedUser.getFriendList();
        } catch (ServiceException ignored) {}

        User newUser = new User(userId, firstName, lastName, birthday, email);
        newUser.setFriendList(friendList);
        validator.validate(newUser);

        User modifiedUser = repo.modify(newUser);
        Iterable<User> allUsers = repo.getAll();
        allUsers.forEach(user -> {
            List<User> friendListOfCurrentUser = user.getFriendList();
            List<User> filteredFriendListOfCurrentUser = friendListOfCurrentUser.stream().filter(friendOfUser -> !friendOfUser.equals(modifiedUser)).collect(Collectors.toList());

            if(!friendListOfCurrentUser.equals(filteredFriendListOfCurrentUser)) {
                filteredFriendListOfCurrentUser.add(newUser);
            }

            user.setFriendList(filteredFriendListOfCurrentUser);
        });

        return modifiedUser;
    }

    public User search(Long userId) throws RepoException, ServiceException, IllegalArgumentException {
        validateId(userId);
        return repo.search(userId);
    }

    public int len() {
        return repo.len();
    }

    public Iterable<User> getAll() throws RepoException {
        return repo.getAll();
    }

    public List<User> getFriendsOfUser(Long userId) throws RepoException, ServiceException, IllegalArgumentException {
        validateId(userId);
        return repo.search(userId).getFriendList();
    }
}
