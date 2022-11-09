package infrastructure.file;

import domain.Address;
import domain.User;
import exception.RepoException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class UserFileRepo extends AbstractFileRepo<Long, User> {
    public UserFileRepo(String fileName) throws IOException, RepoException {
        super(fileName);
    }

    @Override
    public User assembleEntity(@NotNull List<String> attributes) {
        Long id = Long.parseLong(attributes.get(0));
        String firstName = attributes.get(1);
        String lastName = attributes.get(2);
        LocalDate birthday = LocalDate.parse(attributes.get(3));
        String email = attributes.get(4);
        List<User> friendList = new ArrayList<>();
        String homeAddress = attributes.get(5);
        String country = attributes.get(6);
        String county = attributes.get(7);
        String city = attributes.get(8);
        Address address = new Address(homeAddress, country, county, city);
        return new User(id, firstName, lastName, birthday, email, address);
    }

    @Override
    public String convertEntityToString(@NotNull User user) {
        return user.getId().toString() + ";" + user.getFirstName() + ";" + user.getLastName() + ";" + user.getBirthday().toString() + ";" + user.getEmail() + ";" + user.getAddress().getHomeAddress() + ";" + user.getAddress().getCountry() + ";" + user.getAddress().getCounty() + ";" + user.getAddress().getCity();
    }
}
