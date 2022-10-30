import business.FriendshipService;
import business.UserService;
import domain.Friendship;
import domain.User;
import infrastructure.IRepository;
import infrastructure.memory.InMemoryRepo;
import presentation.UI;
import validation.FriendshipValidator;
import validation.IValidator;
import validation.UserValidator;

public class Main {
    public static void main(String[] args) {
        IValidator<User> userValidator = new UserValidator();
        IRepository<Long, User> userRepo = new InMemoryRepo<>();
        UserService userService = new UserService(userValidator, userRepo);

        IValidator<Friendship> friendshipValidator = new FriendshipValidator();
        IRepository<Long, Friendship> friendshipRepo = new InMemoryRepo<>();
        FriendshipService friendshipService = new FriendshipService(friendshipValidator, friendshipRepo, userRepo);

        UI ui = new UI(userService, friendshipService);
        ui.run();
    }
}