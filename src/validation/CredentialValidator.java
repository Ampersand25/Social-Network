package validation;

import domain.Credential;
import exception.ValidationException;

import org.jetbrains.annotations.NotNull;

public class CredentialValidator implements IValidator<Credential> {
    @Override
    public void validate(@NotNull Credential credential) throws ValidationException {
        String err = new String("");

        String username = credential.getUsername();
        if(username == null) {
            err += "[!]Invalid username (username must not be null)\n";
        }
        else if(username.length() == 0) {
            err += "[!]Invalid username length (username must be non-empty)\n";
        }

        String password = credential.getPassword();
        if(password == null) {
            err += "[!]Invalid password (password must not be null)\n";
        }
        else if(password.length() == 0) {
            err += "[!]Invalid password length (password must be non-empty)\n";
        }

        if(err.length() != 0) {
            throw new ValidationException(err);
        }
    }
}
