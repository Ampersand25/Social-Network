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
     * Constructor al unui obiect de clasa User care primeste 4 parametrii
     * @param firstName - obiect de clasa String care reprezinta prenumele utilizatorului creat
     * @param lastName - obiect de clasa String care reprezinta numele utilizatorului creat
     * @param birthday - obiect de clasa LocalDate care reprezinta data nasterii utilizatorului creat
     * @param email - obiect de clasa String care reprezinta adresa de email a utilizatorului creat
     */
    public User(String firstName, String lastName, LocalDate birthday, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        friendList = new ArrayList<>();
    }

    /**
     * Constructor al unui obiect de clasa User care primeste 5 parametrii
     * @param id - obiect de clasa Long care reprezinta id-ul utilizatorului creat
     * @param firstName - obiect de clasa String care reprezinta prenumele utilizatorului creat
     * @param lastName - obiect de clasa String care reprezinta numele utilizatorului creat
     * @param birthday - obiect de clasa LocalDate care reprezinta data nasterii utilizatorului creat
     * @param email - obiect de clasa String care reprezinta adresa de email a utilizatorului creat
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
     * Metoda publica de tip getter care intoarce prenumele unui utilizator (atributul firstName)
     * @return - atributul firstName (obiect de clasa String) al unui obiect de clasa User
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Metoda publica de tip void (nu returneaza/intoarce nicio valoare) de tip setter care modifica/actualizeaza prenumele unui utilizator (atributul firstName)
     * @param firstName - noul prenume al utilizatorului curent (obiect de clasa String)
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Metoda publica de tip getter care intoarce numele unui utilizator (atributul lastName)
     * @return - atributul lastName (obiect de clasa String) al unui obiect de clasa User
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Metoda publica de tip void (nu returneaza/intoarce nicio valoare) de tip setter care modifica/actualizeaza numele unui utilizator (atributul lastName)
     * @param lastName - noul nume al utilizatorului curent (obiect de clasa String)
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Metoda publica de tip getter care intoarce data nasterii unui utilizator (atributul birthday)
     * @return - atributul birthday (obiect de clasa LocalDate) al unui obiect de clasa User
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Metoda publica de tip getter care intoarce adresa de email a unui utilizator (atributul email)
     * @return - atributul email (obiect de clasa String) al unui obiect de clasa User
     */
    public String getEmail() {
        return email;
    }

    /**
     * Metoda publica de tip getter care intoarce lista de prieteni a unui utilizator (atributul friendList)
     * @return - atributul friendList (obiect de clasa List de elemente de tipul User) a unui obiect de clasa User
     */
    public List<User> getFriendList() {
        return friendList;
    }

    /**
     * Metoda publica de tip void (nu returneaza/intoarce nicio valoare) de tip setter care modifica/actualizeaza lista de utilizatori (useri) ai unui utilizator (atributul friendList)
     * @param friendList - noua lista de prieteni a utilizatorului curent (obiect de clasa List cu elemente obiecte de clasa User)
     */
    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }

    /**
     * Metoda publica de tip String care intoarce/returneaza varianta textuala a unui obiect de clasa User (cum sa fie afisat instanta care apeleaza metoda)
     * @return - obiect de clasa String reprezentand forma textuala pe care o are un obiect de clasa User
     */
    @Override
    public String toString() {
        return "id=" + super.getId() + "|first name=" + getFirstName() + "|last name=" + getLastName() + "|birthday=" + getBirthday() + "|email=" + getEmail();
    }

    /**
     * Metoda publica de tip int (integer = intreg) care returneaza/intoarce o valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) reprenzentand codul de dispersie al unui obiect de clasa User
     * @return - valoare intreaga ce reprezinta codul de dispersie al obiectului curent (obiectul care apeleaza metoda)
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.getId());
    }

    /**
     * Metoda publica de tip bool/boolean (returneaza/intoarce adevarat sau fals) care verifica daca doua obiecte sunt egale (obiectul pentru care se actualizeaza metoda si obiectul obj dat ca si parametru metodei)
     * @param obj - obiect de clasa Object reprenzentand obiectul cu care dorim sa comparam obiectul care apeleaza metoda
     * @return - true - cele doua obiecte de clasa User (utilizatori) sunt egale (au acelasi id)
     *           false - cele doua obiecte nu sunt egale (nu au acelasi id sau obj nu este obiect al clasei User (nu este instanta a clasei mentionate))
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
