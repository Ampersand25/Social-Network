package validation;

import domain.Friendship;
import exception.ValidationException;

import java.time.LocalDate;

public class FriendshipValidator implements IValidator<Friendship> {
    /**
     * Metoda publica de tip void (nu intoarce/returneaza niciun rezultat) care valideaza o prietenie (un obiect de tipul Friendship)<br>
     * Se vor valida toate datele membru (campurile) ale obiectului dat: id, firstFriend, secondFriend si date
     * @param friendship obiect de clasa Friendship (instanta a clasei Friendship) pe care dorim sa il validam
     * @throws ValidationException daca obiectul/entitatea friendship nu este valida (contine campuri/atribute/componente invalide)
     */
    @Override
    public void validate(Friendship friendship) throws ValidationException {
        String err = new String("");

        Long friendshipId = friendship.getId();
        if(friendshipId == null) {
            err += "[!]Invalid friendship id (id must not be null)!\n";
        }
        else if(friendshipId < 0L) {
            err += "[!]Invalid friendship id (id must be non-negative)!\n";
        }

        if(friendship.getFirstFriend() == null) {
            err += "[!]Invalid first friend (first friend must not be null)!\n";
        }

        if(friendship.getSecondFriend() == null) {
            err += "[!]Invalid second friend (second friend must not be null)!\n";
        }

        LocalDate friendshipDate = friendship.getDate();
        if(friendshipDate == null) {
            err += "[!]Invalid date (date must not be null)!\n";
        }
        else if(friendshipDate.isAfter(LocalDate.now())) {
            err += "[!]Invalid date (date must not be in the future)!\n";
        }
        else if(friendshipDate.isBefore(LocalDate.now().minusYears(120))) {
            err += "[!]Invalid date (date must be sooner than 120 years)!\n";
        }

        if(err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
