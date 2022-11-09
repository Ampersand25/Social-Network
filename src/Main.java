import domain.Address;
import domain.User;
import domain.Friendship;
import exception.RepoException;
import infrastructure.file.UserFileRepo;
import infrastructure.file.FriendshipFileRepo;
import utils.ConsoleColors;
import utils.Constants;
import validation.IValidator;
import validation.AddressValidator;
import validation.UserValidator;
import validation.FriendshipValidator;
import infrastructure.IRepository;
import infrastructure.memory.InMemoryRepo;
import business.UserService;
import business.FriendshipService;
import business.SuperService;
import presentation.UI;
import test.ApplicationTester;

import java.io.File;
import java.util.Scanner;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // TODO: create file repositories for the entities
        // TODO: create database repositories for the entities
        // TODO: create test functions

        ApplicationTester applicationTester = new ApplicationTester();
        applicationTester.runAllTests();

        System.out.println("Alegeti cum sa se faca salvarea datelor:");
        System.out.println("[1] - in memorie (in memoria RAM - Random Access Memory)");
        System.out.println("[2] - in fisiere text (fisiere CSV - Comma Separated Values)");
        System.out.println("[3] - in baza de date (baza de date SQL - Structured Query Language)");

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
                    System.out.print(ConsoleColors.PURPLE + "[x]Option currently unavailable!" + ConsoleColors.RESET);
                    keepRunning = true;
                    break;
                default:
                    System.out.print(ConsoleColors.RED + "[!]Invalid option!" + ConsoleColors.RESET);
                    keepRunning = true;
            }
            System.out.println();
        }

        IValidator<Address> addressValidator = new AddressValidator();
        IValidator<User> userValidator = new UserValidator(addressValidator);
        UserService userService = new UserService(userValidator, userRepo);

        IValidator<Friendship> friendshipValidator = new FriendshipValidator();
        FriendshipService friendshipService = new FriendshipService(friendshipValidator, friendshipRepo, userRepo);

        SuperService superService = new SuperService(userService, friendshipService);
        UI ui = new UI(superService);
        ui.run();
    }
}