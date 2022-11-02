package domain;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.time.LocalDate;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private final LocalDate birthday;
    private final String email;
    private List<User> friendList;

    public User(String firstName, String lastName, LocalDate birthday, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        friendList = new ArrayList<>();
    }

    public User(Long id, String firstName, String lastName, LocalDate birthday, String email) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        friendList = new ArrayList<>();
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

    public String getEmail() {
        return email;
    }

    public List<User> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }

    @Override
    public String toString() {
        return "id=" + super.getId() + "|first name=" + getFirstName() + "|last name=" + getLastName() + "|birthday=" + getBirthday() + "|email=" + getEmail();
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
