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

    /**
     * Metoda privata de tip void (functie procedurala) care adauga bidirectional (in ambele sensuri) un user/utilizator (obiect de clasa User) in lista de prieteni ai unui alt user/utilizator (obiect de clasa User)<br>
     * Metoda face doua adaugari ci anume:<br>
     * - adauga utilizatorul (obiect de clasa User) secondFriend in lista de prieteni (atributul/campul privat friendList ce reprezinta o lista de obiecte de tipul User) ai utilizatorului (obiect de clasa User) firstFriend<br>
     * - adauga utilizatorul (obiect de clasa User) firstFriend in lista de prieteni (atributul/campul privat friendList ce reprezinta o lista de obiecte de tipul User) ai utilizatorului (obiect de clasa User) secondFriend
     * @param firstUser obiect de clasa User (utilizator valid din reteaua de socializare) reprezentand primul prieten din relatia de prietenie
     * @param secondUser obiect de clasa User (utilizator valid din reteaua de socializare) reprezentand al doilea prieten din relatia de prietenie
     */
    private void addFriendToUser(User firstUser, User secondUser) {
        List<User> firstUserFriendList = firstUser.getFriendList();
        firstUserFriendList.add(secondUser);
        firstUser.setFriendList(firstUserFriendList);

        List<User> secondUserFriendList = secondUser.getFriendList();
        secondUserFriendList.add(firstUser);
        secondUser.setFriendList(secondUserFriendList);
    }

    /**
     * Metoda privata de tip void (functie procedurala) care sterge bidirectional (in ambele sensuri) un user/utilizator (obiect de clasa User) din lista de prieteni ai unui alt user/utilizator (obiect de clasa User)<br>
     * Metoda face doua stergeri/eliminari ci anume:<br>
     * - sterge/elimina utilizatorul (obiect de clasa User) secondFriend din lista de prieteni (atributul/campul privat friendList ce reprezinta o lista de obiecte de tipul User) ai utilizatorului (obiect de clasa User) firstFriend<br>
     * - sterge/elimina utilizatorul (obiect de clasa User) firstFriend din lista de prieteni (atributul/campul privat friendList ce reprezinta o lista de obiecte de tipul User) ai utilizatorului (obiect de clasa User) secondFriend
     * @param firstUser obiect de clasa User (utilizator valid din reteaua de socializare) reprezentand primul prieten din relatia de prietenie
     * @param secondUser obiect de clasa User (utilizator valid din reteaua de socializare) reprezentand al doilea prieten din relatia de prietenie
     */
    private void deleteFriendFromUser(User firstUser, User secondUser) {
        List<User> firstUserFriendList = firstUser.getFriendList();
        List<User> filteredFirstUserFriendList = firstUserFriendList.stream().filter(user -> !user.equals(secondUser)).toList();
        firstUser.setFriendList(filteredFirstUserFriendList);

        List<User> secondUserFriendList = secondUser.getFriendList();
        List<User> filteredSecondUserFriendList = secondUserFriendList.stream().filter(user -> !user.equals(firstUser)).toList();
        secondUser.setFriendList(filteredSecondUserFriendList);
    }

    /**
     * Metoda privata de tip void (procedura) folosita pentru validarea id-ului (identificator unic intreg) unei posibile relatii de prietenie (obiect de clasa Friendship) din reteaua de socializare
     * @param id obiect de clasa Long reprezentand identificatorul pe care dorim sa il validam
     * @throws ServiceException daca parametrul formal/simbolic de intrare id nu poate sa fie id-ul unei prietenii din relatia de prietenie (este fie null, fie reprezinta o valoare numerica intreaga negativa (este mai mic strict decat 0))
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
     * Constructorul public al unui obiect de clasa FriendshipService care primeste trei parametri de intrare (parametri formali/simbolici): validator, repo si userRepo
     * @param validator obiect de clasa IValidator (interfaca de tip template care are Friendship ca si parametru) folosit pentru validarea relatiilor de prietenie (obiectelor de clasa Friendship)
     * @param repo obiect de clasa IRepository (interfata de tip template care are Long si Friendship ca si parametri) folosit pentru stocarea relatiilor de prietenie (obiectelor de clasa Friendship) in memorie (repozitoriu)
     * @param userRepo obiect de clasa IRepository (interfata de tip template care are Long si User ca si parametri) folosit pentru stocarea utilizatorilor (obiectelor de clasa User) in memorie (repozitoriu)
     */
    public FriendshipService(IValidator<Friendship> validator, IRepository<Long, Friendship> repo, IRepository<Long, User> userRepo) {
        this.validator = validator;
        this.friendshipRepo = repo;
        this.userRepo = userRepo;
        this.availableId = 0L;
    }

    public void add(Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException, ServiceException, IllegalArgumentException {
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

    public Friendship remove(Long friendshipId) throws RepoException, ServiceException, IllegalArgumentException {
        validateId(friendshipId);

        Friendship removedFriendship = friendshipRepo.remove(friendshipId);
        deleteFriendFromUser(removedFriendship.getFirstFriend(), removedFriendship.getSecondFriend());

        return removedFriendship;
    }

    public Friendship modify(Long friendshipId, Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException, IllegalArgumentException {
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

    public Friendship modify(Long friendshipId, Long firstFriendId, Long secondFriendId, LocalDate date) throws ValidationException, RepoException, IllegalArgumentException {
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

    public Friendship search(Long friendshipId) throws RepoException, ServiceException, IllegalArgumentException {
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
