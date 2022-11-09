package business;

import domain.SocialNetworkGraph;
import domain.User;
import domain.Friendship;
import exception.ValidationException;
import exception.RepoException;
import exception.ServiceException;
import validation.IValidator;
import infrastructure.IRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

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
    private void addFriendToUser(@NotNull User firstUser, @NotNull User secondUser) {
        List<User> firstUserFriendList = firstUser.getFriendList();
        List<User> firstUserFriendListUpdated = new ArrayList<>(firstUserFriendList);
        firstUserFriendListUpdated.add(secondUser);
        firstUser.setFriendList(firstUserFriendListUpdated);

        List<User> secondUserFriendList = secondUser.getFriendList();
        List<User> secondUserFriendListUpdated = new ArrayList<>(secondUserFriendList);
        secondUserFriendListUpdated.add(firstUser);
        secondUser.setFriendList(secondUserFriendListUpdated);
    }

    /**
     * Metoda privata de tip void (functie procedurala) care sterge bidirectional (in ambele sensuri) un user/utilizator (obiect de clasa User) din lista de prieteni ai unui alt user/utilizator (obiect de clasa User)<br>
     * Metoda face doua stergeri/eliminari ci anume:<br>
     * - sterge/elimina utilizatorul (obiect de clasa User) secondFriend din lista de prieteni (atributul/campul privat friendList ce reprezinta o lista de obiecte de tipul User) ai utilizatorului (obiect de clasa User) firstFriend<br>
     * - sterge/elimina utilizatorul (obiect de clasa User) firstFriend din lista de prieteni (atributul/campul privat friendList ce reprezinta o lista de obiecte de tipul User) ai utilizatorului (obiect de clasa User) secondFriend
     * @param firstUser obiect de clasa User (utilizator valid din reteaua de socializare) reprezentand primul prieten din relatia de prietenie
     * @param secondUser obiect de clasa User (utilizator valid din reteaua de socializare) reprezentand al doilea prieten din relatia de prietenie
     */
    private void deleteFriendFromUser(@NotNull User firstUser, @NotNull User secondUser) {
        List<User> firstUserFriendList = firstUser.getFriendList();
        List<User> filteredFirstUserFriendList = new ArrayList<>();
        filteredFirstUserFriendList = firstUserFriendList.stream().filter(user -> !user.equals(secondUser)).toList();
        firstUser.setFriendList(filteredFirstUserFriendList);

        List<User> secondUserFriendList = secondUser.getFriendList();
        List<User> filteredSecondUserFriendList = new ArrayList<>();
        filteredSecondUserFriendList = secondUserFriendList.stream().filter(user -> !user.equals(firstUser)).toList();
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
    public FriendshipService(IValidator<Friendship> validator, @NotNull IRepository<Long, Friendship> repo, IRepository<Long, User> userRepo) {
        this.validator = validator;
        this.friendshipRepo = repo;
        this.userRepo = userRepo;
        availableId = 0L;
        try {
            Iterable<Friendship> friendships = repo.getAll();
            for(Friendship friendship : friendships) {
                availableId = Math.max(availableId, friendship.getId());
            }
            ++availableId;
        } catch(RepoException ignored) {}
    }

    /**
     * Metoda publica de tip void (functie procedurala) care adauga o relatie de prietenie (obiect de clasa Friendship) in reteaua de socializare
     * @param firstFriendId obiect de clasa Long ce reprezinta identificatorul unic (id-ul) primului prieten
     * @param secondFriendId obiect de clasa Long ce reprezinta identificatorul unic (id-ul) celui de al doilea prieten
     * @throws ValidationException daca relatia de prietenie creata nu este valida (este invalida)
     * @throws RepoException daca exista deja o relatie de prietenie intre cei doi prieteni cu id-urile firstFriendId respectiv secondFriendId
     * @throws ServiceException daca cel putin unul dintre cele doua id-uri nu este valid (firstFriendId sau secondFriendId este invalid; cele doua id-uri trebuie sa fie diferite de null si sa fie valori numerice intreagi mai mari sau egale cu 0)
     * @throws IllegalArgumentException daca cel putin unul dintre cele doua id-uri nu este valid (firstFriendId sau secondFriendId este invalid; cele doua id-uri trebuie sa fie diferite de null si sa fie valori numerice intreagi mai mari sau egale cu 0)
     */
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

    /**
     * Metoda publica de tip operand/rezultat care sterge/elimina o relatie de prietenie (obiect de clasa Friendship) din reteaua de socializare
     * @param friendshipId obiect de clasa Long ce reprezinta id-ul (identificatorul unic) prieteniei pe care dorim sa o stergem/eliminam
     * @return obiect de clasa Friendship (prietenie valida din reteaua de socializare) ce reprezinta prietenia stearsa/eliminata
     * @throws RepoException daca nu exista prietenii in retea sau prietenia cu id-ul egal cu friendshipId nu exista (nu exista nicio prietenie care sa aiba id-ul egal cu friendshipId)
     * @throws ServiceException daca parametrul formal/simbolic de intrare friendshipId este null sau reprezinta o valoare numerica strict negativa (mai mica strict decat 0)
     * @throws IllegalArgumentException daca parametrul formal/simbolic de intrare friendshipId este null sau reprezinta o valoare numerica strict negativa (mai mica strict decat 0)
     */
    public Friendship remove(Long friendshipId) throws RepoException, ServiceException, IllegalArgumentException {
        validateId(friendshipId);

        Friendship removedFriendship = friendshipRepo.remove(friendshipId);
        deleteFriendFromUser(removedFriendship.getFirstFriend(), removedFriendship.getSecondFriend());

        return removedFriendship;
    }

    /**
     * Metoda publica de tip operand/rezultat care modifica o prietenie (obiect de clasa Friendship) existenta in retea
     * @param friendshipId obiect de clasa Long (valoare numerica intreaga cu semn (signed)) ce reprezinta identificatorul unic al prieteniei (al obiectului de clasa Friendship)
     * @param firstFriendId obiect ce clasa Long (valoare numerica intreaga cu semn (signed)) ce reprezinta noul prim prieten al relatiei de prietenie
     * @param secondFriendId obiect ce clasa Long (valoare numerica intreaga cu semn (signed)) ce reprezinta noul cel de al doilea prieten al relatiei de prietenie
     * @return obiect de clasa Friendship ce reprezinta prietenia (obiectul de clasa Friendship) modificata din retea
     * @throws ValidationException daca noua prietenie creata nu este valida (id-ul primului prieten sau al celui de al doilea nu este valid)
     * @throws RepoException daca nu exista prietenii in retea sau nu exista nicio relatie de prieteni care sa aiba id-ul egal cu id-ul noi relatii de prietenie (parametrul friendshipId)
     * @throws IllegalArgumentException daca friendshipId, firstFriendId sau secondFriendId sunt invalide (sunt egale cu null sau sunt valori numerice strict negative)
     */
    public Friendship modify(Long friendshipId, Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException, IllegalArgumentException {
        User firstFriend = userRepo.search(firstFriendId);
        User secondFriend = userRepo.search(secondFriendId);

        Friendship friendship;
        if(firstFriendId <= secondFriendId) {
            friendship = new Friendship(friendshipId, firstFriend, secondFriend, LocalDateTime.now());
        }
        else {
            friendship = new Friendship(friendshipId, secondFriend, firstFriend, LocalDateTime.now());
        }

        validator.validate(friendship);

        Friendship modifiedFriendship = friendshipRepo.modify(friendship);
        if(!friendship.getFirstFriend().equals(modifiedFriendship.getFirstFriend()) || !friendship.getSecondFriend().equals(modifiedFriendship.getSecondFriend())) {
            deleteFriendFromUser(modifiedFriendship.getFirstFriend(), modifiedFriendship.getSecondFriend());
            addFriendToUser(friendship.getFirstFriend(), friendship.getSecondFriend());
        }

        return modifiedFriendship;
    }

    /**
     * Metoda publica de tip operand/rezultat care modifica o prietenie (obiect de clasa Friendship) existenta in retea
     * @param friendshipId obiect de clasa Long (valoare numerica intreaga cu semn (signed)) ce reprezinta identificatorul unic al prieteniei (al obiectului de clasa Friendship)
     * @param firstFriendId obiect ce clasa Long (valoare numerica intreaga cu semn (signed)) ce reprezinta noul prim prieten al relatiei de prietenie
     * @param secondFriendId obiect ce clasa Long (valoare numerica intreaga cu semn (signed)) ce reprezinta noul cel de al doilea prieten al relatiei de prietenie
     * @param friendsFrom obiect de clasa LocalDateTime ce reprezinta noua data calendaristica (zi, luna, an, ora, minut, secunda) la care relatia de prietenie s-a legat intre cei doi prieteni: cel cu id-ul firstFriendId si cel cu id-ul secondFriendId
     * @return obiect de clasa Friendship ce reprezinta prietenia (obiectul de clasa Friendship) modificata din retea
     * @throws ValidationException daca noua prietenie creata nu este valida (id-ul primului prieten sau al celui de al doilea nu este valid)
     * @throws RepoException daca nu exista prietenii in retea sau nu exista nicio relatie de prieteni care sa aiba id-ul egal cu id-ul noi relatii de prietenie (parametrul friendshipId)
     * @throws IllegalArgumentException daca friendshipId, firstFriendId sau secondFriendId sunt invalide (sunt egale cu null sau sunt valori numerice strict negative) sau daca parametrul friendsFrom este invalid (este null, nu reprezinta un format corect de data si ora, reprezinta o data calendaristica din viitor sau reprezinta o data calendaristica mult prea veche (mai veche de 120 de ani))
     */
    public Friendship modify(Long friendshipId, Long firstFriendId, Long secondFriendId, LocalDateTime friendsFrom) throws ValidationException, RepoException, IllegalArgumentException {
        User firstFriend = userRepo.search(firstFriendId);
        User secondFriend = userRepo.search(secondFriendId);

        Friendship friendship;
        if(firstFriendId <= secondFriendId) {
            friendship = new Friendship(friendshipId, firstFriend, secondFriend, friendsFrom);
        }
        else {
            friendship = new Friendship(friendshipId, secondFriend, firstFriend, friendsFrom);
        }

        validator.validate(friendship);

        Friendship modifiedFriendship = friendshipRepo.modify(friendship);
        if(!friendship.getFirstFriend().equals(modifiedFriendship.getFirstFriend()) || !friendship.getSecondFriend().equals(modifiedFriendship.getSecondFriend())) {
            deleteFriendFromUser(modifiedFriendship.getFirstFriend(), modifiedFriendship.getSecondFriend());
            addFriendToUser(friendship.getFirstFriend(), friendship.getSecondFriend());
        }

        return modifiedFriendship;
    }

    /**
     * Metoda publica de tip operand/rezultat care cauta un prieten (obiect de clasa Friendship) in reteaua de socializare
     * @param friendshipId obiect de clasa Long ce reprezinta identificatorul unic al utilizatorului cautat
     * @return obiect de clasa Friendship (entitatea prietenie) ce reprezinta prietenia cu id-ul egal cu friendshipId din retea
     * @throws RepoException daca nu exista prietenii (obiecte de clasa Friendship) in retea sau daca prietenia cautata nu exista (nu exista nicio prietenie cu id-ul egal cu friendshipId)
     * @throws ServiceException daca parametrul formal/simbolic de intrare friendshipId este invalid (este null sau este o valoare numerica intreaga mai mica strict decat 0)
     * @throws IllegalArgumentException daca parametrul formal/simbolic de intrare friendshipId este invalid (este null sau este o valoare numerica intreaga mai mica strict decat 0)
     */
    public Friendship search(Long friendshipId) throws RepoException, ServiceException, IllegalArgumentException {
        validateId(friendshipId);
        return friendshipRepo.search(friendshipId);
    }

    /**
     * Metoda publica de tip operand/rezultat care returneaza/intoarce numarul total de prietenii din relatia de prietenie
     * @return valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) ce reprezinta numarul de prietenii din retea
     */
    public int len() {
        return friendshipRepo.len();
    }

    /**
     * Metoda publica de tip operand/rezultat care returneaza o lista cu toate prieteniile (obiecte de clasa Friendship) din reteaua de socializare
     * @return obiect de clasa Iterable (obiect iterabil care poate sa fie parcurs/iterat) ce reprezinta lista de prietenii din retea
     * @throws RepoException daca nu exista nicio prietenie in reteaua de socializare
     */
    public Iterable<Friendship> getAll() throws RepoException {
        return friendshipRepo.getAll();
    }

    /**
     * Metoda publica de tip int (integer = intreg) care returneaza numarul de comunitati din reteaua de socializare (adica numarul de componente conexe din grafului retelei)
     * @return valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) reprezentand numarul de comunitati din retea
     */
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

    /**
     * Metoda publica de tip operand (rezultat) care returneaza/intoarce o lista cu toate comunitatile din reteaua de socializare (o comunitate reprezinta o componenta conexa din graful retelei)
     * @return lista de elemente de tipul lista cu elemente numere intregi de tip long (obiecte de clasa Long) ce reprezinta lista tuturor comunitatilor din reteaua de socializare
     * @throws RepoException daca nu exista utilizatori (obiecte de clasa User) in reteaua de socializare
     */
    public List<List<Long>> getAllCommunities() throws RepoException {
        SocialNetworkGraph graph = new SocialNetworkGraph(userRepo.getAll(), friendshipRepo.getAll());
        return graph.getAllCommunities();
    }
}
