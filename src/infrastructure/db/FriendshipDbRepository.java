package infrastructure.db;

import domain.User;
import domain.Friendship;
import exception.RepoException;
import infrastructure.IRepository;

import java.sql.*;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

public class FriendshipDbRepository implements IRepository<Long, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private final IRepository<Long, User> userRepo;

    public FriendshipDbRepository(String url, String username, String password, IRepository<Long, User> userRepo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userRepo = userRepo;
    }

    @Override
    public void add(Friendship friendship) throws RepoException, IllegalArgumentException {
        if(friendship == null) {
            throw new RepoException("[!]Invalid friendships (friendship must not be null)!\n");
        }

        boolean friendshipsAlreadyExists = false;
        try {
            search(friendship.getId());
            friendshipsAlreadyExists = true;
        } catch(RepoException ignored) {}
        if(friendshipsAlreadyExists) {
            throw new RepoException("[!]Friendship already exists in the social network (there is a friendship with the given id)!\n");
        }

        String sqlCommand = "INSERT INTO friendships (id, first_friend_id, second_friend_id, friends_from) VALUES (?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1, friendship.getId());
            statement.setLong(2, friendship.getFirstFriend().getId());
            statement.setLong(3, friendship.getSecondFriend().getId());
            statement.setDate(4, java.sql.Date.valueOf(friendship.getFriendsFrom().toLocalDate()));
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Friendship remove(Long friendshipID) throws RepoException, IllegalArgumentException {
        Friendship deletedFriendships = search(friendshipID);
        String sqlCommand = "DELETE FROM friendships WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1, friendshipID);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return deletedFriendships;
    }

    @Override
    public Friendship modify(Friendship friendship) throws RepoException, IllegalArgumentException {
        if(friendship == null) {
            throw new RepoException("[!]Invalid friendship (friendship must not be null)!\n");
        }

        Friendship modifiedFriendship = search(friendship.getId());
        String sqlCommand = "UPDATE friendships SET first_friend_id = ?, second_friend_id = ?, friends_from = ? WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1, friendship.getFirstFriend().getId());
            statement.setLong(2, friendship.getSecondFriend().getId());
            statement.setDate(3, java.sql.Date.valueOf(friendship.getFriendsFrom().toLocalDate()));
            statement.setLong(4, friendship.getId());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return modifiedFriendship;
    }

    @Override
    public Friendship search(Long friendshipID) throws RepoException, IllegalArgumentException {
        if(friendshipID == null) {
            throw new IllegalArgumentException("[!]Id must not be null!\n");
        }
        else if(friendshipID < 0L) {
            throw new IllegalArgumentException("[!]Id must be a non-negative integer!\n");
        }

        if(len() == 0) {
            throw new RepoException("[!]There are no friendships in the social network!\n");
        }

        String sqlCommand = "SELECT * FROM friendships WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1, friendshipID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Long firstFriendID = resultSet.getLong("first_friend_id");
                Long secondFriendID = resultSet.getLong("second_friend_id");
                LocalDateTime friendsFrom = resultSet.getDate("friends_from").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                Friendship searchedFriendships = new Friendship(userRepo.search(firstFriendID), userRepo.search(secondFriendID), friendsFrom);
                searchedFriendships.setId(friendshipID);

                return searchedFriendships;
            }
            else {
                throw new RepoException("[!]There is no friendship with the given id in the social network!\n");
            }
        } catch(SQLException e) {
            e.printStackTrace();
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
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Iterable<Friendship> getAll() throws RepoException {
        if(len() == 0) {
            throw new RepoException("[!]There are no friendships in the social network!\n");
        }

        Set<Friendship> friendships = new HashSet<>();
        String sqlCommand = "SELECT * FROM friendships";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Long friendshipID = resultSet.getLong("id");
                Long firstFriendID = resultSet.getLong("first_friend_id");
                Long secondFriendID = resultSet.getLong("second_friend_id");
                LocalDateTime friendsFrom = resultSet.getDate("friends_from").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                Friendship friendship = new Friendship(userRepo.search(firstFriendID), userRepo.search(secondFriendID), friendsFrom);
                friendship.setId(friendshipID);
                friendships.add(friendship);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }
}
