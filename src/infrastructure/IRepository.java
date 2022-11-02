package infrastructure;

import domain.Entity;
import exception.RepoException;

public interface IRepository<ID, E extends Entity<ID>> {
    /**
     * Metoda publica abstracta (nedefinita) de tip void (nu returneaza/intoarce nicio valoare) care adauga o entitate entity de tipul E in repozitoriu
     * @param entity - obiect de clasa E (entitatea pe care dorim sa o adaugam in repozitoriu, adica in reteaua de socializare)
     * @throws RepoException - exceptie aruncata daca entitatea entity exista deja in repozitoriu (reteaua de socializare); in retea nu putem avea entitati duplicate
     */
    void add(E entity) throws RepoException;

    /**
     * Metoda publica abstracta (nedefinita) de tipul E (intoarce/returneaza un obiect de clasa/tipul E) care sterge un obiect cu id-ul id din repozitoriu (reteaua de socializare)
     * @param id - obiect de clasa/tipul ID (id-ul obiectului pe care dorim sa il stergem din retea (repozitoriu))
     * @return - metoda intoarce/returneaza obiectul sters (daca stergerea se realizeaza cu succes)
     * @throws RepoException - exceptie aruncata daca nu exista nicio entitate cu id-ul id in repozitoriu (adica in reteaua de socializare)
     */
    E remove(ID id) throws RepoException;

    /**
     * Metoda publica abstracta (nedefinita) de tipul E (tip generic de date) care modifica/actualizeaza un de tipul E din retea (repozitoriu)
     * @param entity - obiect de tipul E (tip generic) pe care dorim sa il modificam/actualizam
     * @return - entitatea modificata/actualizata (obiect de clasa E)
     * @throws RepoException - exceptie aruncata daca nu exista entitati in reteaua de socializare sau daca entitatea pe care dorim sa o modificam nu exista in retea (repozitoriu)
     */
    E modify(E entity) throws RepoException;

    /**
     * Metoda publica abstracta (nedefinita) de tipul E (returneaza/intoarce un obiect de tipul E (tip generic)) care cauta o entitate dupa id in retea
     * @param id - obiect de tipul ID (tip generic de date) reprezentand id-ul entitatii pe care dorim sa o cautam in retea (repozitoriu)
     * @return - entitatea cu id-ul id (daca aceasta exista in retea/repozitoriu)
     * @throws RepoException - exceptie aruncata daca nu exista nicio entitate cu id-ul id in reteaua de socializare (repozitoriu) sau daca nu exista entitati in retea
     */
    E search(ID id) throws RepoException;

    /**
     * Metoda publica abstracta (nedefinita) de tipul int (integer = intreg) care returneaza/intoarce numarul total de entitati din reteaua de socializare (repozitoriu)
     * @return - numarul de entitati din retea (repozitoriu), valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) care este pozitiva (mai mare sau egala cu 0)
     */
    int len();

    /**
     * Metoda publica abstracta (nedefinita) de tipul Iterable cu elemente de tipul E (entitati) care returneaza/intoarce o lista (obiect iterabil (care poate sa fie parcurs/iterat)) cu toate entitatile din retea (repozitoriu)
     * @return - lista cu elemente de tipul E (E tip generic de date) care reprezinta toate entitatile din reteaua de socializare
     * @throws RepoException - exceptie aruncata daca nu exista entitati in retea/repozitoriu
     */
    Iterable<E> getAll() throws RepoException;
}
