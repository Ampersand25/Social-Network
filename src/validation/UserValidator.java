package validation;

import domain.User;
import exception.ValidationException;

public class UserValidator implements IValidator<User> {
    private boolean validName(String name) {
        if(name.length() == 0) {
            return false;
        }

        char firstCharName = name.charAt(0);
        if(!Character.isDigit(firstCharName) && !Character.isUpperCase(firstCharName)) {
            return false;
        }

        for(int index = 1; index < name.length() - 1; ++index) {
            char currentCharacter = name.charAt(index);
            if(!Character.isLetterOrDigit(currentCharacter) && currentCharacter != '\'' && currentCharacter != '-') {
                return false;
            }
        }

        char lastCharName = name.charAt(name.length() - 1);
        if(!Character.isLetterOrDigit(lastCharName)) {
            return false;
        }

        return true;
    }

    @Override
    public void validate(User user) throws ValidationException {
        String err = new String("");

        if(user.getId() == null) {
            err += "[!]Invalid user id (id must not be null)!\n";
        }
        else if(user.getId() < 0L) {
            err += "[!]Invalid user id (id must be non-negative)!\n";
        }

        if(user.getFirstName() == null) {
            err += "[!]Invalid first name (first name must not be null)!\n";
        }
        else if(!validName(user.getFirstName())) {
            err += "[!]Invalid first name!\n";
        }

        if(user.getLastName() == null) {
            err += "[!]Invalid last name (last name must not be null)!\n";
        }
        else if(!validName(user.getLastName())) {
            err += "[!]Invalid last name!\n";
        }

        if(err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
