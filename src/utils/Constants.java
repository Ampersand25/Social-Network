package utils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;

public class Constants {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final List<Character> VALID_NAME_SPECIAL_CHARACTERS = Arrays.asList('\'', '-', ' ');
    public static final List<Character> VALID_ADDRESS_NAME_SPECIAL_CHARACTERS = Arrays.asList('-', ' ');
    public static final String VALID_EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
            + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";
    public static final String USER_TEXT_FILE_PATH = "./src/data/users.csv";
    public static final String FRIENDSHIP_TEXT_FILE_PATH = "./src/data/friendships.csv";
}
