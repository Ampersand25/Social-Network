package validation;

import domain.User;
import exception.ValidationException;

public class UserValidator implements IValidator<User> {
    private boolean validName(String name) {
        if(name.length() == 0) {
            return false;
        }
        for(int index = 0; index < name.length(); ++index) {
            Character currentCharacter = name.charAt(index);
            if(!Character.isLetter(currentCharacter) && !Character.isDigit(currentCharacter) && currentCharacter != '\'' && currentCharacter != '-') {
                return false;
            }
        }
        return true;
    }

    @Override
    public void validate(User user) throws ValidationException {
        String err = new String("");
        if(user.getId() < 0L) {
            err += "[!]Invalid id!\n";
        }
        if(!validName(user.getFirstName())) {
            err += "[!]Invalid first name!\n";
        }
        if(!validName(user.getLastName())) {
            err += "[!]Invalid last name!\n";
        }
        if(err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
