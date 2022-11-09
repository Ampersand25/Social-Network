package domain;

import utils.Constants;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private User firstFriend;
    private User secondFriend;
    private LocalDateTime friendsFrom;

    /**
     * Constructor public al unui obiect de clasa Friendship care primeste doi parametri de intrare: firstFriend de tipul User (utilizator din reteaua de socializare) si secondFriend de tipul User (utilizator din reteaua de socializare)
     * @param firstFriend obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare reprezentand primul prieten al relatiei
     * @param secondFriend obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare reprezentand al doilea prieten al relatiei
     */
    public Friendship(User firstFriend, User secondFriend) {
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
        this.friendsFrom = LocalDateTime.now();
    }

    /**
     * Constructor public al unui obiect de clasa Friendship care primeste trei parametri de intrare: firstFriend de tipul User, secondFriend de tipul User si friendsFrom de tipul LocalDateTime
     * @param firstFriend obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare simbolizand primul prieten al relatiei de prietenie
     * @param secondFriend obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare simbolizand al doilea prieten al relatiei de prietenie
     * @param friendsFrom obiect de clasa LocalDateTime care reprezinta data si ora la care s-a legat relatia de prietenie dintre cei doi useri/utilizatori: firstFriends si secondFriend
     */
    public Friendship(User firstFriend, User secondFriend, LocalDateTime friendsFrom) {
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
        this.friendsFrom = friendsFrom;
    }

    /**
     * Constructor public al unui obiect de clasa Friendship care primeste patru parametri de intrare: id de tipul Long, firstFriend de tipul User, secondFriend de tipul User si friendsFrom de tipul LocalDateTime
     * @param id obiect de clasa Long reprezentand identificatorul unic al relatiei de prietenie dintre firstFriend si secondFriend (prietenie legata la data si ora egala cu valoarea atributului privat friendsFrom al obiectului de clasa Friendship)
     * @param firstFriend obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare simbolizand primul prieten al relatiei
     * @param secondFriend obiect de clasa User (entitate) care reprezinta un utilizator valid din reteaua de socializare simbolizand al doilea prieten al relatiei
     * @param friendsFrom obiect de clasa LocalDateTime care reprezinta data si ora la care s-a legat relatia de prietenie dintre cei doi useri/utilizatori: firstFriends si secondFriend
     */
    public Friendship(Long id, User firstFriend, User secondFriend, LocalDateTime friendsFrom) {
        super(id);
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
        this.friendsFrom = friendsFrom;
    }

    /**
     * Metoda publica de tip getter care returneaza/intoarce primul prieten al relatiei de prietenie (atributul/campul firstFriend)
     * @return obiect de clasa User (utilizator valid din reteaua de socializare) reprezentand primul prieten din relatia de prietenie pentru care se apeleaza metoda
     */
    public User getFirstFriend() {
        return firstFriend;
    }

    /**
     * Metoda publica de tip setter care modifica/actualizeaza primul prieten al relatiei de prietenie (suprascrie atributul/campul firstFriend)
     * @param firstFriend obiect de clasa User (utilizator valid din reteaua de socializare) reprenzentand noul prim prieten al relatiei de prietenie
     */
    public void setFirstFriend(User firstFriend) {
        this.firstFriend = firstFriend;
    }

    /**
     * Metoda publica de tip getter care returneaza/intoarce al doilea prieten al relatiei de prietenie (atributul/campul secondFriend)
     * @return obiect de clasa User (utilizator valid din reteaua de socializare) reprezentand al doilea prieten din relatia de prietenie pentru care se apeleaza metoda
     */
    public User getSecondFriend() {
        return secondFriend;
    }

    /**
     * Metoda publica de tip setter care modifica/actualizeaza al doilea prieten al relatiei de prietenie (suprascrie atributul/campul secondFriend)
     * @param secondFriend obiect de clasa User (utilizator valid din reteaua de socializare) reprenzentand noul al doilea prieten al relatiei de prietenie
     */
    public void setSecondFriend(User secondFriend) {
        this.secondFriend = secondFriend;
    }

    /**
     * Metoda publica de tip getter care returneaza/intoarce atributul friendsFrom al unui obiect de clasa Friendship (adica al unei prietenii din reteaua de socializare)
     * @return obiect de clasa LocalDateTime reprenzentand data (zi, luna, an) si ora (ora, minut, secunda) la care prietenia a fost legata intre cei doi prieteni: firstFriend si secondFriend (atribute private ale obiectului de clasa Friendship)
     */
    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    /**
     * Metoda publica de tip setter care modifica/actualizeaza data si ora la care a fost creata relatia de prietenie (obiect de clasa Friendship)
     * @param friendsFrom obiect de clasa LocalDateTime reprenzentand noua data (zi, luna, an) si ora (ora, minut, secunda) la care prietenia a fost intemeiata intre cei doi prieteni reprezentati de atributele firstFriend si secondFriend
     */
    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    /**
     * Metoda publica de tip String care intoarce/returneaza varianta textuala (forma scrisa) a unui obiect de clasa Friendship (cum sa fie afisat obiectul (instanta clasei Friendship) care apeleaza metoda)
     * @return obiect de clasa String ce reprezinta forma textuala/scrisa pe care o are un obiect de clasa Friendship
     */
    @Override
    public String toString() {
        return "id: " + super.getId() + "\nfirst friend: " + getFirstFriend() + "\nsecond friend: " + getSecondFriend() + "\ndate (year, month, day) and time (hour, minute, second) when the friendship was created: " + getFriendsFrom().format(Constants.DATE_TIME_FORMATTER);
    }

    /**
     * Metoda publica de tip int (integer = intreg) care returneaza/intoarce o valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) reprenzentand codul de dispersie al unui obiect de clasa Friendship
     * @return valoare intreaga ce reprezinta codul de dispersie al obiectului curent (obiectul care apeleaza metoda)
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstFriend().getId(), getSecondFriend().getId());
    }

    /**
     * Metoda publica de tip bool/boolean (returneaza/intoarce adevarat (true) sau fals (false)) care verifica daca doua obiecte sunt egale (obiectul pentru care se apeleaza metoda si obiectul obj dat ca si parametru de intrare metodei)
     * @param obj obiect de clasa Object reprenzentand obiectul cu care dorim sa comparam obiectul care apeleaza metoda (obiectul curent ce reprezinta o instanta a clasei Friendship)
     * @return <b>false</b> - cele doua obiecte nu sunt egale (fie nu sunt identice (nu coincid), fie obj nu este un obiect de clasa Friendship sau obj este o instanta a clasei Friendship dar are cel putin un prieten diferit fata de relatia de prietenie curenta care apeleaza metoda)<br>
     *         <b>true</b> - cele doua obiecte de clasa Friendship (prietenii) sunt egale (coincid sau ambele sunt de tipul Friendship si contin aceeasi useri/utilizatori ca prieteni (au acelasi atribut firstFriend si secondFriend))
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
