package domain;

import utils.Constants;

import java.time.LocalDate;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private User firstFriend;
    private User secondFriend;
    private LocalDate date;

    public Friendship(User firstFriend, User secondFriend) {
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
        this.date = LocalDate.now();
    }

    public Friendship(User firstFriend, User secondFriend, LocalDate date) {
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
        this.date = date;
    }

    public User getFirstFriend() {
        return firstFriend;
    }

    public void setFirstFriend(User firstFriend) {
        this.firstFriend = firstFriend;
    }

    public User getSecondFriend() {
        return secondFriend;
    }

    public void setSecondFriend(User secondFriend) {
        this.secondFriend = secondFriend;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "id: " + super.getId() + "\nfirst friend: " + getFirstFriend() + "\nsecond friend: " + getSecondFriend() + "\ndate when the friendship was created: " + getDate().format(Constants.DATE_TIME_FORMATTER);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstFriend().getId(), getSecondFriend().getId());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof Friendship that)) {
            return false;
        }
        return (Objects.equals(getFirstFriend().getId(), that.getFirstFriend().getId()) && Objects.equals(getSecondFriend().getId(), that.getSecondFriend().getId())) ||
                (Objects.equals(getFirstFriend().getId(), that.getSecondFriend().getId()) && Objects.equals(getSecondFriend().getId(), that.getFirstFriend().getId()));
    }
}
