package validation;

import domain.User;
import exception.ValidationException;
import utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class UserValidator implements IValidator<User> {
    private int validName(@NotNull String name) {
        int nameLength = name.length();
        if(nameLength < 1) {
            return 1;
        }
        if(nameLength > 100) {
            return 2;
        }

        char firstCharacterName = name.charAt(0);
        if(!Character.isDigit(firstCharacterName) && !Character.isUpperCase(firstCharacterName)) {
            return 3;
        }

        for(int index = 1; index < nameLength - 1; ++index) {
            char currentCharacter = name.charAt(index);
            if(!Character.isLetterOrDigit(currentCharacter) && !Constants.VALID_NAME_SPECIAL_CHARACTERS.contains(currentCharacter)) {
                return 4;
            }
            else if(Constants.VALID_NAME_SPECIAL_CHARACTERS.contains(currentCharacter)) {
                char previousCharacter = name.charAt(index - 1);
                if(Constants.VALID_NAME_SPECIAL_CHARACTERS.contains(previousCharacter)) {
                    return 5;
                }
            }
        }

        char lastCharacterName = name.charAt(nameLength - 1);
        if(!Character.isLetterOrDigit(lastCharacterName)) {
            return 6;
        }

        return 0;
    }

    private boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    @Override
    public void validate(@NotNull User user) throws ValidationException {
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
        else {
            err += switch(validName(userFirstName)) {
                case 1 -> "[!]Invalid first name (first name is too short)!\n";
                case 2 -> "[!]Invalid first name (first name is too long)!\n";
                case 3 -> "[!]Invalid first name (first name must start with capital letter or digit)!\n";
                case 4 -> "[!]Invalid first name (first name contains invalid characters)!\n";
                case 5 -> "[!]Invalid first name (first name contains too many consecutive special characters)!\n";
                case 6 -> "[!]Invalid first name (first name must end with letter or digit)!\n";
                default -> "";
            };
        }

        String userLastName = user.getLastName();
        if(userLastName == null) {
            err += "[!]Invalid last name (last name must not be null)!\n";
        }
        else {
            err += switch(validName(userLastName)) {
                case 1 -> "[!]Invalid last name (last name is too short)!\n";
                case 2 -> "[!]Invalid last name (last name is too long)!\n";
                case 3 -> "[!]Invalid last name (last name must start with capital letter or digit)!\n";
                case 4 -> "[!]Invalid last name (last name contains invalid characters)!\n";
                case 5 -> "[!]Invalid last name (last name contains too many consecutive special characters)!\n";
                case 6 -> "[!]Invalid last name (last name must end with letter or digit)!\n";
                default -> "";
            };
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

        if(!patternMatches(user.getEmail(), Constants.VALID_EMAIL_REGEX)) {
            err += "[!]Invalid email address!\n";
        }

        if(err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
