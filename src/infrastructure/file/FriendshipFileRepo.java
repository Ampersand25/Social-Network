package infrastructure.file;

import domain.User;
import domain.Friendship;
import exception.RepoException;
import infrastructure.IRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import utils.Constants;

public class FriendshipFileRepo extends AbstractFileRepo<Long, Friendship> {
    private final IRepository<Long, User> userRepo;

    public FriendshipFileRepo(String fileName, IRepository<Long, User> userRepo) throws IOException, RepoException {
        super(fileName, false);
        this.userRepo = userRepo;
        super.loadData();
    }

    @Override
    protected Friendship assembleEntity(@NotNull List<String> attributes) {
        Long id = Long.parseLong(attributes.get(0));
        Long firstFriendId = Long.parseLong(attributes.get(1));
        Long secondFriendId = Long.parseLong(attributes.get(2));
        LocalDateTime friendsFrom = LocalDateTime.parse(attributes.get(3), Constants.DATE_TIME_FORMATTER);

        try {
            return new Friendship(id, userRepo.search(firstFriendId), userRepo.search(secondFriendId), friendsFrom);
        } catch(RepoException ex) {
            System.err.println("[!]Error at searching friends in user repository (there is no user with " + firstFriendId + " id or there is no user with " + secondFriendId + " id)!");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    protected String convertEntityToString(@NotNull Friendship friendship) {
        return friendship.getId().toString() + ";" + friendship.getFirstFriend().getId().toString() + ";" + friendship.getSecondFriend().getId().toString() + ";" + friendship.getFriendsFrom().format(Constants.DATE_TIME_FORMATTER);
    }
}
