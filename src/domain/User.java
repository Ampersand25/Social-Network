package domain;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.time.LocalDate;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private final LocalDate birthday;
    private final String email;
    private List<User> friendList;

    /**
     * Constructor public al unui obiect de clasa User care primeste 4 parametrii de intrare (parametrii formali/simbolici)
     * @param firstName obiect de clasa String care reprezinta prenumele utilizatorului creat
     * @param lastName obiect de clasa String care reprezinta numele de familie al utilizatorului creat
     * @param birthday obiect de clasa LocalDate care reprezinta data nasterii (zi, luna, an) utilizatorului creat
     * @param email obiect de clasa String care reprezinta adresa de email a utilizatorului creat
     */
    public User(String firstName, String lastName, LocalDate birthday, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        friendList = new ArrayList<>();
    }

    /**
     * Constructor public al unui obiect de clasa User care primeste 5 parametrii de intrare (parametrii formali/simbolici)
     * @param id obiect de clasa Long care reprezinta identificatorul unic al utilizatorului creat
     * @param firstName obiect de clasa String care reprezinta prenumele utilizatorului creat
     * @param lastName obiect de clasa String care reprezinta numele de familie al utilizatorului creat
     * @param birthday obiect de clasa LocalDate care reprezinta data nasterii (zi, luna, an) utilizatorului creat
     * @param email obiect de clasa String care reprezinta adresa de email a utilizatorului creat
     */
    public User(Long id, String firstName, String lastName, LocalDate birthday, String email) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        friendList = new ArrayList<>();
    }

    /**
     * Metoda publica de tip getter care intoarce prenumele unui utilizator (atributul/campul firstName)
     * @return componenta firstName (obiect de clasa String) a unui utilizator (obiect de clasa User)
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Metoda publica de tip void (nu returneaza/intoarce nicio valoare) de tip setter care modifica/actualizeaza prenumele unui utilizator (atributul/campul firstName)
     * @param firstName noul prenume (obiect de clasa String) al utilizatorului curent
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Metoda publica de tip getter care intoarce numele de familie al unui utilizator (atributul/campul lastName)
     * @return componenta lastName (obiect de clasa String) a unui utilizator (obiect de clasa User)
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Metoda publica de tip void (nu returneaza/intoarce nicio valoare) de tip setter care modifica/actualizeaza numele de familie al unui utilizator (atributul/campul lastName)
     * @param lastName noul nume de familie (obiect de clasa String) al utilizatorului curent
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Metoda publica de tip getter care intoarce data nasterii (zi, luna, an) a unui utilizator (atributul/campul birthday)
     * @return componenta birthday (obiect de clasa LocalDate) a unui utilizator (obiect de clasa User)
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Metoda publica de tip getter care intoarce adresa de email a unui utilizator (atributul/campul email)
     * @return componenta email (obiect de clasa String) a unui utilizator (obiect de clasa User)
     */
    public String getEmail() {
        return email;
    }

    /**
     * Metoda publica de tip getter care intoarce lista de prieteni a unui utilizator (atributul/campul friendList)
     * @return componenta friendList (obiect de clasa List cu elemente care sunt instante ale clasei User (elementele reprezinta utilizatori din reteaua de socializare)) a unui utilizator (obiect de clasa User)
     */
    public List<User> getFriendList() {
        return friendList;
    }

    /**
     * Metoda publica de tip void (nu returneaza/intoarce nicio valoare) de tip setter care modifica/actualizeaza lista de utilizatori (useri) ai unui utilizator (atributul/campul friendList)
     * @param friendList noua lista de prieteni (obiect de clasa List (interfata) cu elemente obiecte de clasa User (utilizatori din retea)) ai utilizatorului curent
     */
    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }

    /**
     * Metoda publica de tip String care intoarce/returneaza varianta textuala a unui obiect de clasa User (cum sa fie afisata pe ecran (in consola/terminal) instanta clasei User care apeleaza metoda)
     * @return obiect de clasa String ce reprezinta forma textuala (forma scrisa) pe care o are un obiect de clasa User (un utilizator din reteaua de socializare)
     */
    @Override
    public String toString() {
        return "id=" + super.getId() + "|first name=" + getFirstName() + "|last name=" + getLastName() + "|birthday=" + getBirthday() + "|email=" + getEmail();
    }

    /**
     * Metoda publica de tip int (integer = intreg) care returneaza/intoarce o valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) reprenzentand codul de dispersie al unui obiect de clasa User (al unui utilizator din reteaua de socializare)
     * @return valoare intreaga ce reprezinta codul de dispersie al obiectului curent (obiectul care apeleaza metoda hashCode)
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.getId());
    }

    /**
     * Metoda publica de tip bool/boolean (returneaza/intoarce adevarat (true) sau fals (false)) care verifica daca doua obiecte (this (obiectul curent care apeleaza metoda) si obj (obiect dat ca si parametru de intrare al metodei la apel)) sunt egale (sunt acelasi obiect sau au acelasi identificator unic)
     * @param obj obiect de tipul Object (instanta a clasei Object) ce reprezinta obiectul cu care dorim sa comparam obiectul curent (obiectul care apeleaza metoda)
     * @return <b>false</b> - daca cele doua obiecte nu sunt egale (parametrul formal/simbolic obj nu este obiectul curent sau nu este o instanta a clasei User sau este o instanta a clasei user dar are un identificator unic diferit fata de obiectul pentru care se apeleaza metoda)<br>
     *         <b>true</b> - daca cele doua obiecte sunt egale (cele doua obiecte sunt identice sau ambele sunt instante ale clasei User si au acelasi identificator unic)
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof User that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }
}
