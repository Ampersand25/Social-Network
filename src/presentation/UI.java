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
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.concurrent.atomic.AtomicInteger;

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
        System.out.println("[5] - search user after name");
        System.out.println("[6] - get the number of existing users");
        System.out.println("[7] - get all existing users");
        System.out.println("[8] - get all friends of a given user");
        System.out.println("[9] - add 10 users in the social network");
        System.out.println("*type \"menu\" to display the users menu");
        System.out.println("**type \"exit\" to exit the application");
    }

    private void addUserUI(@NotNull Scanner scanner) {
        System.out.print("Introduce the first name of the new user: ");
        String firstName = scanner.nextLine();

        System.out.print("Introduce the last name of the new user: ");
        String lastName = scanner.nextLine();

        try {
            System.out.print("Introduce the birthday (year, month, day) of the new user: ");
            LocalDate birthday = LocalDate.parse(scanner.nextLine(), Constants.DATE_FORMATTER);

            System.out.print("Introduce the email adress of the new user: ");
            String email = scanner.nextLine();

            System.out.print("Introduce the location (home address) of the new user: ");
            String homeAddress = scanner.nextLine();

            System.out.print("Introduce the country of the new user: ");
            String country = scanner.nextLine();

            System.out.print("Introduce the county of the new user: ");
            String county = scanner.nextLine();

            System.out.print("Introduce the city of the new user: ");
            String city = scanner.nextLine();

            System.out.print("Introduce the username of the new user: ");
            String username = scanner.nextLine();

            System.out.print("Introduce the password of the new user: ");
            String password = scanner.nextLine();

            superService.addUser(firstName, lastName, birthday, email, homeAddress, country, county, city, username, password);
            printSuccessMessage("[+]User added with success!");
        } catch(ValidationException | RepoException | IllegalArgumentException ex) {
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

            System.out.print("Introduce the new location (home address) of the user you want to modify: ");
            String homeAddress = scanner.nextLine();

            System.out.print("Introduce the new country of the user you want to modify: ");
            String country = scanner.nextLine();

            System.out.print("Introduce the new county of the user you want to modify: ");
            String county = scanner.nextLine();

            System.out.print("Introduce the new city of the user you want to modify: ");
            String city = scanner.nextLine();

            System.out.print("Introduce the new username of the user you want to modify: ");
            String username = scanner.nextLine();

            System.out.print("Introduce the new password of the user you want to modify: ");
            String password = scanner.nextLine();

            User modifiedUser = superService.modifyUser(userId, firstName, lastName, homeAddress, country, county, city, username, password);
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

    private void searchUserAfterNameUI(@NotNull Scanner scanner) {
        System.out.print("Introduce the name of the user you want to search: ");
        String name = scanner.nextLine();
        try{
            List<User> searchedUsers = superService.searchUserAfterName(name);
            if(searchedUsers.size() == 0) {
                printSuccessMessage("[?]There is no user with the name \"" + name + "\"!");
            }
            else if(searchedUsers.size() == 1) {
                printSuccessMessage("[?]The only user that contains the name \"" + name + "\" is:\n" + searchedUsers.get(0));
            }
            else{
                printSuccessMessage("[?]All " + searchedUsers.size() + " users that contains the name \"" + name + "\" are:");
                searchedUsers.forEach(user -> printSuccessMessage(user.toString()));
            }
        } catch(RepoException | ServiceException ex) {
            printException(ex.getMessage());
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

            users.forEach(user -> printSuccessMessage(user.toString()));
        } catch(RepoException ex) {
            printException(ex.getMessage());
        }
    }

    private void getFriendsOfUserUI(@NotNull Scanner scanner) {
        try {
            System.out.print("Introduce the id of the user: ");
            Long userId = Long.parseLong(scanner.nextLine());

            List<User> friendsOfUser = superService.getFriendsOfUser(userId);
            if(friendsOfUser.size() == 0) {
                printSuccessMessage("User \"" + superService.searchUser(userId).toString() + "\" has no friends!");
            }
            else if(friendsOfUser.size() == 1) {
                printSuccessMessage("The only friend of user \"" + superService.searchUser(userId).toString() + "\" is:");
            }
            else {
                printSuccessMessage("Friends of user \"" + superService.searchUser(userId).toString() + "\" are:");
            }

            friendsOfUser.forEach(friend -> printSuccessMessage(friend.toString()));
        } catch(RepoException | ServiceException ex) {
            printException(ex.getMessage());
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        }
    }

    private void addUsersDebug() {
        try{
            superService.addUser("Ben John"       , "O'Brien"     , LocalDate.of(1998, 10, 15), "ben_john98@ezybarber.com"     , "Strada Caderea Bastiliei 11"             , "Romania", "Timis"    , "Timisoara"  , "clearwing"   , "ZuwX8xSKP9M!CKgF");
            superService.addUser("William"        , "Day"         , LocalDate.of(1988, 12, 30), "will_day88@gmail.com"         , "Piata Presei Libere 3-5"                 , "Romania", "Dolj"     , "Craiova"    , "pomegranates", "xJQ^WsdcH%yf%TzG");
            superService.addUser("Charles Richard", "Kelly"       , LocalDate.of(1996, 3 , 13), "charlie_kelly96@yahoo.com"    , "Strada Bihor"                            , "Romania", "Brasov"   , "Brasov"     , "rowboat"     , "ge*v@&W7Mh%z#FVa");
            superService.addUser("Donald"         , "Castaneda"   , LocalDate.of(1998, 7 , 4) , "donald_castaneda98@domain.com", "Strada Economu Cezarescu 42"             , "Romania", "Cluj"     , "Cluj-Napoca", "sympathy"    , "@Db*u8esF@DL4nW(");
            superService.addUser("Charles-Mike"   , "Lam"         , LocalDate.of(1986, 4 , 15), "charles_lam86@manghinsu.com"  , "Strada Miletin"                          , "Romania", "Bihor"    , "Oradea"     , "wrinkles"    , "m9yduP6vAng5#Sa&");
            superService.addUser("Robert"         , "Beck"        , LocalDate.of(1990, 7 , 23), "bert_beck90@otpku.com"        , "Calea Crangasi 29"                       , "Romania", "Braila"   , "Braila"     , "timberhead"  , "XKU8@k5P%c+s*k(A");
            superService.addUser("O'Mikel"        , "Rowe"        , LocalDate.of(1984, 3 , 20), "rowe84@thekangsua.com"        , "Bulevardul Timisoara 26"                 , "Romania", "Arges"    , "Pitesti"    , "nautical"    , "2+9VbKyNV5C%!bKT");
            superService.addUser("Mike-Abraham"   , "Dotson"      , LocalDate.of(1989, 9 , 5) , "mike_dotson89@bomukic.com"    , "Piata Romana 6"                          , "Romania", "Mures"    , "Targu Mures", "refugee"     , "GHPdPCV+ckxmuhw6");
            superService.addUser("Joseph Michael" , "Tucker-Doyle", LocalDate.of(2001, 1 , 22), "joseph_mike01@avmap.com"      , "Bulevardul General Gheorghe Magheru 2-4" , "Romania", "Maramures", "Baia Mare"  , "bint"        , "3JAhVj+^w#8e*ppJ");
            superService.addUser("Robert"         , "O'Gallagher" , LocalDate.of(1994, 12, 6) , "robertino94@gmailni.com"      , "Aleea Istru Nr. 5, Bl P4, Ap31, Sector 6", "Romania", "Botosani" , "Botosani"   , "pester"      , "AU4kJDZSUP6z^rKV");

            printSuccessMessage("[+]All 10 users added with success!");
        } catch(ValidationException | RepoException | IllegalArgumentException ex) {
            ex.printStackTrace();
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
                    searchUserAfterNameUI(scanner);
                    break;
                case "6":
                    numberOfUsersUI();
                    break;
                case "7":
                    getAllUsersUI();
                    break;
                case "8":
                    getFriendsOfUserUI(scanner);
                    break;
                case "9":
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

            System.out.print("Introduce the date (year, month, day, hour, minute, second) when the friendship was created: ");
            LocalDateTime friendsFrom = LocalDateTime.parse(scanner.nextLine(), Constants.DATE_TIME_FORMATTER);

            Friendship modifiedFriendship = superService.modifyFriendship(friendshipId, firstFriendId, secondFriendId, friendsFrom);
            printSuccessMessage("[&]Friendship modified with success!\nModified friendship:\n" + modifiedFriendship);
        } catch (ValidationException | RepoException ex) {
            printException(ex.getMessage());
        } catch(NumberFormatException ex) {
            printException("[!]Invalid id (id must be a non negative number)!\n");
        } catch(DateTimeParseException ex) {
            printException("[!]Invalid date (the format of the date must be \"yyyy-MM-dd hh:mm:ss\")!\n");
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

            AtomicInteger numberOfDisplayedFriendships = new AtomicInteger();
            friendships.forEach(friendship -> {
                if(numberOfDisplayedFriendships.getAndIncrement() > 0) {
                    System.out.println();
                }

                printSuccessMessage(friendship.toString());
            });
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

                AtomicInteger i = new AtomicInteger();
                users.forEach(user -> {
                    if(i.get() != 0) {
                        System.out.println();
                    }

                    printSuccessMessage("Community #" + i.incrementAndGet() + " is:\n" + user.toString());
                });
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
                    System.out.println(ConsoleColors.PURPLE + "[x]Option currently unavailable!" + ConsoleColors.RESET);
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
