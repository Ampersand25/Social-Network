package presentation;

import domain.Friendship;
import domain.User;
import exception.ValidationException;
import exception.RepoException;
import exception.ServiceException;
import business.SuperService;
import utils.ConsoleColors;
import utils.Constants;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.jetbrains.annotations.NotNull;

public class UI {
    private final SuperService superService;

    public UI(SuperService superService) {
        this.superService = superService;
    }

    private void printException(String message) {
        System.out.print(ConsoleColors.RED + message + ConsoleColors.RESET);
    }

    private void printSuccessMessage(String message) {
        System.out.println(ConsoleColors.GREEN + message + ConsoleColors.RESET);
    }

    private void printUsersMenu() {
        System.out.println("~Users Menu~");
        System.out.println("[0] - back");
        System.out.println("[1] - add new user");
        System.out.println("[2] - remove existing user");
        System.out.println("[3] - modify existing user");
        System.out.println("[4] - search user after id");
        System.out.println("[5] - get the number of existing users");
        System.out.println("[6] - get all existing users");
        System.out.println("[7] - add 10 users in the social network");
        System.out.println("*type \"menu\" to display the users menu");
        System.out.println("**type \"exit\" to exit the application");
    }

    private void addUsersDebug() {
        try{
            superService.addUser("John", "O'Brien", LocalDate.of(1998, 10, 15));
            superService.addUser("William", "Day", LocalDate.of(1988, 12, 30));
            superService.addUser("Charles", "Kelly", LocalDate.of(1996, 3, 13));
            superService.addUser("Donald", "Castaneda", LocalDate.of(1998, 7, 4));
            superService.addUser("Charles-Mike", "Lam", LocalDate.of(1986, 4, 15));
            superService.addUser("Robert", "Beck", LocalDate.of(1990, 7, 23));
            superService.addUser("O'Mikel", "Rowe", LocalDate.of(1984, 3, 20));
            superService.addUser("Donald", "Dotson", LocalDate.of(1989, 9, 5));
            superService.addUser("Joseph", "Tucker-Doyle", LocalDate.of(2001, 1, 22));
            superService.addUser("Robert", "O'Gallagher", LocalDate.of(1994, 12, 6));

            printSuccessMessage("[+]All 10 users added with success!");
        } catch(ValidationException | RepoException ex) {
            ex.printStackTrace();
        }
    }

    private void addUserUI(@NotNull Scanner scanner) {
        System.out.print("Introduce the first name of the new user: ");
        String firstName = scanner.nextLine();

        System.out.print("Introduce the last name of the new user: ");
        String lastName = scanner.nextLine();

        try {
            System.out.print("Introduce the birthday of the new user: ");
            LocalDate birthday = LocalDate.parse(scanner.nextLine(), Constants.DATE_TIME_FORMATTER);

            superService.addUser(firstName, lastName, birthday);
            printSuccessMessage("[+]User added with success!");
        } catch (ValidationException | RepoException ex) {
            printException(ex.getMessage());
        } catch(DateTimeParseException ex) {
            printException("[!]Invalid date (the format of the date must be \"yyyy-MM-dd\")!\n");
        }
    }

    private void removeUserUI(@NotNull Scanner scanner) {
        try {
            System.out.print("Introduce the id of the user you want to remove: ");
            Long userId = Long.parseLong(scanner.nextLine());

            User removedUser = superService.removeUser(userId);
            printSuccessMessage("[-]User removed with success!\nRemoved user: " + removedUser);
        } catch(RepoException | ServiceException ex) {
            printException(ex.getMessage());
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        }
    }

    private void modifyUserUI(@NotNull Scanner scanner) {
        try {
            System.out.print("Introduce the id of the user you want to modify: ");
            Long userId = Long.parseLong(scanner.nextLine());

            System.out.print("Introduce the new first name of the user you want to modify: ");
            String firstName = scanner.nextLine();

            System.out.print("Introduce the new last name of the user you want to modify: ");
            String lastName = scanner.nextLine();

            User modifiedUser = superService.modifyUser(userId, firstName, lastName);
            printSuccessMessage("[&]User modified with success!\nModified user: " + modifiedUser);
        } catch (ValidationException | RepoException ex) {
            printException(ex.getMessage());
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        }
    }

