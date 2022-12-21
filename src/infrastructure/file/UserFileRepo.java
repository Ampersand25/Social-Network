package infrastructure.file;

import domain.Address;
import domain.Credential;
import domain.User;
import exception.RepoException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import utils.Constants;

public class UserFileRepo extends AbstractFileRepo<Long, User> {
    public UserFileRepo(String fileName) throws IOException, RepoException {
        super(fileName, true);
    }

    @Override
    protected User assembleEntity(@NotNull List<String> attributes) {
        Long id = Long.parseLong(attributes.get(0));
        String firstName = attributes.get(1);
        String lastName = attributes.get(2);
        LocalDate birthday = LocalDate.parse(attributes.get(3), Constants.DATE_FORMATTER);
        String email = attributes.get(4);
        List<User> friendList = new ArrayList<>();

        String homeAddress = attributes.get(5);
        String country = attributes.get(6);
        String county = attributes.get(7);
        String city = attributes.get(8);
        Address address = new Address(homeAddress, country, county, city);

        String username = attributes.get(9);
        String password = attributes.get(10);
        Credential credential = new Credential(username, password);

        return new User(id, firstName, lastName, birthday, email, address, credential);
    }

    @Override
    protected String convertEntityToString(@NotNull User user) {
        return user.getId().toString() + ";" + user.getFirstName() + ";" + user.getLastName() + ";" + user.getBirthday().format(Constants.DATE_FORMATTER) + ";" + user.getEmail() + ";" + user.getAddress().getHomeAddress() + ";" + user.getAddress().getCountry() + ";" + user.getAddress().getCounty() + ";" + user.getAddress().getCity() + ";" + user.getCredential().getUsername() + ";" + user.getCredential().getPassword();
    }
}
