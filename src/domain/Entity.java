package domain;

public class Entity<ID> {
    private ID id;

    /**
     * Constructor public si default (implicit, adica fara parametri) al unui obiect de clasa Entity
     */
    public Entity() {}

    /**
     * Constructor public al unui obiect de clasa Entity care primeste ca si parametru id-ul entitatii (id care trebuie sa fie unic)
     * @param id - obiect de tipul/clasa ID (tip generic de date) care reprezinta id-ul unic al entitatii
     */
    public Entity(ID id) {
        this.id = id;
    }

    /**
     * Metoda publica de tip getter care intoarce/returneaza id-ul unei entitati (id-ul entitatii pentru care este apelata metoda)
     * @return - obiect de tipul ID care reprezinta id-ul entitatii curente (id-ul entitatii care apeleaza metoda)
     */
    public ID getId() {
        return id;
    }

    /**
     * Metoda publica de tip setter care modifica id-ul unei entitati (il inlocuieste cu parametrul id de tipul ID)
     * @param id - obiect de clasa ID care reprezinta noul id al entitatii care apeleaza metoda
     */
    public void setId(ID id) {
        this.id = id;
    }
}
