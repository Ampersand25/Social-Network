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
     * Metoda publica de tip void (procedurala) care adauga un utilizator (obiect de clasa User) in reteaua de socializare<br>
     * Metoda construieste un obiect de clasa User (un utilizator), il valideaza folosind un validator si il adauga in lista de useri/utilizatori din repozitoriu
     * @param firstName obiect de clasa String (sir de caractere) ce reprezinta prenumele utilizatorului pe care dorim sa il adaugam in retea
     * @param lastName obiect de clasa String (sir de caractere) ce reprezinta numele de familie al utilizatorului pe care dorim sa il adaugam in retea
     * @param birthday obiect de clasa LocalDate (data calendaristica) ce reprezinta data nasterii utilizatorului pe care dorim sa il adaugam in retea
     * @param email obiect de clasa String (sir de caractere) ce reprezinta adresa de email a utilizatorului pe care dorim sa il adaugam in retea
     * @throws ValidationException daca utilizatorul creat nu este valid (are cel putin un atribut/camp invalid)
     * @throws RepoException daca utilizatorul exista deja in reteaua de socializare (exista un obiect de clasa User care are acelasi id)
     * @throws IllegalArgumentException daca identificatorul unic al utilizatorului nu este valid (este null sau este mai mic strict decat 0)
     */
    public void addUser(String firstName, String lastName, LocalDate birthday, String email) throws ValidationException, RepoException, IllegalArgumentException {
        userService.add(firstName, lastName, birthday, email);
    }

    /**
     * Metoda publica de tip operand/rezultat care sterge/elimina un utilizator (obiect de clasa User) din reteaua de socializare
     * @param userId identificatorul unic al utilizatorului pe care dorim sa il stergem/eliminam din retea
     * @return obiect de clasa User ce reprezinta utilizatorul cu id-ul userId pe care l-am sters din retea (intoarce utilizatorul sters in cazul in care stergerea s-a realizat cu succes)
     * @throws RepoException daca nu exista niciun utilizator cu id-ul userId in retea
     * @throws ServiceException daca parametrul userId este invalid (este null sau este negativ (mai mic strict decat 0))
     * @throws IllegalArgumentException daca parametrul userId este invalid (este null sau este negativ (mai mic strict decat 0))
     */
    public User removeUser(Long userId) throws RepoException, ServiceException, IllegalArgumentException {
        return userService.remove(userId);
    }

    /**
     * Metoda publica de tip operand/rezultat care modifica/actualizeaza un utilizator (obiect de clasa User) din reteaua de socializare
     * @param userId obiect de clasa Long (valoare numerica intreaga cu semn) ce reprezinta identificatorul unic al utilizatorului pe care dorim sa il modificam
     * @param firstName obiect de clasa String (sir de caractere) ce reprezinta prenumele utilizatorului pe care dorim sa il modificam
     * @param lastName obiect de clasa String (sir de caractere) ce reprezinta numele de familie al utilizatorului pe care dorim sa il modificam
     * @return obiect de clasa User care reprezinta utilizatorul modificat (inainte de modificare)
     * @throws ValidationException daca utilizatorul creat nu este valid (are cel putin un atribut/camp invalid)
     * @throws RepoException daca nu exista niciun utilizator in retea sau daca utilizatorul pe care dorim sa il modificam nu exista (nu exista niciun user cu id-ul userId)
     * @throws IllegalArgumentException daca id-ul userId este invalid (este null sau este o valoare numerica intreaga strict negativa)
     */
    public User modifyUser(Long userId, String firstName, String lastName) throws ValidationException, RepoException, IllegalArgumentException {
        return userService.modify(userId, firstName, lastName);
    }

    /**
     * Metoda publica de tip operand/rezultat care cauta un utilizator (obiect de clasa User) dupa id-ul acestuia in reteaua de socializare
     * @param userId obiect de clasa Long (valoare numerica intreaga cu semn (signed)) care reprezinta identificatorul unic al utilizatorului pe care dorim sa il cautam
     * @return obiect de clasa User (utilizator valid din reteaua de prietenie) ce reprezinta utilizatorul cu id-ul egal cu userId din retea
     * @throws RepoException daca nu exista utilizatori in retea (reteaua este goala/vida) sau utilizatorul cautat nu exista (nu exista niciun utilizator cu id-ul egal cu userId)
     * @throws ServiceException daca parametrul formal/simbolic de intrare userId nu este valid (este invalid adica este null sau este o valoare numerica intreaga strict negativa)
     * @throws IllegalArgumentException daca parametrul formal/simbolic de intrare userId nu este valid (este invalid adica este null sau este o valoare numerica intreaga strict negativa)
     */
    public User searchUser(Long userId) throws RepoException, ServiceException, IllegalArgumentException {
        return userService.search(userId);
    }

    /**
     * Metoda publica de tip operand/rezultat care returneaza/intoarce numarul total al utilizatorilor (obiectelor de clasa User) din reteaua de socializare
     * @return valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) ce reprezinta numarul de utilizatori din retea
     */
    public int numberOfUsers() {
        return userService.len();
    }

    /**
     * Metoda publica de tip operand/rezultat care returneaza/intoarce lista de useri/utilizatori din reteaua de socializare<br>
     * Metoda furnizeaza un obiect iterabil (obiect care poate sa fie parcurs/iterat) a caror elemente sunt obiecte de clasa User
     * @return obiect de clasa Iterable cu elemente de tipul User ce reprezinta lista de utilizatori din retea
     * @throws RepoException daca nu exista niciun utilizator (obiect de clasa User) in reteaua de socializare
     */
    public Iterable<User> getAllUsers() throws RepoException {
        return userService.getAll();
    }

    /**
     * Metoda publica de tip operand/rezultat care returneaza/intoarce o lista cu toti prietenii unui utilizator (obiect de clasa User) din reteaua de socializare
     * @param userId obiect de clasa Long (valoare numerica intreaga cu semn (signed)) ce reprezinta id-ul userului pentru care vrem sa obtinem lista de prieteni
     * @return un obiect de clasa List (lista) cu elemente de tipul User (utilizatori valizi din reteaua de socializare) ce reprezinta lista de prieteni ai utilizatorului cu id-ul egal cu userId din reteaua de socializare
     * @throws RepoException daca nu exista entitati in retea sau daca nu exista niciun utilizator cu id-ul egal cu userId
     * @throws ServiceException daca id-ul furnizat ca si parametru de intrare metodei este invalid (este null sau mai mic strict decat 0)
     * @throws IllegalArgumentException daca id-ul furnizat ca si parametru de intrare metodei este invalid (este null sau mai mic strict decat 0)
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
