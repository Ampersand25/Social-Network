package validation;

import domain.User;
import exception.ValidationException;
import utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class UserValidator implements IValidator<User> {
    /**
     * Metoda privata de tip int (returneaza/intoarce un intreg (integer) cu semn (signed) pe 4 bytes/octeti (32 de biti)) care verifica daca un string dat ca si parametru reprezinta un nume valid (poate sa fie numele sau prenumele unei persoane din lumea reala)<br>
     * Valoarea numerica intreaga returnata de catre functie reprezinta codul de eroare (0 daca string-ul este valid si o valoare nenula (diferita de 0) daca string-ul nu este valid (nu poate reprezenta un nume real))
     * @param name obiect de clasa String (sir de caractere) reprezentand string-ul pe care vrem sa il validam (sa verificam daca reprezinta un nume valid)
     * @return <b>0</b> - string-ul name este valid<br>
     *         <b>1</b> - string-ul name are lungime prea mica (contine 0 caractere)<br>
     *         <b>2</b> - string-ul name are lungime prea mare (contine cel putin 100 de caractere)<br>
     *         <b>3</b> - string-ul name incepe cu un caracter invalid (primul caracter nu este litera mare sau cifra)<br>
     *         <b>4</b> - string-ul name contine un caracter invalid (care nu este nici litera, nici cifra si nici unul dintre caracterele speciale care sunt permise in componenta unor nume: apostrof, linie orizontala sau spatiu)<br>
     *         <b>5</b> - string-ul name contine doua caractere speciale consecutive (prin urmare numele nu este valid)<br>
     *         <b>6</b> - string-ul name se termina cu un caracter invalid (ultimul caracter nu este litera sau cifra)
     */
    private int validName(@NotNull String name) {
        int nameLength = name.length();
        if(nameLength < 1) {
            return 1;
        }
        if(nameLength > 100) {
            return 2;
        }

        char firstCharacterName = name.charAt(0);
        if(!Character.isDigit(firstCharacterName) && !Character.isUpperCase(firstCharacterName)) {
            return 3;
        }

        for(int index = 1; index < nameLength - 1; ++index) {
            char currentCharacter = name.charAt(index);
            if(!Character.isLetterOrDigit(currentCharacter) && !Constants.VALID_NAME_SPECIAL_CHARACTERS.contains(currentCharacter)) {
                return 4;
            }
            else if(Constants.VALID_NAME_SPECIAL_CHARACTERS.contains(currentCharacter)) {
                char previousCharacter = name.charAt(index - 1);
                if(Constants.VALID_NAME_SPECIAL_CHARACTERS.contains(previousCharacter)) {
                    return 5;
                }
            }
        }

        char lastCharacterName = name.charAt(nameLength - 1);
        if(!Character.isLetterOrDigit(lastCharacterName)) {
            return 6;
        }

        return 0;
    }

    /**
     * Metoda privata de tip boolean (returneaza/intoarce o valoare de tip bool/boolean, adica false sau true) care verifica daca un string reprezinta o adresa de email valida (se potriveste cu un sablon (o macheta) care indica formatul unei adrese de email valide)
     * @param emailAdress instanta a clasei String care reprezinta adresa de email pe care dorim sa o validam (verificam daca poate sa reprezinte o adresa de email valida)
     * @param regexPattern instanta a clasei String care reprezinta o expresie regulara (expresie ce reprezinta un sablon pentru o adresa de email valida)
     * @return <b>false</b> - string-ul (obiectul de clasa String) emailAdress nu reprezinta o adresa de email valida<br>
     *         <b>true</b>  - string-ul (obiectul de clasa String) emailAdress reprezinta o adresa de email valida
     */
    private boolean patternMatches(String emailAdress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAdress)
                .matches();
    }

    /**
     * Metoda publica de tip void (functie procedurala care nu returneaza/intoarce nimic) pentru validarea unui utilizator (obiect de clasa User)<br>
     * Metoda valideaza componentele (datele membru) ale unui obiect de tipul User dat ca si parametru de intrare pentru metoda<br>
     * Datele membru (campurile/atributele) care sunt validate sunt: id (identificator unic), firstName (prenumele), lastName (numele de familie), birthday (data nasterii) si email (adresa de email)<br>
     * Datele membru (campurile/atributele) care nu sunt validate sunt: friendList (lista de prieteni a unui utilizator)
     * @param user obiect de clasa/tipul User (instanta a clasei User) ce reprezinta un posibil utilizator din reteaua de socializare
     * @throws ValidationException daca obiectul user contine cel putin un atribut/camp invalid (prin urmare nu reprezinta un utilizator valid pentru reteaua de socializare)
     */
    @Override
    public void validate(@NotNull User user) throws ValidationException {
        String err = new String("");

        Long userId = user.getId();
        if(userId == null) {
            err += "[!]Invalid user id (id must not be null)!\n";
        }
        else if(userId < 0L) {
            err += "[!]Invalid user id (id must be non-negative)!\n";
        }

        String userFirstName = user.getFirstName();
        if(userFirstName == null) {
            err += "[!]Invalid first name (first name must not be null)!\n";
        }
        else {
            err += switch(validName(userFirstName)) {
                case 1 -> "[!]Invalid first name (first name is too short)!\n";
                case 2 -> "[!]Invalid first name (first name is too long)!\n";
                case 3 -> "[!]Invalid first name (first name must start with capital letter or digit)!\n";
                case 4 -> "[!]Invalid first name (first name contains invalid characters)!\n";
                case 5 -> "[!]Invalid first name (first name contains too many consecutive special characters)!\n";
                case 6 -> "[!]Invalid first name (first name must end with letter or digit)!\n";
                default -> "";
            };
        }

        String userLastName = user.getLastName();
        if(userLastName == null) {
            err += "[!]Invalid last name (last name must not be null)!\n";
        }
        else {
            err += switch(validName(userLastName)) {
                case 1 -> "[!]Invalid last name (last name is too short)!\n";
                case 2 -> "[!]Invalid last name (last name is too long)!\n";
                case 3 -> "[!]Invalid last name (last name must start with capital letter or digit)!\n";
                case 4 -> "[!]Invalid last name (last name contains invalid characters)!\n";
                case 5 -> "[!]Invalid last name (last name contains too many consecutive special characters)!\n";
                case 6 -> "[!]Invalid last name (last name must end with letter or digit)!\n";
                default -> "";
            };
        }

        LocalDate userBirthday = user.getBirthday();
        if(userBirthday == null) {
            err += "[!]Invalid birthday (birthday must not be null)!\n";
        }
        else if(userBirthday.isAfter(LocalDate.now())) {
            err += "[!]Invalid birthday (birthday must not be in the future)!\n";
        }
        else if(userBirthday.isAfter(LocalDate.now().minusYears(3))) {
            err += "[!]Invalid birthday (user must not be younger than 3 years old)!\n";
        }
        else if(userBirthday.isBefore(LocalDate.now().minusYears(120))) {
            err += "[!]Invalid birthday (user must not be older than 120 years old)!\n";
        }

        if(!patternMatches(user.getEmail(), Constants.VALID_EMAIL_REGEX)) {
            err += "[!]Invalid email adress!\n";
        }

        if(err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
