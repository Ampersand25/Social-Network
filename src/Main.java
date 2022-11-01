import domain.User;
import domain.Friendship;
import validation.IValidator;
import validation.UserValidator;
import validation.FriendshipValidator;
import infrastructure.IRepository;
import infrastructure.memory.InMemoryRepo;
import business.UserService;
import business.FriendshipService;
import business.SuperService;
import presentation.UI;

public class Main {
    public static void main(String[] args) {
        // TODO: add list of friends to each user
        // TODO: add date when user was created
        // TODO: replace for instructions with streams operations
        // TODO: write documentation
        // TODO: create test functions
        // TODO: update user validation

        IValidator<User> userValidator = new UserValidator();
        IRepository<Long, User> userRepo = new InMemoryRepo<>();
        UserService userService = new UserService(userValidator, userRepo);

        IValidator<Friendship> friendshipValidator = new FriendshipValidator();
        IRepository<Long, Friendship> friendshipRepo = new InMemoryRepo<>();
        FriendshipService friendshipService = new FriendshipService(friendshipValidator, friendshipRepo, userRepo);

        SuperService superService = new SuperService(userService, friendshipService);
        UI ui = new UI(superService);
        ui.run();
    }
}