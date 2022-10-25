package domain;

public class HasID<ID> {
    private ID id;

    public HasID(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
