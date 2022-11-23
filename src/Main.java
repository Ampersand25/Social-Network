import domain.Address;
import domain.User;
import domain.Friendship;
import exception.RepoException;
import validation.IValidator;
import validation.AddressValidator;
import validation.UserValidator;
import validation.FriendshipValidator;
import infrastructure.IRepository;
import infrastructure.memory.InMemoryRepo;
import infrastructure.file.UserFileRepo;
import infrastructure.file.FriendshipFileRepo;
import infrastructure.db.FriendshipDbRepository;
import infrastructure.db.UserDbRepository;
import business.UserService;
import business.FriendshipService;
import business.SuperService;
import utils.ConsoleColors;
import utils.Constants;
import presentation.UI;
import test.ApplicationTester;

import java.io.File;
import java.util.Scanner;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // TODO: update documentation (write specifications for the new methods/functions implemented)
        // TODO: fixed possible bugs that may appear when running the app using database repositories
        // TODO: create test functions

        ApplicationTester applicationTester = new ApplicationTester();
        applicationTester.runAllTests();

        System.out.println("Choose how to save the entities (data persistence):");
        System.out.println("[1] - in memory (in RAM - Random Access Memory)");
        System.out.println("[2] - in text files (in CSV - Comma Separated Values files)");
        System.out.println("[3] - in database (in SQL - Structured Query Language database)");

        IRepository<Long, User> userRepo = new InMemoryRepo<>();
        IRepository<Long, Friendship> friendshipRepo = new InMemoryRepo<>();

        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;
        while(keepRunning) {
            keepRunning = false;
            System.out.print("\n>>>");
            String cmd = scanner.nextLine();
            switch(cmd) {
                case "1":
                    break;
                case "2":
                    try {
                        userRepo = new UserFileRepo(Constants.USER_TEXT_FILE_PATH);
                        friendshipRepo = new FriendshipFileRepo(Constants.FRIENDSHIP_TEXT_FILE_PATH, userRepo);
                    } catch(RepoException | IOException ex) {
                        System.out.println("Current directory/folder is: " + new File(".").getAbsoluteFile());
                        ex.printStackTrace();
                        System.exit(1);
                    }
                    break;
                case "3":
                    userRepo = new UserDbRepository(Constants.DATABASE_URL, Constants.DATABASE_USER, Constants.DATABASE_PASSWORD);
                    friendshipRepo = new FriendshipDbRepository(Constants.DATABASE_URL, Constants.DATABASE_USER, Constants.DATABASE_PASSWORD, userRepo);
                    break;
                default:
                    System.out.print(ConsoleColors.RED + "[!]Invalid option!" + ConsoleColors.RESET);
                    keepRunning = true;
            }
            System.out.println();
        }

        IValidator<Address> addressValidator = new AddressValidator();
        IValidator<User> userValidator = new UserValidator(addressValidator);
        UserService userService = new UserService(userValidator, userRepo, friendshipRepo);

        IValidator<Friendship> friendshipValidator = new FriendshipValidator();
        FriendshipService friendshipService = new FriendshipService(friendshipValidator, friendshipRepo, userRepo);

        SuperService superService = new SuperService(userService, friendshipService);
        UI ui = new UI(superService);
        ui.run();
    }
}