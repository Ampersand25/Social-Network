import business.FriendshipService;
import business.UserService;
import domain.Friendship;
import domain.User;
import exception.RepoException;
import exception.ValidationException;
import infrastructure.IRepository;
import infrastructure.memory.InMemoryRepo;
import presentation.UI;
import validation.FriendshipValidator;
import validation.IValidator;
import validation.UserValidator;

public class Main {
    public static void addDebugUsers(UserService userService) {
        User user1  = new User("John", "O'Brien");
        User user2  = new User("William", "Day");
        User user3  = new User("Charles", "Kelly");
        User user4  = new User("Donald", "Castaneda");
        User user5  = new User("Charles-Mike", "Lam");
        User user6  = new User("Robert", "Beck");
        User user7  = new User("O'Mikel", "Rowe");
        User user8  = new User("Donald", "Dotson");
        User user9  = new User("Joseph", "Tucker-Doyle");
        User user10 = new User("Robert", "O'Gallagher");

        try{
            userService.add(user1);
            userService.add(user2);
            userService.add(user3);
            userService.add(user4);
            userService.add(user5);
            userService.add(user6);
            userService.add(user7);
            userService.add(user8);
            userService.add(user9);
            userService.add(user10);
        } catch(ValidationException | RepoException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        IValidator<User> userValidator = new UserValidator();
        IRepository<Long, User> userRepo = new InMemoryRepo<>();
        UserService userService = new UserService(userValidator, userRepo);

        IValidator<Friendship> friendshipValidator = new FriendshipValidator();
        IRepository<Long, Friendship> friendshipRepo = new InMemoryRepo<>();
        FriendshipService friendshipService = new FriendshipService(friendshipValidator, friendshipRepo, userRepo);

        addDebugUsers(userService);

        UI ui = new UI(userService, friendshipService);
        ui.run();
    }
}