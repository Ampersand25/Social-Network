package domain;

import utils.Constants;

import java.time.LocalDate;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private User firstFriend;
    private User secondFriend;
    private LocalDate date;

    /**
     * Constructor public al unui obiect de clasa Friendship care primeste doi parametri de intrare: firstFriend si secondFriend
     * @param firstFriend - obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare reprezentand primul prieten al relatiei
     * @param secondFriend - obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare reprezentand al doilea prieten al relatiei
     */
    public Friendship(User firstFriend, User secondFriend) {
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
        this.date = LocalDate.now();
    }

    /**
     * Constructor public al unui obiect de clasa Friendship care primeste doi parametri de intrare: firstFriend, secondFriend si date
     * @param firstFriend - obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare reprezentand primul prieten al relatiei
     * @param secondFriend - obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare reprezentand al doilea prieten al relatiei
     * @param date - obiect de tipul LocalDate care reprezinta data la care s-a legat relatia de prietenie dintre cei doi useri/utilizatori: firstFriends si secondFriend
     */
    public Friendship(User firstFriend, User secondFriend, LocalDate date) {
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
        this.date = date;
    }

    /**
     * Constructor public al unui obiect de clasa Friendship care primeste doi parametri de intrare: id, firstFriend, secondFriend si date
     * @param id - obiect de tipul Long reprezentand id-ul prieteniei dintre firstFriend si secondFriend (prietenie legata la data date)
     * @param firstFriend - obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare reprezentand primul prieten al relatiei
     * @param secondFriend - obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare reprezentand al doilea prieten al relatiei
     * @param date - obiect de tipul LocalDate care reprezinta data la care s-a legat relatia de prietenie dintre cei doi useri/utilizatori: firstFriends si secondFriend
     */
    public Friendship(Long id, User firstFriend, User secondFriend, LocalDate date) {
        super(id);
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
        this.date = date;
    }

    /**
     * Metoda publica de tip getter care returneaza/intoarce primul prieten al relatiei de prietenie
     * @return - obiect de clasa User reprezentand primul prieten din relatia de prietenie pentru care se apeleaza metoda
     */
    public User getFirstFriend() {
        return firstFriend;
    }

    /**
     * Metoda publica de tip setter care modifica/actualizeaza primul prieten al relatiei de prietenie
     * @param firstFriend - obiect de clasa User (entitate) reprenzentand noul prim prieten al relatiei de prietenie
     */
    public void setFirstFriend(User firstFriend) {
        this.firstFriend = firstFriend;
    }

    /**
     * Metoda publica publica de tip getter care returneaza/intoarce al doilea prieten al relatiei de prietenie
     * @return - obiect de clasa User reprezentand al doilea prieten din relatia de prietenie pentru care se apeleaza metoda
     */
    public User getSecondFriend() {
        return secondFriend;
    }

    /**
     * Metoda publica de tip setter care modifica/actualizeaza al doilea prieten al relatiei de prietenie
     * @param secondFriend - obiect de clasa User (entitate) reprenzentand noul al doilea prieten al relatiei de prietenie
     */
    public void setSecondFriend(User secondFriend) {
        this.secondFriend = secondFriend;
    }

    /**
     * Metoda publica de tip getter care returneaza/intoarce atributul date al unui obiect de clasa Friendship
     * @return - obiect de clasa LocalDate reprenzentand data la care prietenia a fost legata intre cei doi prieteni: firstFriend si secondFriend
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Metoda publica de tip setter care modifica/actualizeaza data la care a fost creata relatia de prietenie
     * @param date - obiect de clasa LocalDate reprenzentand noua data la care prietenia a fost intemeiata
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Metoda publica de tip String care intoarce/returneaza varianta textuala a unui obiect de clasa Friendship (cum sa fie afisat instanta care apeleaza metoda)
     * @return - obiect de clasa String reprezentand forma textuala pe care o are un obiect de clasa Friendship
     */
    @Override
    public String toString() {
        return "id: " + super.getId() + "\nfirst friend: " + getFirstFriend() + "\nsecond friend: " + getSecondFriend() + "\ndate when the friendship was created: " + getDate().format(Constants.DATE_TIME_FORMATTER);
    }

    /**
     * Metoda publica de tip int (integer = intreg) care returneaza/intoarce o valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) reprenzentand codul de dispersie al unui obiect de clasa Friendship
     * @return - valoare intreaga ce reprezinta codul de dispersie al obiectului curent (obiectul care apeleaza metoda)
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstFriend().getId(), getSecondFriend().getId());
    }

    /**
     * Metoda publica de tip bool/boolean (returneaza/intoarce adevarat sau fals) care verifica daca doua obiecte sunt egale (obiectul pentru care se actualizeaza metoda si obiectul obj dat ca si parametru metodei)
     * @param obj - obiect de clasa Object reprenzentand obiectul cu care dorim sa comparam obiectul care apeleaza metoda
     * @return - true - cele doua obiecte de clasa Friendship (prietenii) sunt egale (au aceeasi prieteni)
     *           false - cele doua obiecte nu sunt egale (nu au aceeasi prieteni sau obj nu este obiect al clasei Friendship (nu este instanta a clasei mentionate))
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof Friendship that)) {
            return false;
        }
        return (Objects.equals(getFirstFriend().getId(), that.getFirstFriend().getId()) && Objects.equals(getSecondFriend().getId(), that.getSecondFriend().getId())) ||
                (Objects.equals(getFirstFriend().getId(), that.getSecondFriend().getId()) && Objects.equals(getSecondFriend().getId(), that.getFirstFriend().getId()));
    }
}
