package presentation;

import business.UserService;
import domain.User;
import exception.RepoException;
import exception.ServiceException;
import exception.ValidationException;
import org.jetbrains.annotations.NotNull;
import utils.ConsoleColors;

import java.util.Scanner;

public class UI {
    private final UserService userService;

    public UI(UserService userService) {
        this.userService = userService;
    }

    private void printException(String message) {
        System.out.print(ConsoleColors.RED + message + ConsoleColors.RESET);
    }

    private void printUsersMenu() {
        System.out.println("~Users Menu~");
        System.out.println("[0] - back");
        System.out.println("[1] - add new user");
        System.out.println("[2] - modify existing user");
        System.out.println("[3] - remove existing user");
        System.out.println("[4] - search user after id");
        System.out.println("[5] - get the number of existing users");
        System.out.println("[6] - get all existing users");
        System.out.println("*type \"menu\" to display the users menu");
        System.out.println("**type \"exit\" to exit the application");
    }

    private void addUserUI(@NotNull Scanner scanner) {
        System.out.print("Introduce the first name of the new user: ");
        String firstName = scanner.nextLine();
        System.out.print("Introduce the last name of the new user: ");
        String lastName = scanner.nextLine();
        User newUser = new User(firstName, lastName);
        try {
            userService.add(newUser);
            System.out.println("[+]User added with success!");
        } catch (ValidationException | RepoException ex) {
            printException(ex.getMessage());
        }
    }

    private void modifyUserUI(@NotNull Scanner scanner) {
        try {
            System.out.print("Introduce the id of the user you want to modify: ");
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Introduce the new first name of the user you want to modify: ");
            String firstName = scanner.nextLine();
            System.out.print("Introduce the new last name of the user you want to modify: ");
            String lastName = scanner.nextLine();
            User newUser = new User(id, firstName, lastName);
            try {
                User modifiedUser = userService.modify(newUser);
                System.out.println("[&]User modified with success!\nModified user: " + modifiedUser);
            } catch (ValidationException | RepoException ex) {
                printException(ex.getMessage());
            }
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        }
    }

    private void removeUserUI(@NotNull Scanner scanner) {
        System.out.print("Introduce the id of the user you want to remove: ");
        String idString = scanner.nextLine();
        try {
            Long id = Long.parseLong(idString);
            User removedUser = userService.remove(id);
            System.out.println("[-]User removed with success!\nRemoved user: " + removedUser);
        } catch(ServiceException | RepoException ex) {
            printException(ex.getMessage());
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        }
    }

    private void searchUserUI(@NotNull Scanner scanner) {
        System.out.print("Introduce the id of the user you want to search: ");
        String idString = scanner.nextLine();
        try {
            Long id = Long.parseLong(idString);
            User searchedUser = userService.search(id);
            System.out.println("[?]The user with the id " + id + " is: " + searchedUser);
        } catch(ServiceException | RepoException ex) {
            printException(ex.getMessage());
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        }
    }

    private void numberOfUsersUI(Scanner scanner) {
        int numberOfUsers = userService.len();
        if(numberOfUsers == 0) {
            System.out.println("[=]There are no users in the social network!");
        }
        else if(numberOfUsers == 1) {
            System.out.println("[=]There is only one user in the social network!");
        }
        else {
            System.out.println("[=]There are " + userService.len() + " users in the social network!");
        }
    }

    private void getAllUsersUI(Scanner scanner) {
        try {
            Iterable<User> users = userService.getAll();
            int numberOfUsers = userService.len();
            if(numberOfUsers == 1) {
                System.out.println("[*]The only user from the social network is:");
            }
            else{
                System.out.println("[*]The " + numberOfUsers + " users from the social network are:");
            }
            for(User user : users) {
                System.out.println(user);
            }
        } catch(RepoException ex) {
            printException(ex.getMessage());
        }
    }

    private void runUsersMenu() {
        System.out.println();
        printUsersMenu();
        Scanner scanner = new Scanner(System.in);
        String cmd;
        while(true) {
            System.out.print("\n>>>");
            cmd = scanner.nextLine();
            switch(cmd.toLowerCase()) {
                case "0":
                    System.out.println();
                    printMainMenu();
                    return;
                case "1":
                    addUserUI(scanner);
                    break;
                case "2":
                    modifyUserUI(scanner);
                    break;
                case "3":
                    removeUserUI(scanner);
                    break;
                case "4":
                    searchUserUI(scanner);
                    break;
                case "5":
                    numberOfUsersUI(scanner);
                    break;
                case "6":
                    getAllUsersUI(scanner);
                    break;
                case "menu":
                    System.out.println();
                    printUsersMenu();
                    break;
                case "exit":
                    System.exit(0);
                default:
                    printException("[!]Invalid option!\n");
            }
        }
    }

    private void printFriendshipsMenu() {
        System.out.println("~Friendships Menu~");
        System.out.println("[0] - back");
        System.out.println("[1] - add new friendship");
        System.out.println("[2] - modify existing friendship");
        System.out.println("[3] - remove existing friendship");
        System.out.println("[4] - search friendship after id");
        System.out.println("[5] - get the number of existing friendships");
        System.out.println("[6] - get all existing friendships");
        System.out.println("*type \"menu\" to display the friendships menu");
        System.out.println("**type \"exit\" to exit the application");
    }

    private void runFriendshipsMenu() {
        System.out.println();
        printFriendshipsMenu();
        Scanner scanner = new Scanner(System.in);
        String cmd;
        while(true) {
            System.out.print("\n>>>");
            cmd = scanner.nextLine();
            switch(cmd.toLowerCase()) {
                case "0":
                    System.out.println();
                    printMainMenu();
                    return;
                case "1":
                    // TODO
                    break;
                case "2":
                    // TODO
                    break;
                case "3":
                    // TODO
                    break;
                case "4":
                    // TODO
                    break;
                case "5":
                    // TODO
                    break;
                case "6":
                    // TODO
                    break;
                case "menu":
                    System.out.println();
                    printFriendshipsMenu();
                    break;
                case "exit":
                    System.exit(0);
                default:
                    printException("[!]Invalid option!\n");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("~Social Network Application Menu~");
        System.out.println("[0] - exit application");
        System.out.println("[1] - access users menu");
        System.out.println("[2] - access friendships menu");
        System.out.println("*type \"menu\" to display the main menu");
    }

    private void runMainMenu() {
        printMainMenu();
        Scanner scanner = new Scanner(System.in);
        String cmd;
        while(true) {
            System.out.print("\n>>>");
            cmd = scanner.nextLine();
            switch(cmd.toLowerCase()) {
                case "0":
                    System.exit(0);
                case "1":
                    runUsersMenu();
                    break;
                case "2":
                    runFriendshipsMenu();
                    break;
                case "menu":
                    System.out.println();
                    printMainMenu();
                    break;
                default:
                    printException("[!]Invalid option!\n");
            }
        }
    }

    public void run() {
        runMainMenu();
    }
}
