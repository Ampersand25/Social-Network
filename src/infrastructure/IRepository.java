package infrastructure;

import domain.Entity;
import exception.RepoException;

public interface IRepository<ID, E extends Entity<ID>> {
    /**
     * Metoda publica abstracta (nedefinita) de tip void (nu returneaza/intoarce nicio valoare) care adauga o entitate entity de tipul E (tip de date generic) in reteaua de socializare
     * @param entity obiect de clasa E (entitatea pe care dorim sa o adaugam in retea)
     * @throws RepoException daca entitatea entity exista deja in reteaua de socializare (in retea nu putem avea entitati duplicate, adica doua sau mai multe entitati care sa aiba acelasi id)
     * @throws IllegalArgumentException daca entitatea entity data ca si parametru are o valoare nula (este null)
     */
    void add(E entity) throws RepoException, IllegalArgumentException;

    /**
     * Metoda publica abstracta (nedefinita) de tipul E (intoarce/returneaza un obiect de clasa/tipul E) care sterge un obiect cu identificatorul id din reteaua de socializare<br>
     * Metoda intoarce obiectul sters cu identificatorul unic egal cu parametrul de intrare id
     * @param id obiect de clasa/tipul ID (id-ul obiectului pe care dorim sa il stergem/eliminam din retea)
     * @return entitatea cu identificatorul id din reteaua de socializare (daca stergerea s-a realizat cu succes si nu s-au aruncat exceptii/erori)
     * @throws RepoException daca nu exista nicio entitate cu identificatorul id in reteaua de socializare (fiecare entitate din retea are propriul id (identificator unic))
     * @throws IllegalArgumentException daca parametrul de intrare id este o valoare nula (este null)
     */
    E remove(ID id) throws RepoException, IllegalArgumentException;

    /**
     * Metoda publica abstracta (nedefinita) de tipul E (tip generic de date) care modifica/actualizeaza un obiect de tipul E (entitate) din reteaua de socializare
     * @param entity obiect de tipul E (tip generic de date) reprezentand entitatea pe care dorim sa o modificam/actualizam
     * @return entitatea modificata/actualizata (obiect de clasa E) in cazul in care modificarea/actualizarea s-a realizat cu succes (se arunca exceptie in caz contrar (adica daca modificarea nu s-a putut realiza))
     * @throws RepoException daca nu exista entitati in reteaua de socializare (reteaua este goala) sau daca entitatea pe care dorim sa o modificam nu exista in retea
     * @throws IllegalArgumentException daca entitatea entity de tipul E are o valoare nula (este null)
     */
    E modify(E entity) throws RepoException, IllegalArgumentException;

    /**
     * Metoda publica abstracta (nedefinita) de tipul E (returneaza/intoarce un obiect de tipul E (tip generic)) care cauta o entitate dupa id (se da ca parametru de intrare metodei) in reteaua de socializare
     * @param id obiect de tipul ID (tip generic de date) reprezentand identificatorul unic al entitatii pe care dorim sa o cautam in retea
     * @return entitatea (obiect de clasa/tipul E (tip generic de date)) cu identificatorul id (daca aceasta exista in retea)
     * @throws RepoException daca nu exista entitati in retea (reteaua este goala) sau daca nu exista nicio entitate cu identificatorul unic id in reteaua de socializare
     * @throws IllegalArgumentException daca identificatorul id (parametru de intrare al metodei) este o valoare nula (adica este null)
     */
    E search(ID id) throws RepoException, IllegalArgumentException;

    /**
     * Metoda publica abstracta (nedefinita) de tipul int (integer = intreg) care returneaza/intoarce numarul total de entitati din reteaua de socializare
     * @return valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) care este pozitiva (mai mare sau egala cu 0) si reprezinta numarul de entitati din reteaua de socializare
     */
    int len();

    /**
     * Metoda publica abstracta (nedefinita) de tipul Iterable cu elemente de tipul E (entitati) care returneaza/intoarce o lista (obiect iterabil (care poate sa fie parcurs/iterat)) cu toate entitatile din reteaua de socializare
     * @return o lista cu elemente de tipul E (E tip generic de date care poate sa fie User sau Friendship) care reprezinta toate entitatile din reteaua de socializare (toti utilizatorii sau toate prieteniile)
     * @throws RepoException  daca nu exista entitati in retea
     */
    Iterable<E> getAll() throws RepoException;
}
