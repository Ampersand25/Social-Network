package validation;

import domain.Friendship;
import exception.ValidationException;

public class FriendshipValidator implements IValidator<Friendship> {
    @Override
    public void validate(Friendship friendship) throws ValidationException {
        String err = new String("");
        if(friendship.getId() < 0L) {
            err += "[!]Invalid id!\n";
        }
        if(friendship.getFirstFriend() == null) {
            err += "[!]Invalid first friend!\n";
        }
        if(friendship.getSecondFriend() == null) {
            err += "[!]Invalid second friend!\n";
        }
        if(friendship.getDate() == null) {
            err += "[!]Invalid date!\n";
        }
        if(err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
