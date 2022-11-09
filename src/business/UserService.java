package business;

import domain.Address;
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

import org.jetbrains.annotations.NotNull;

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
    public UserService(IValidator<User> validator, @NotNull IRepository<Long, User> repo) {
        this.validator = validator;
        this.repo = repo;
        availableId = 0L;
        try {
            for(User user : repo.getAll()) {
                availableId = Math.max(availableId, user.getId());
            }
            ++availableId;
        } catch(RepoException ignored) {}
    }

    /**
     * Metoda publica de tip void (procedurala) care adauga un utilizator (obiect de clasa User) in reteaua de socializare<br>
     * Metoda construieste un obiect de clasa User (un utilizator), il valideaza folosind un validator si il adauga in lista de useri/utilizatori din repozitoriu
     * @param firstName obiect de clasa String (sir de caractere) ce reprezinta prenumele utilizatorului pe care dorim sa il adaugam in retea
     * @param lastName obiect de clasa String (sir de caractere) ce reprezinta numele de familie al utilizatorului pe care dorim sa il adaugam in retea
     * @param birthday obiect de clasa LocalDate (data calendaristica) ce reprezinta data nasterii utilizatorului pe care dorim sa il adaugam in retea
     * @param email obiect de clasa String (sir de caractere) ce reprezinta adresa de email a utilizatorului pe care dorim sa il adaugam in retea
     * @param homeAddress obiect de clasa String (sir de caractere) ce reprezinta adresa fizica (strada, numar, bloc, scara, etaj, etc) unde traieste utilizatorul pe care dorim sa il adaugam in retea
     * @param country obiect de clasa String (sir de caractere) ce reprezinta tara in care traieste utilizatorul pe care dorim sa il adaugam in retea
     * @param county obiect de clasa String (sir de caractere) ce reprezinta judetul in care traieste utilizatorul pe care dorim sa il adaugam in retea
     * @param city obiect de clasa String (sir de caractere) ce reprezinta orasul in care traieste utilizatorul pe care dorim sa il adaugam in retea
     * @throws ValidationException daca utilizatorul creat nu este valid (are cel putin un atribut/camp invalid)
     * @throws RepoException daca utilizatorul exista deja in reteaua de socializare (exista un obiect de clasa User care are acelasi id)
     * @throws IllegalArgumentException daca identificatorul unic al utilizatorului nu este valid (este null sau este mai mic strict decat 0)
     */
    public void add(String firstName, String lastName, LocalDate birthday, String email, String homeAddress, String country, String county, String city) throws ValidationException, RepoException, IllegalArgumentException {
        Address address = new Address(homeAddress, country, county, city);
        User user = new User(firstName, lastName, birthday, email, address);
        user.setId(availableId++);

        try{
            validator.validate(user);
        } catch(ValidationException ex) {
            --availableId;
            throw new ValidationException(ex.getMessage());
        }

        repo.add(user);
    }

    /**
     * Metoda publica de tip operand/rezultat care sterge/elimina un utilizator (obiect de clasa User) din reteaua de socializare
     * @param userId identificatorul unic al utilizatorului pe care dorim sa il stergem/eliminam din retea
     * @return obiect de clasa User ce reprezinta utilizatorul cu id-ul userId pe care l-am sters din retea (intoarce utilizatorul sters in cazul in care stergerea s-a realizat cu succes)
     * @throws RepoException daca nu exista niciun utilizator cu id-ul userId in retea
     * @throws ServiceException daca parametrul userId este invalid (este null sau este negativ (mai mic strict decat 0))
     * @throws IllegalArgumentException daca parametrul userId este invalid (este null sau este negativ (mai mic strict decat 0))
     */
    public User remove(Long userId) throws RepoException, ServiceException, IllegalArgumentException {
        validateId(userId);

        User removedUser = repo.remove(userId);
        try {
            Iterable<User> allUsers = repo.getAll();
            allUsers.forEach(user -> {
                List<User> friendListOfCurrentUser = user.getFriendList();
                List<User> filteredFriendListOfCurrentUser = friendListOfCurrentUser.stream().filter(friendOfUser -> !friendOfUser.equals(removedUser)).collect(Collectors.toList());
                user.setFriendList(filteredFriendListOfCurrentUser);
            });
        } catch(RepoException ignored) {}

        return removedUser;
    }

    /**
     * Metoda publica de tip operand/rezultat care modifica/actualizeaza un utilizator (obiect de clasa User) din reteaua de socializare
     * @param userId obiect de clasa Long (valoare numerica intreaga cu semn) ce reprezinta identificatorul unic al utilizatorului pe care dorim sa il modificam
     * @param firstName obiect de clasa String (sir de caractere) ce reprezinta prenumele utilizatorului pe care dorim sa il modificam
     * @param lastName obiect de clasa String (sir de caractere) ce reprezinta numele de familie al utilizatorului pe care dorim sa il modificam
     * @param homeAddress obiect de clasa String (sir de caractere) ce reprezinta adresa fizica (strada, numar, bloc, scara, etaj, etc) unde traieste utilizatorul pe care dorim sa il adaugam in retea
     * @param country obiect de clasa String (sir de caractere) ce reprezinta tara in care traieste utilizatorul pe care dorim sa il adaugam in retea
     * @param county obiect de clasa String (sir de caractere) ce reprezinta judetul in care traieste utilizatorul pe care dorim sa il adaugam in retea
     * @param city obiect de clasa String (sir de caractere) ce reprezinta orasul in care traieste utilizatorul pe care dorim sa il adaugam in retea
     * @return obiect de clasa User care reprezinta utilizatorul modificat (inainte de modificare)
     * @throws ValidationException daca utilizatorul creat nu este valid (are cel putin un atribut/camp invalid)
     * @throws RepoException daca nu exista niciun utilizator in retea sau daca utilizatorul pe care dorim sa il modificam nu exista (nu exista niciun user cu id-ul userId)
     * @throws IllegalArgumentException daca id-ul userId este invalid (este null sau este o valoare numerica intreaga strict negativa)
     */
    public User modify(Long userId, String firstName, String lastName, String homeAddress, String country, String county, String city) throws ValidationException, RepoException, IllegalArgumentException {
        LocalDate birthday = LocalDate.now();
        String email = "";
        List<User> friendList = new ArrayList<>();
        try {
            User searchedUser = search(userId);
            birthday = searchedUser.getBirthday();
            email = searchedUser.getEmail();
            friendList = searchedUser.getFriendList();
        } catch (ServiceException ignored) {}

        Address address = new Address(homeAddress, country, county, city);
        User newUser = new User(userId, firstName, lastName, birthday, email, address);
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

    /**
     * Metoda publica de tip operand/rezultat care cauta un utilizator (obiect de clasa User) dupa id-ul acestuia in reteaua de socializare
     * @param userId obiect de clasa Long (valoare numerica intreaga cu semn (signed)) care reprezinta identificatorul unic al utilizatorului pe care dorim sa il cautam
     * @return obiect de clasa User (utilizator valid din reteaua de prietenie) ce reprezinta utilizatorul cu id-ul egal cu userId din retea
     * @throws RepoException daca nu exista utilizatori in retea (reteaua este goala/vida) sau utilizatorul cautat nu exista (nu exista niciun utilizator cu id-ul egal cu userId)
     * @throws ServiceException daca parametrul formal/simbolic de intrare userId nu este valid (este invalid adica este null sau este o valoare numerica intreaga strict negativa)
     * @throws IllegalArgumentException daca parametrul formal/simbolic de intrare userId nu este valid (este invalid adica este null sau este o valoare numerica intreaga strict negativa)
     */
    public User search(Long userId) throws RepoException, ServiceException, IllegalArgumentException {
        validateId(userId);
        return repo.search(userId);
    }

    /**
     * Metoda publica de tip operand/rezultat care returneaza/intoarce numarul total al utilizatorilor (obiectelor de clasa User) din reteaua de socializare
     * @return valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) ce reprezinta numarul de utilizatori din retea
     */
    public int len() {
        return repo.len();
    }

    /**
     * Metoda publica de tip operand/rezultat care returneaza/intoarce lista de useri/utilizatori din reteaua de socializare<br>
     * Metoda furnizeaza un obiect iterabil (obiect care poate sa fie parcurs/iterat) a caror elemente sunt obiecte de clasa User
     * @return obiect de clasa Iterable cu elemente de tipul User ce reprezinta lista de utilizatori din retea
     * @throws RepoException daca nu exista niciun utilizator (obiect de clasa User) in reteaua de socializare
     */
    public Iterable<User> getAll() throws RepoException {
        return repo.getAll();
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
        validateId(userId);
        return repo.search(userId).getFriendList();
    }
}
