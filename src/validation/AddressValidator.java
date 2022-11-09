package validation;

import domain.Address;
import exception.ValidationException;

import org.jetbrains.annotations.NotNull;

public class AddressValidator implements IValidator<Address> {
    @Override
    public void validate(@NotNull Address address) throws ValidationException {
        String err = new String("");

        String homeAddress = address.getHomeAddress();
        if(homeAddress == null) {
            err += "[!]Invalid home address (home address must not be null)\n";
        }
        else if(homeAddress.length() == 0) {
            err += "[!]Invalid home address (home address is too short)!\n";
        }

        String country = address.getCountry();
        if(country == null) {
            err += "[!]Invalid country (country must not be null)\n";
        }
        else if(country.length() == 0) {
            err += "[!]Invalid country (country is too short)!\n";
        }

        String county = address.getCounty();
        if(county == null) {
            err += "[!]Invalid county (county must not be null)\n";
        }
        else if(county.length() == 0) {
            err += "[!]Invalid county (county is too short)!\n";
        }

        String city = address.getCity();
        if(city == null) {
            err += "[!]Invalid city (city must not be null)\n";
        }
        else if(city.length() == 0) {
            err += "[!]Invalid city (city is too short)!\n";
        }

        if(err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