    private void searchUserUI(@NotNull Scanner scanner) {
        try {
            System.out.print("Introduce the id of the user you want to search: ");
            Long userId = Long.parseLong(scanner.nextLine());

            User searchedUser = superService.searchUser(userId);
            printSuccessMessage("[?]The user with the id " + userId + " is: " + searchedUser);
        } catch(RepoException | ServiceException ex) {
            printException(ex.getMessage());
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        }
    }

    private void numberOfUsersUI() {
        int numberOfUsers = superService.numberOfUsers();
        if(numberOfUsers == 0) {
            printSuccessMessage("[#]There are no users in the social network!");
        }
        else if(numberOfUsers == 1) {
            printSuccessMessage("[#]There is only one user in the social network!");
        }
        else {
            printSuccessMessage("[#]There are " + numberOfUsers + " users in the social network!");
        }
    }

    private void getAllUsersUI() {
        try {
            Iterable<User> users = superService.getAllUsers();

            int numberOfUsers = superService.numberOfUsers();
            if(numberOfUsers == 1) {
                printSuccessMessage("[*]The only user from the social network is:");
            }
            else{
                printSuccessMessage("[*]The " + numberOfUsers + " users from the social network are:");
            }

            for(User user : users) {
                //System.out.println(user);
                printSuccessMessage(user.toString());
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
                    removeUserUI(scanner);
                    break;
                case "3":
                    modifyUserUI(scanner);
                    break;
                case "4":
                    searchUserUI(scanner);
                    break;
                case "5":
                    numberOfUsersUI();
                    break;
                case "6":
                    getAllUsersUI();
                    break;
                case "7":
                    addUsersDebug();
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
        System.out.println("[2] - remove existing friendship");
        System.out.println("[3] - modify existing friendship");
        System.out.println("[4] - search friendship after id");
        System.out.println("[5] - get the number of existing friendships");
        System.out.println("[6] - get all existing friendships");
        System.out.println("[7] - print the number of communities");
        System.out.println("[8] - print the most sociable community");
        System.out.println("[9] - get all communities from the social network");
        System.out.println("*type \"menu\" to display the friendships menu");
        System.out.println("**type \"exit\" to exit the application");
    }

    private void addFriendshipUI(@NotNull Scanner scanner) {
        try{
            System.out.print("Introduce the id of the first friend: ");
            Long firstFriendId = Long.parseLong(scanner.nextLine());

            System.out.print("Introduce the id of the second friend: ");
            Long secondFriendId = Long.parseLong(scanner.nextLine());

            superService.addFriendship(firstFriendId, secondFriendId);
            printSuccessMessage("[+]Friendship added with success!");
        } catch (ValidationException | RepoException | ServiceException ex) {
            printException(ex.getMessage());
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        }
    }

    private void removeFriendshipUI(@NotNull Scanner scanner) {
        try {
            System.out.print("Introduce the id of the friendship you want to remove: ");
            Long friendshipId = Long.parseLong(scanner.nextLine());

            Friendship removedFriendship = superService.removeFriendship(friendshipId);
            printSuccessMessage("[-]Friendship removed with success!\nRemoved friendship:\n" + removedFriendship);
        } catch(RepoException | ServiceException ex) {
            printException(ex.getMessage());
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        }
    }

    private void modifyFriendshipUI(@NotNull Scanner scanner) {
        try {
            System.out.print("Introduce the id of the friendship you want to modify: ");
            Long friendshipId = Long.parseLong(scanner.nextLine());

            System.out.print("Introduce the id of the new first friend: ");
            Long firstFriendId = Long.parseLong(scanner.nextLine());

            System.out.print("Introduce the id of the new second friend: ");
            Long secondFriendId = Long.parseLong(scanner.nextLine());

            System.out.print("Introduce the date when the friendship was created: ");
            LocalDate date = LocalDate.parse(scanner.nextLine(), Constants.DATE_TIME_FORMATTER);

            Friendship modifiedFriendship = superService.modifyFriendship(friendshipId, firstFriendId, secondFriendId, date);
            printSuccessMessage("[&]Friendship modified with success!\nModified friendship:\n" + modifiedFriendship);
        } catch (ValidationException | RepoException ex) {
            printException(ex.getMessage());
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        } catch(DateTimeParseException ex) {
            printException("[!]Invalid date (the format of the date must be \"yyyy-MM-dd\")!\n");
        }
    }

    private void searchFriendshipUI(@NotNull Scanner scanner) {
        try {
            System.out.print("Introduce the id of the friendship you want to search: ");
            Long friendshipId = Long.parseLong(scanner.nextLine());

            Friendship searchedFriendship = superService.searchFriendship(friendshipId);
            printSuccessMessage("[?]The friendship with the id " + friendshipId + " is:\n" + searchedFriendship);
        } catch(RepoException | ServiceException ex) {
            printException(ex.getMessage());
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        }
    }

    private void numberOfFriendshipsUI() {
        int numberOfFriendships = superService.numberOfFriendships();
        if(numberOfFriendships == 0) {
            printSuccessMessage("[#]There are no friendships in the social network!");
        }
        else if(numberOfFriendships == 1) {
            printSuccessMessage("[#]There is only one friendship in the social network!");
        }
        else {
            printSuccessMessage("[#]There are " + numberOfFriendships + " friendships in the social network!");
        }
    }

    private void getAllFriendshipsUI() {
        try {
            Iterable<Friendship> friendships = superService.getAllFriendships();

            int numberOfFriendships = superService.numberOfFriendships();
            if(numberOfFriendships == 1) {
                printSuccessMessage("[*]The only friendship from the social network is:");
            }
            else{
                printSuccessMessage("[*]The " + numberOfFriendships + " friendships from the social network are:");
            }

            int numberOfDisplayedFriendships = 0;
            for(Friendship friendship : friendships) {
                if(numberOfDisplayedFriendships++ > 0) {
                    System.out.println();
                }
                //System.out.println(friendship);
                printSuccessMessage(friendship.toString());
            }
        } catch(RepoException ex) {
            printException(ex.getMessage());
        }
    }

    void printNumberOfCommunitiesUI() {
        int communities = superService.numberOfCommunities();
        if(communities == 0) {
            printSuccessMessage("[=]There are no communities in the social network!");
        }
        else if(communities == 1) {
            printSuccessMessage("[=]There is only one community in the social network!");
        }
        else {
            printSuccessMessage("[=]There are " + communities + " communities in the social network!");
        }
    }

    void getAllCommunitiesUI() {
        try {
            List<List<Long>> communities = superService.getAllCommunities();

            int numberOfCommunities = communities.size();
            if(numberOfCommunities == 1) {
                printSuccessMessage("The only community from the social network is:");
            }
            else {
                printSuccessMessage("All " + numberOfCommunities + " communities from the social network are:");
            }

            for(int i = 0; i < numberOfCommunities; ++i) {
                if(i != 0) {
                    System.out.println();
                }

                if(numberOfCommunities != 1) {
                    printSuccessMessage("Community #" + (i + 1) + " is:");
                }

                int currentCommunitySize = communities.get(i).size();
                for(int j = 0; j < currentCommunitySize; ++j) {
                    try {
                        printSuccessMessage(superService.searchUser(communities.get(i).get(j)).toString());
                    } catch(RepoException | ServiceException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch(RepoException ex) {
            try {
                Iterable<User> users = superService.getAllUsers();
                printSuccessMessage("All " + superService.numberOfUsers() + " communities from the social network are:");

                int i = 0;
                for(User user : users) {
                    if(i != 0) {
                        System.out.println();
                    }

                    printSuccessMessage("Community #" + ++i + " is:\n" + user.toString());
                }
            } catch (RepoException e) {
                printException("[!]There are no users in the social network!\n");
            }
        }
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
                    addFriendshipUI(scanner);
                    break;
                case "2":
                    removeFriendshipUI(scanner);
                    break;
                case "3":
                    modifyFriendshipUI(scanner);
                    break;
                case "4":
                    searchFriendshipUI(scanner);
                    break;
                case "5":
                    numberOfFriendshipsUI();
                    break;
                case "6":
                    getAllFriendshipsUI();
                    break;
                case "7":
                    printNumberOfCommunitiesUI();
                    break;
                case "8":
                    System.out.println("[x]Option currently unavailable!");
                    break;
                case "9":
                    getAllCommunitiesUI();
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
