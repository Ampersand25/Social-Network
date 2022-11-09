import domain.User;
import domain.Friendship;
import test.ApplicationTester;
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
        // TODO: add Address entity in the application
        // TODO: incapsulate Address entity as private attribute in User entity
        // TODO: create file repositories for the entities
        // TODO: create database repositories for the entities
        // TODO: create test functions

        ApplicationTester applicationTester = new ApplicationTester();
        applicationTester.runAllTests();

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