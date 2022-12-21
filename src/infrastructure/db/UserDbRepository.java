package infrastructure.db;

import domain.Address;
import domain.Credential;
import domain.User;
import exception.RepoException;
import infrastructure.IRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

public class UserDbRepository implements IRepository<Long, User> {
    private final String url;
    private final String username;
    private final String password;

    public UserDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(User user) throws RepoException, IllegalArgumentException {
        if(user == null) {
            throw new RepoException("[!]Invalid user (user must not be null)!\n");
        }

        boolean userAlreadyExists = false;
        try {
            search(user.getId());
            userAlreadyExists = true;
        } catch(RepoException ignored) {}
        if(userAlreadyExists) {
            throw new RepoException("[!]User already exists in the social network (there is an user with the given id)!\n");
        }

        String sqlCommand = "INSERT INTO users (id, first_name, last_name, birthday, email, home_address, country, county, city, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1, user.getId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getAddress().getHomeAddress());
            statement.setString(7, user.getAddress().getCountry());
            statement.setString(8, user.getAddress().getCounty());
            statement.setString(9, user.getAddress().getCity());
            statement.setString(10, user.getCredential().getUsername());
            statement.setString(11, user.getCredential().getPassword());
            statement.executeUpdate();
        } catch(SQLException ex) {
            if(ex.getMessage().contains("ERROR: duplicate key value violates unique constraint \"uq_users\"\n")) {
                throw new RepoException("[!]There is already an user in the social network with the given email address!\n");
            }
            else if(ex.getMessage().contains("ERROR: duplicate key value violates unique constraint \"uq_users_username\"\n")) {
                throw new RepoException("[!]There is already an user in the social network with the given username!\n");
            }
            else if(ex.getMessage().contains("ERROR: duplicate key value violates unique constraint \"uq_users_password\"\n")) {
                throw new RepoException("[!]There is already an user in the social network with the given password!\n");
            }
            else {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public User remove(Long userID) throws RepoException, IllegalArgumentException {
        User deletedUser = search(userID);
        String sqlCommand = "DELETE FROM users WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1, userID);
            statement.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return deletedUser;
    }

    @Override
    public User modify(User user) throws RepoException, IllegalArgumentException {
        if(user == null) {
            throw new RepoException("[!]Invalid user (user must not be null)!\n");
        }

        User modifiedUser = search(user.getId());
        String sqlCommand = "UPDATE users SET first_name = ?, last_name = ?, home_address = ?, country = ?, county = ?, city = ?, username = ?, password = ? WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getAddress().getHomeAddress());
            statement.setString(4, user.getAddress().getCountry());
            statement.setString(5, user.getAddress().getCounty());
            statement.setString(6, user.getAddress().getCity());
            statement.setString(7, user.getCredential().getUsername());
            statement.setString(8, user.getCredential().getPassword());
            statement.setLong(9, user.getId());
            statement.executeUpdate();
        } catch(SQLException ex) {
            if(ex.getMessage().contains("ERROR: duplicate key value violates unique constraint \"uq_users_username\"\n")) {
                throw new RepoException("[!]There is already an user in the social network with the given username!\n");
            }
            else if(ex.getMessage().contains("ERROR: duplicate key value violates unique constraint \"uq_users_password\"\n")) {
                throw new RepoException("[!]There is already an user in the social network with the given password!\n");
            }
            else {
                ex.printStackTrace();
            }
        }
        return modifiedUser;
    }

    @Override
    public User search(Long userID) throws RepoException, IllegalArgumentException {
        if(userID == null) {
            throw new IllegalArgumentException("[!]Id must not be null!\n");
        }
        else if(userID < 0L) {
            throw new IllegalArgumentException("[!]Id must be a non-negative integer!\n");
        }

        if(len() == 0) {
            throw new RepoException("[!]There are no users in the social network!\n");
        }

        String sqlCommand = "SELECT * FROM users WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
                String email = resultSet.getString("email");

                String homeAddress = resultSet.getString("home_address");
                String country = resultSet.getString("country");
                String county = resultSet.getString("county");
                String city = resultSet.getString("city");
                Address address = new Address(homeAddress, country, county, city);

                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Credential credential = new Credential(username, password);

                User searchedUser = new User(firstName, lastName, birthday, email, address, credential);
                searchedUser.setId(userID);

                return searchedUser;
            }
            else {
                throw new RepoException("[!]There is no user with the given id in the social network!\n");
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public int len() {
        String sqlCommand = "SELECT COUNT(*) FROM users";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public Iterable<User> getAll() throws RepoException {
        if(len() == 0) {
            throw new RepoException("[!]There are no users in the social network!\n");
        }

        Set<User> users = new HashSet<>();
        String sqlCommand = "SELECT * FROM users";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Long userID = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
                String email = resultSet.getString("email");

                String homeAddress = resultSet.getString("home_address");
                String country = resultSet.getString("country");
                String county = resultSet.getString("county");
                String city = resultSet.getString("city");
                Address address = new Address(homeAddress, country, county, city);

                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Credential credential = new Credential(username, password);

                User user = new User(firstName, lastName, birthday, email, address, credential);
                user.setId(userID);
                users.add(user);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }
}
