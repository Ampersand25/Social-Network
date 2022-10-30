package domain;

import java.time.LocalDate;
import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private final LocalDate birthday;

    public User(String firstName, String lastName, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public User(Long id, String firstName, String lastName, LocalDate birthday) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return "id=" + super.getId() + "|first name=" + getFirstName() + "|last name=" + getLastName() + "|birthday=" + getBirthday();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof User that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }
}
