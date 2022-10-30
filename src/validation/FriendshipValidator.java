package validation;

import domain.Friendship;
import exception.ValidationException;

import java.time.LocalDate;

public class FriendshipValidator implements IValidator<Friendship> {
    @Override
    public void validate(Friendship friendship) throws ValidationException {
        String err = new String("");

        if(friendship.getId() == null) {
            err += "[!]Invalid friendship id (id must not be null)!\n";
        }
        else if(friendship.getId() < 0L) {
            err += "[!]Invalid friendship id (id must be non-negative)!\n";
        }

        if(friendship.getFirstFriend() == null) {
            err += "[!]Invalid first friend (first friend must not be null)!\n";
        }

        if(friendship.getSecondFriend() == null) {
            err += "[!]Invalid second friend (second friend must not be null)!\n";
        }

        if(friendship.getDate() == null) {
            err += "[!]Invalid date (date must not be null)!\n";
        }
        else if(friendship.getDate().isAfter(LocalDate.now())) {
            err += "[!]Invalid date (date must not be in the future)!\n";
        }
        else if(friendship.getDate().isBefore(LocalDate.now().minusYears(120))) {
            err += "[!]Invalid date (date must be sooner than 120 years)!\n";
        }

        if(err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
