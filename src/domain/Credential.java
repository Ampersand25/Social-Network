package domain;

import java.util.Objects;

public class Credential {
    private String username;
    private String password;

    public Credential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "username=" + getUsername() + ";password=" + getPassword();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof Credential that)) {
            return false;
        }
        return Objects.equals(getUsername(), that.getUsername()) || Objects.equals(getPassword(), that.getPassword());
    }
}
