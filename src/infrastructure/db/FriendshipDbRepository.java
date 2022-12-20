package infrastructure.db;

import domain.Friendship_Status;
import domain.User;
import domain.Friendship;
import exception.RepoException;
import infrastructure.IRepository;
import utils.FriendshipStatusConverter;

import java.sql.*;
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

        String sqlCommand = "INSERT INTO friendships (id, first_friend_id, second_friend_id, friends_from, friendship_status) VALUES (?, ?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1, friendship.getId());
            statement.setLong(2, friendship.getFirstFriend().getId());
            statement.setLong(3, friendship.getSecondFriend().getId());
            statement.setTimestamp(4, Timestamp.valueOf(friendship.getFriendsFrom()));
            statement.setString(5, FriendshipStatusConverter.convertStatusToString(friendship.getStatus()));
            statement.executeUpdate();
        } catch(SQLException ex) {
            if(ex.getMessage().contains("ERROR: duplicate key value violates unique constraint \"uq_friendships\"\n")) {
                throw new RepoException("[!]Friendship already exists!\n");
            }
            else {
                ex.printStackTrace();
            }
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
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return deletedFriendships;
    }

    @Override
    public Friendship modify(Friendship friendship) throws RepoException, IllegalArgumentException {
        if(friendship == null) {
            throw new RepoException("[!]Invalid friendship (friendship must not be null)!\n");
        }

        Friendship modifiedFriendship = search(friendship.getId());
        String sqlCommand = "UPDATE friendships SET first_friend_id = ?, second_friend_id = ?, friends_from = ?, friendship_status = ? WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1, friendship.getFirstFriend().getId());
            statement.setLong(2, friendship.getSecondFriend().getId());
            statement.setTimestamp(3, Timestamp.valueOf(friendship.getFriendsFrom()));
            statement.setString(4, FriendshipStatusConverter.convertStatusToString(friendship.getStatus()));
            statement.setLong(5, friendship.getId());
            statement.executeUpdate();
        } catch(SQLException ex) {
            if(ex.getMessage().contains("ERROR: duplicate key value violates unique constraint \"uq_friendships\"\n")) {
                throw new RepoException("[!]Friendship already exists!\n");
            }
            else {
                ex.printStackTrace();
            }
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
                LocalDateTime friendsFrom = resultSet.getTimestamp("friends_from").toLocalDateTime();
                Friendship_Status status;
                try {
                    status = FriendshipStatusConverter.convertStringToStatus(resultSet.getString("friendship_status"));
                } catch(Exception ex) {
                    System.err.println("[!]Error at reading friendship status from database (invalid status)!");
                    ex.printStackTrace();
                    return null;
                }

                Friendship searchedFriendships = new Friendship(userRepo.search(firstFriendID), userRepo.search(secondFriendID), friendsFrom, status);
                searchedFriendships.setId(friendshipID);

                return searchedFriendships;
            }
            else {
                throw new RepoException("[!]There is no friendship with the given id in the social network!\n");
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public int len() {
        String sqlCommand = "SELECT COUNT(*) FROM friendships";
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
                LocalDateTime friendsFrom = resultSet.getTimestamp("friends_from").toLocalDateTime();
                Friendship_Status status;
                try {
                    status = FriendshipStatusConverter.convertStringToStatus(resultSet.getString("friendship_status"));
                } catch(Exception ex) {
                    System.err.println("[!]Error at reading friendship status from database (invalid status)!");
                    ex.printStackTrace();
                    return null;
                }

                Friendship friendship = new Friendship(userRepo.search(firstFriendID), userRepo.search(secondFriendID), friendsFrom, status);
                friendship.setId(friendshipID);
                friendships.add(friendship);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return friendships;
    }
}
