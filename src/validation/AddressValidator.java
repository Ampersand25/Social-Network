package validation;

import domain.Address;
import exception.ValidationException;

import org.jetbrains.annotations.NotNull;
import utils.Constants;

public class AddressValidator implements IValidator<Address> {
    private int validAddressName(@NotNull String addressName) {
        int addressNameLength = addressName.length();
        if(addressNameLength > 50) {
            return 1;
        }

        char firstCharacterAddressName = addressName.charAt(0);
        if(!Character.isUpperCase(firstCharacterAddressName)) {
            return 2;
        }

        for(int index = 1; index < addressNameLength - 1; ++index) {
            char currentCharacter = addressName.charAt(index);
            if(!Character.isLetter(currentCharacter) && !Constants.VALID_ADDRESS_NAME_SPECIAL_CHARACTERS.contains(currentCharacter)) {
                return 3;
            }
            else if(Constants.VALID_ADDRESS_NAME_SPECIAL_CHARACTERS.contains(currentCharacter)) {
                char previousCharacter = addressName.charAt(index - 1);
                if(Constants.VALID_ADDRESS_NAME_SPECIAL_CHARACTERS.contains(previousCharacter)) {
                    return 4;
                }
            }
        }

        char lastCharacterName = addressName.charAt(addressNameLength - 1);
        if(!Character.isLetter(lastCharacterName)) {
            return 5;
        }

        return 0;
    }

    @Override
    public void validate(@NotNull Address address) throws ValidationException {
        String err = new String("");

        String homeAddress = address.getHomeAddress();
        if(homeAddress == null) {
            err += "[!]Invalid home address (home address must not be null)\n";
        }
        else if(homeAddress.length() == 0) {
            err += "[!]Invalid home address (home address name is too short: minimum limit of characters is 4)!\n";
        }
        else if(homeAddress.contains(";")) {
            err += "[!]Invalid home address (home address name must not contains ';' character)!\n";
        }

        String country = address.getCountry();
        if(country == null) {
            err += "[!]Invalid country (country must not be null)\n";
        }
        else if(country.length() < 4) {
            err += "[!]Invalid country (country name is too short: minimum limit of characters is 4)!\n";
        }
        else {
            err += switch(validAddressName(country)) {
                case 1 -> "[!]Invalid country (country name is too long: exceeds 50 character limit)!\n";
                case 2 -> "[!]Invalid country (country name must start with a capital letter)!\n";
                case 3 -> "[!]Invalid country (country name contains invalid characters (valid special characters are ' ' and '-'))!\n";
                case 4 -> "[!]Invalid country (country name contains too many consecutive special characters)!\n";
                case 5 -> "[!]Invalid country (country name must end with a letter)!\n";
                default -> "";
            };
        }

        String county = address.getCounty();
        if(county == null) {
            err += "[!]Invalid county (county must not be null)\n";
        }
        else if(county.length() == 0) {
            err += "[!]Invalid county (county name is too short: minimum limit of characters is 4)!\n";
        }
        else {
            err += switch(validAddressName(county)) {
                case 1 -> "[!]Invalid county (county name is too long: exceeds 50 character limit)!\n";
                case 2 -> "[!]Invalid county (county name must start with a capital letter)!\n";
                case 3 -> "[!]Invalid county (county name contains invalid characters (valid special characters are ' ' and '-'))!\n";
                case 4 -> "[!]Invalid county (county name contains too many consecutive special characters)!\n";
                case 5 -> "[!]Invalid county (county name must end with a letter)!\n";
                default -> "";
            };
        }

        String city = address.getCity();
        if(city == null) {
            err += "[!]Invalid city (city must not be null)\n";
        }
        else if(city.length() == 0) {
            err += "[!]Invalid city (city name is too short: minimum limit of characters is 4)!\n";
        }
        else {
            err += switch(validAddressName(city)) {
                case 1 -> "[!]Invalid city (city name is too long: exceeds 50 character limit)!\n";
                case 2 -> "[!]Invalid city (city name must start with a capital letter)!\n";
                case 3 -> "[!]Invalid city (city name contains invalid characters (valid special characters are ' ' and '-'))!\n";
                case 4 -> "[!]Invalid city (city name contains too many consecutive special characters)!\n";
                case 5 -> "[!]Invalid city (city name must end with a letter)!\n";
                default -> "";
            };
        }

        if(err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
