package infrastructure.memory;

import domain.Entity;
import exception.RepoException;
import infrastructure.IRepository;

import java.util.Map;
import java.util.HashMap;

public class InMemoryRepo<ID, E extends Entity<ID>> implements IRepository<ID, E> {
    private Map<ID, E> entities;

    public InMemoryRepo() {
        entities = new HashMap<>();
    }

    /**
     * Metoda publica definita de tipul void (functie procedurala (procedura) care nu returneaza/intoarce nicio valoare) care adauga un obiect entity de tipul E (adica o entitate) in reteaua de socializare
     * @param entity obiect de tipul/clasa E (tip generic de date) pe care dorim sa il adaugam in retea
     * @throws RepoException daca exista deja o entitate (un obiect) cu acelasi identificator ca si entitatea entity pe care dorim sa o adaugam in retea (adica entitatea entity exista deja in retea)
     * @throws IllegalArgumentException daca parametrul de intrare entity are o valoare nula (entitatea entity este egala cu null)
     */
    @Override
    public void add(E entity) throws RepoException, IllegalArgumentException {
        if(entity == null) {
            throw new IllegalArgumentException("[!]Invalid entity (entity must not be null)!\n");
        }

        if(entities.get(entity.getId()) != null) {
            throw new RepoException("[!]Entity already exists (there is an entity with the given id)!\n");
        }

        if(len() != 0) {
            Iterable<E> allEntities = getAll();
            for(E existingEntity : allEntities) {
                if(entity.equals(existingEntity)) {
                    throw new RepoException("[!]Entity already exists!\n");
                }
            }
        }

        entities.put(entity.getId(), entity);
    }

    /**
     * Metoda publica definita de tipul E (returneaza/intoarce un obiect de clasa E) care sterge o entitate cu identificatorul id din reteaua de socializare
     * @param id obiect de tipul ID (tip generic de reprezentare a datelor) reprezentand identificatorul unic al entitatii pe care dorim sa o stergem/eliminam din retea
     * @return obiect de tipul E (entitate) reprezentand obiectul sters din reteaua de socializare (daca exista o entitate (un obiect) in retea care sa aiba identificatorul (id-ul) egal cu parametrul id de tipul ID)
     * @throws RepoException daca nu exista entitati in retea (reteaua de socializare este goala/vida) sau daca nu exista nicio entitate care sa aiba identificatorul egal cu parametrul formal/simbolic id de tipul ID
     * @throws IllegalArgumentException daca parametrul de intrare id are valoarea egala cu null
     */
    @Override
    public E remove(ID id) throws RepoException, IllegalArgumentException {
        if(id == null) {
            throw new IllegalArgumentException("[!]Invalid id (id must not be null)!\n");
        }

        if(entities.isEmpty()) {
            throw new RepoException("[!]There are no entities in the repository!\n");
        }

        E removedEntity = entities.remove(id);
        if(removedEntity == null) {
            throw new RepoException("[!]There is no entity in the repository with the given id!\n");
        }

        return removedEntity;
    }

    /**
     * Metoda publica definita de tipul E (returneaza/intoarce un obiect de clasa E) care modifica un obiect entity (o entitate) din reteaua de socializare (daca acesta exista) sau arunca exceptie in cazul in care nu exista
     * @param entity obiect de tipul E (tip generic de date) pe care dorim sa il modificam/actualizam in reteaua de socializare
     * @return obiect de tipul/clasa E care reprezinta entitatea modificata/actualizata (daca modificarea se realizeaza cu succes (exista o entitate in retea care are acelasi id ca si entitatea entity data ca si parametru de intrare metodei))
     * @throws RepoException daca nu exista entitati in retea (reteaua este goala/vida) sau daca nu exista o entitate care sa aiba acelasi identificator ca si entitatea entity
     * @throws IllegalArgumentException daca parametrul formal/simbolic de intrare entity reprezinta o entitate nula (are valoarea egala cu null)
     */
    @Override
    public E modify(E entity) throws RepoException, IllegalArgumentException {
        if(entity == null) {
            throw new IllegalArgumentException("[!]Invalid entity (entity must not be null)!\n");
        }

        if(entities.isEmpty()) {
            throw new RepoException("[!]There are no entities in the repository!\n");
        }

        E modifiedEntity = entities.get(entity.getId());
        if(modifiedEntity == null) {
            throw new RepoException("[!]There is no entity in the repository with the given id!\n");
        }

        entities.put(entity.getId(), entity);
        return modifiedEntity;
    }

    /**
     * Metoda publica definita de tipul E (intoarce/returneaza un obiect de tipul ID (tip generic de reprezentare a datelor)) care cauta o entitate in retea dupa id (cauta entitatea care are identificatorul egal cu parametrul formal/simbolic id)
     * @param id obiect de tipul ID care reprezinta identificatorul unic al entitatii pe care dorim sa o cautam/gasim in reteaua de socializare
     * @return obiect de tipul E (instanta a clasei E) reprezentand entitatea pe care o cautam in retea (daca acesta exista (in caz contrar se arunca exceptie))
     * @throws RepoException daca nu exista entitati in reteaua de socializare (reteaua este goala/vida) sau daca nu exista nicio entitate cu identificatorul id in retea
     * @throws IllegalArgumentException daca parametrul formal/simbolic de intrare id (de tipul ID) are valoarea nula (egala cu null)
     */
    @Override
    public E search(ID id) throws RepoException, IllegalArgumentException {
        if(id == null) {
            throw new IllegalArgumentException("[!]Invalid id (id must not be null)!\n");
        }

        if(entities.isEmpty()) {
            throw new RepoException("[!]There are no entities in the repository!\n");
        }

        E searchedEntity = entities.get(id);
        if(searchedEntity == null) {
            throw new RepoException("[!]There is no entity in the repository with the given id!\n");
        }

        return searchedEntity;
    }

    /**
     * Metoda publica definita care returneaza/intoarce un numar intreg (valoare numerica intreaga) reprezentand numarul de entitati din reteaua de socializare
     * @return numar intreg pozitiv cu semn (signed) pe 4 bytes/octeti (32 de biti) non-negative (mai mare sau egal cu 0) ce reprezinta numarul total de entitati din reteaua de socializare (dimensiunea retelei)
     */
    @Override
    public int len() {
        return entities.values().size();
    }

    /**
     * Metoda publica definita care returneaza/intoarce toate entitatile de tipul E (adica toate entitatile) din reteaua de socializare
     * @return obict iterabil (vector, multime, dictionar, etc.) care contine elemente de tipul E (entitati) reprezentand lista de entitati din reteaua de socializare
     * @throws RepoException daca nu exista entitati in retea (reteaua este goala/vida)
     */
    @Override
    public Iterable<E> getAll() throws RepoException {
        if(entities.isEmpty()) {
            throw new RepoException("[!]There are no entities in the repository!\n");
        }

        return entities.values();
    }
}
