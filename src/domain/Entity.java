package domain;

public class Entity<ID> {
    private ID id;

    /**
     * Constructor public si default (implicit, adica fara parametri) al unui obiect de clasa Entity
     */
    public Entity() {}

    /**
     * Constructor public al unui obiect de clasa Entity care primeste ca si parametru identificatorul unic al entitatii care se creeaza
     * @param id obiect de tipul/clasa ID (tip generic de date) care reprezinta id-ul unic (atribut/camp) al entitatii
     */
    public Entity(ID id) {
        this.id = id;
    }

    /**
     * Metoda publica de tip getter care intoarce/returneaza identificatorul unei entitati (id-ul entitatii pentru care este apelata metoda)
     * @return obiect de tipul ID care reprezinta id-ul entitatii curente (identificatorul entitatii care apeleaza metoda)
     */
    public ID getId() {
        return id;
    }

    /**
     * Metoda publica de tip setter care modifica/seteaza identificatorul (atribut/camp privat) unei entitati (il inlocuieste cu parametrul id de tipul ID)
     * @param id obiect de clasa ID (tip generic de date) ce reprezinta noul identificator unic al entitatii care apeleaza metoda (suprascrie id-ul existent cu noul id dat ca si parametru de intrare metodei)
     */
    public void setId(ID id) {
        this.id = id;
    }
}
