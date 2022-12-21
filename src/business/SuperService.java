package business;

import domain.User;
import domain.Friendship;
import exception.ValidationException;
import exception.RepoException;
import exception.ServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
     * @param homeAddress obiect de clasa String (sir de caractere) ce reprezinta adresa fizica (strada, numar, bloc, scara, etaj, etc) unde traieste utilizatorul pe care dorim sa il adaugam in retea
     * @param country obiect de clasa String (sir de caractere) ce reprezinta tara in care traieste utilizatorul pe care dorim sa il adaugam in retea
     * @param county obiect de clasa String (sir de caractere) ce reprezinta judetul in care traieste utilizatorul pe care dorim sa il adaugam in retea
     * @param city obiect de clasa String (sir de caractere) ce reprezinta orasul in care traieste utilizatorul pe care dorim sa il adaugam in retea
     * @throws ValidationException daca utilizatorul creat nu este valid (are cel putin un atribut/camp invalid)
     * @throws RepoException daca utilizatorul exista deja in reteaua de socializare (exista un obiect de clasa User care are acelasi id)
     * @throws IllegalArgumentException daca identificatorul unic al utilizatorului nu este valid (este null sau este mai mic strict decat 0)
     */
    public void addUser(String firstName, String lastName, LocalDate birthday, String email, String homeAddress, String country, String county, String city, String username, String password) throws ValidationException, RepoException, IllegalArgumentException {
        userService.add(firstName, lastName, birthday, email, homeAddress, country, county, city, username, password);
    }

    /**
     * Metoda publica de tip operand/rezultat care sterge/elimina un utilizator (obiect de clasa User) din reteaua de socializare<br>
     * Metoda se asigura ca toate prieteniile ce contin utilizatorul cu id-ul userId sunt sterse/eliminate din retea
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
     * Metoda publica de tip operand/rezultat care modifica/actualizeaza un utilizator (obiect de clasa User) din reteaua de socializare<br>
     * Metoda se asigura ca toate relatiile de prietenie ce contin utilizatorul (obiect de clasa User) cu identificatorul unic egal cu parametrul de intrare userId vor fi actualizate
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
    public User modifyUser(Long userId, String firstName, String lastName, String homeAddress, String country, String county, String city, String username, String password) throws ValidationException, RepoException, IllegalArgumentException {
        return userService.modify(userId, firstName, lastName, homeAddress, country, county, city, username, password);
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
     * Metoda publica de tip operand/rezultat (returneaza/intoarce o valoare prin numele functiei) care cauta utilizatorii (obiectele de clasa User) din reteaua de socializare care contin sirul de caractere (stringul) name (dat ca si parametru de intrare metodei/functiei) in numele lor (numele sau prenumele utilizatorului contine stringul name)
     * @param name obiect de clasa String ce reprezinta numele dupa care se filtreaza utilizatorii din retea
     * @return obiect de clasa List cu elemente de tipul User (utilizatori existenti in reteaua de socializare) care reprezinta o lista cu toti userii/utilizatorii care contin parametrul formal/simbolic name in numele lor complet (adica fie in nume fie in prenume)
     * @throws RepoException daca nu exista utilizatori in retea
     * @throws ServiceException daca parametrul name este null sau lungimea sa este egala cu 0 (name este sirul de caractere vid)
     */
    public List<User> searchUserAfterName(String name) throws RepoException, ServiceException {
        return userService.searchUserAfterName(name);
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
     * Metoda publica de tip void (functie procedurala) care adauga o relatie de prietenie (obiect de clasa Friendship) in reteaua de socializare
     * @param firstFriendId obiect de clasa Long ce reprezinta identificatorul unic (id-ul) primului prieten
     * @param secondFriendId obiect de clasa Long ce reprezinta identificatorul unic (id-ul) celui de al doilea prieten
     * @throws ValidationException daca relatia de prietenie creata nu este valida (este invalida)
     * @throws RepoException daca exista deja o relatie de prietenie intre cei doi prieteni cu id-urile firstFriendId respectiv secondFriendId
     * @throws ServiceException daca cel putin unul dintre cele doua id-uri nu este valid (firstFriendId sau secondFriendId este invalid; cele doua id-uri trebuie sa fie diferite de null si sa fie valori numerice intreagi mai mari sau egale cu 0)
     * @throws IllegalArgumentException daca cel putin unul dintre cele doua id-uri nu este valid (firstFriendId sau secondFriendId este invalid; cele doua id-uri trebuie sa fie diferite de null si sa fie valori numerice intreagi mai mari sau egale cu 0)
     */
    public void addFriendship(Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException, ServiceException, IllegalArgumentException {
        friendshipService.add(firstFriendId, secondFriendId);
    }

    /**
     * Metoda publica de tip operand/rezultat care sterge/elimina o relatie de prietenie (obiect de clasa Friendship) din reteaua de socializare
     * @param friendshipId obiect de clasa Long ce reprezinta id-ul (identificatorul unic) prieteniei pe care dorim sa o stergem/eliminam
     * @return obiect de clasa Friendship (prietenie valida din reteaua de socializare) ce reprezinta prietenia stearsa/eliminata
     * @throws RepoException daca nu exista prietenii in retea sau prietenia cu id-ul egal cu friendshipId nu exista (nu exista nicio prietenie care sa aiba id-ul egal cu friendshipId)
     * @throws ServiceException daca parametrul formal/simbolic de intrare friendshipId este null sau reprezinta o valoare numerica strict negativa (mai mica strict decat 0)
     * @throws IllegalArgumentException daca parametrul formal/simbolic de intrare friendshipId este null sau reprezinta o valoare numerica strict negativa (mai mica strict decat 0)
     */
    public Friendship removeFriendship(Long friendshipId) throws RepoException, ServiceException, IllegalArgumentException {
        return friendshipService.remove(friendshipId);
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
    public Friendship modifyFriendship(Long friendshipId, Long firstFriendId, Long secondFriendId) throws ValidationException, RepoException, IllegalArgumentException {
        return friendshipService.modify(friendshipId, firstFriendId, secondFriendId);
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
    public Friendship modifyFriendship(Long friendshipId, Long firstFriendId, Long secondFriendId, LocalDateTime friendsFrom) throws ValidationException, RepoException, IllegalArgumentException {
        return friendshipService.modify(friendshipId, firstFriendId, secondFriendId, friendsFrom);
    }

    /**
     * Metoda publica de tip operand/rezultat care cauta un prieten (obiect de clasa Friendship) in reteaua de socializare
     * @param friendshipId obiect de clasa Long ce reprezinta identificatorul unic al utilizatorului cautat
     * @return obiect de clasa Friendship (entitatea prietenie) ce reprezinta prietenia cu id-ul egal cu friendshipId din retea
     * @throws RepoException daca nu exista prietenii (obiecte de clasa Friendship) in retea sau daca prietenia cautata nu exista (nu exista nicio prietenie cu id-ul egal cu friendshipId)
     * @throws ServiceException daca parametrul formal/simbolic de intrare friendshipId este invalid (este null sau este o valoare numerica intreaga mai mica strict decat 0)
     * @throws IllegalArgumentException daca parametrul formal/simbolic de intrare friendshipId este invalid (este null sau este o valoare numerica intreaga mai mica strict decat 0)
     */
    public Friendship searchFriendship(Long friendshipId) throws RepoException, ServiceException, IllegalArgumentException {
        return friendshipService.search(friendshipId);
    }

    /**
     * Metoda publica de tip operand/rezultat care returneaza/intoarce numarul total de prietenii din relatia de prietenie
     * @return valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) ce reprezinta numarul de prietenii din retea
     */
    public int numberOfFriendships() {
        return friendshipService.len();
    }

    /**
     * Metoda publica de tip operand/rezultat care returneaza o lista cu toate prieteniile (obiecte de clasa Friendship) din reteaua de socializare
     * @return obiect de clasa Iterable (obiect iterabil care poate sa fie parcurs/iterat) ce reprezinta lista de prietenii din retea
     * @throws RepoException daca nu exista nicio prietenie in reteaua de socializare
     */
    public Iterable<Friendship> getAllFriendships() throws RepoException {
        return friendshipService.getAll();
    }

    /**
     * Metoda publica de tip int (integer = intreg) care returneaza numarul de comunitati din reteaua de socializare (adica numarul de componente conexe din grafului retelei)<br>
     * Se considera ca si comunitate (componenta conexa) si un singur utilizator (un user fara prieteni), acesta va reprezenta un nod/varf izolot in graful retelei
     * @return valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) reprezentand numarul de comunitati din retea
     */
    public int numberOfCommunities() {
        return friendshipService.numberOfCommunities();
    }

    /**
     * Metoda publica de tip operand (rezultat) care returneaza/intoarce o lista cu toate comunitatile din reteaua de socializare (o comunitate reprezinta o componenta conexa din graful retelei)<br>
     * Se considera ca si comunitate (componenta conexa) si un singur utilizator (un user fara prieteni), acesta va reprezenta un nod/varf izolot in graful retelei
     * @return lista de elemente de tipul lista cu elemente numere intregi de tip long (obiecte de clasa Long) ce reprezinta lista tuturor comunitatilor din reteaua de socializare
     * @throws RepoException daca nu exista utilizatori (obiecte de clasa User) in reteaua de socializare
     */
    public List<List<Long>> getAllCommunities() throws RepoException {
        return friendshipService.getAllCommunities();
    }
}
