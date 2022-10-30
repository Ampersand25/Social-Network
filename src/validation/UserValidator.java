package validation;

import domain.User;
import exception.ValidationException;

import java.time.LocalDate;

public class UserValidator implements IValidator<User> {
    private boolean validName(String name) {
        int nameLength = name.length();
        if(nameLength == 0) {
            return false;
        }

        char firstCharacterName = name.charAt(0);
        if(!Character.isDigit(firstCharacterName) && !Character.isUpperCase(firstCharacterName)) {
            return false;
        }

        for(int index = 1; index < nameLength - 1; ++index) {
            char currentCharacter = name.charAt(index);
            if(!Character.isLetterOrDigit(currentCharacter) && currentCharacter != '\'' && currentCharacter != '-') {
                return false;
            }
        }

        char lastCharacterName = name.charAt(nameLength - 1);
        if(!Character.isLetterOrDigit(lastCharacterName)) {
            return false;
        }

        return true;
    }

    @Override
    public void validate(User user) throws ValidationException {
        String err = new String("");

        Long userId = user.getId();
        if(userId == null) {
            err += "[!]Invalid user id (id must not be null)!\n";
        }
        else if(userId < 0L) {
            err += "[!]Invalid user id (id must be non-negative)!\n";
        }

        String userFirstName = user.getFirstName();
        if(userFirstName == null) {
            err += "[!]Invalid first name (first name must not be null)!\n";
        }
        else if(!validName(userFirstName)) {
            err += "[!]Invalid first name!\n";
        }

        String userLastName = user.getLastName();
        if(userLastName == null) {
            err += "[!]Invalid last name (last name must not be null)!\n";
        }
        else if(!validName(userLastName)) {
            err += "[!]Invalid last name!\n";
        }

        LocalDate userBirthday = user.getBirthday();
        if(userBirthday == null) {
            err += "[!]Invalid birthday (birthday must not be null)!\n";
        }
        else if(userBirthday.isAfter(LocalDate.now())) {
            err += "[!]Invalid birthday (birthday must not be in the future)!\n";
        }
        else if(userBirthday.isAfter(LocalDate.now().minusYears(3))) {
            err += "[!]Invalid birthday (user must not be younger than 3 years old)!\n";
        }
        else if(userBirthday.isBefore(LocalDate.now().minusYears(120))) {
            err += "[!]Invalid birthday (user must not be older than 120 years old)!\n";
        }

        if(err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
