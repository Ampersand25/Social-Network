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
     * Metoda publica definita de tipul void (functie procedurala care nu returneaza/intoarce nicio valoare) care adauga un obiect entity de tipul E in retea
     * @param entity - obiect de tipul/clasa E pe care dorim sa il adaugam in retea
     * @throws RepoException - exceptie aruncata daca exista deja un obiect cu acelasi id ca si entitatea entity pe care dorim sa o adaugam in retea
     */
    @Override
    public void add(E entity) throws RepoException {
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
     * Metoda publica definita de tipul E (returneaza/intoarce un obiect de clasa E) care sterge o entitate cu id-ul id din retea
     * @param id - obiect de tipul ID (tip generic de reprezentare a datelor) reprezentand id-ul entitatii pe care dorim sa o stergem/eliminam din retea
     * @return - obiect de tipul E (daca exista un obiect in retea (repozitoriu) care sa aiba id-ul egal cu parametrul id) reprezentand obiectul sters din reteaua de socializare
     * @throws RepoException - exceptie aruncata in cazul in care nu exista nicio entitate in retea (repozitoriu) sau daca nu exista nicio entitate care sa aiba id-ul egal cu parametrul formal/simbolic id de tipul ID
     */
    @Override
    public E remove(ID id) throws RepoException {
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
     * Metoda publica definita de tipul E (returneaza/intoarce un obiect de clasa E) care modifica un obiect entity din reteaua de socializare (daca acesta exista) sau arunca exceptie in cazul in care nu exista
     * @param entity - obiect de tipul E (tip generic) pe care dorim sa il modificam/actualizam
     * @return - obiect de tipul/clasa E reprezentand obiectul modificat/actualizat (daca modificarea se realizeaza cu succes (exista un obiect in retea care are acelasi id ca si entitatea entity))
     * @throws RepoException - exceptie aruncata daca nu exista entitati in retea (repozitoriu) sau daca nu exista o entitate care sa aiba acelasi id ca si entity
     */
    @Override
    public E modify(E entity) throws RepoException {
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
     * Metoda publica definita de tipul E (intoarce/returneaza un obiect de tipul ID) care cauta o entitate in retea dupa id (cauta entitatea care are id-ul id)
     * @param id - obiect de tipul ID care reprezinta id-ul entitatii pe care dorim sa o cautam
     * @return - obiect de tipul E (tip de data generic) reprezentand id-ul obiectului pe care il cautam (daca acesta exista (altfel se arunca exceptie))
     * @throws RepoException - exceptie aruncata daca nu exista entitati in retea sau daca nu exista nicio entitate cu id-ul id
     */
    @Override
    public E search(ID id) throws RepoException {
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
     * Metoda publica definita care returneaza/intoarce un numar intreg (valoare numerica intreaga) reprezentand numarul de entitati din retea (repozitoriu)
     * @return - numar intreg pozitiv (mai mare sau egal cu 0) care reprezinta numarul total de entitati din reteaua de socializare
     */
    @Override
    public int len() {
        return entities.values().size();
    }

    /**
     * Metoda publica definita care returneaza/intoarce toate entitatile de tipul E din retea (repozitoriu)
     * @return - obict iterabil (lista, multime, etc.) care contine elemente de tipul E (entitati) reprezentand lista de entitati din reteaua de socializare
     * @throws RepoException - exceptie aruncata daca nu exista entitati in retea
     */
    @Override
    public Iterable<E> getAll() throws RepoException {
        if(entities.isEmpty()) {
            throw new RepoException("[!]There are no entities in the repository!\n");
        }

        return entities.values();
    }
}
