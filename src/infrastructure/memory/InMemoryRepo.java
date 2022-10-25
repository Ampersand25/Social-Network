package infrastructure.memory;

import domain.Entity;
import exception.RepoException;
import infrastructure.IRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepo<ID, E extends Entity<ID>> implements IRepository<ID, E> {
    private Map<ID, E> entities;

    public InMemoryRepo() {
        entities = new HashMap<>();
    }

    @Override
    public void add(E entity) throws RepoException {
        if(entity == null) {
            throw new IllegalArgumentException("[!]Invalid entity (entity must not be null)!");
        }
        if(entities.get(entity.getId()) != null) {
            throw new RepoException("[!]Entity already exists (there is an entity with the given id)!");
        }
        entities.put(entity.getId(), entity);
    }

    @Override
    public E remove(ID id) throws RepoException {
        if(id == null) {
            throw new IllegalArgumentException("[!]Invalid id (id must not be null)!");
        }
        if(entities.isEmpty()) {
            throw new RepoException("[!]There are no entities in the repository!");
        }
        E removedEntity = entities.remove(id);
        if(removedEntity == null) {
            throw new RepoException("[!]There is no entity in the repository with the given id!");
        }
        return removedEntity;
    }

    @Override
    public E modify(E entity) throws RepoException {
        if(entity == null) {
            throw new IllegalArgumentException("[!]Invalid entity (entity must not be null)!");
        }
        if(entities.isEmpty()) {
            throw new RepoException("[!]There are no entities in the repository!");
        }
        E modifiedEntity = entities.get(entity.getId());
        if(modifiedEntity == null) {
            throw new RepoException("[!]There is no entity in the repository with the given id!");
        }
        entities.remove(entity.getId());
        entities.put(entity.getId(), entity);
        return modifiedEntity;
    }

    @Override
    public E search(ID id) throws RepoException {
        if(id == null) {
            throw new IllegalArgumentException("[!]Invalid id (id must not be null)!");
        }
        if(entities.isEmpty()) {
            throw new RepoException("[!]There are no entities in the repository!");
        }
        E searchedEntity = entities.get(id);
        if(searchedEntity == null) {
            throw new RepoException("[!]There is no entity in the repository with the given id!");
        }
        return searchedEntity;
    }

    @Override
    public int len() {
        return entities.values().size();
    }

    @Override
    public Iterable<E> getAll() throws RepoException {
        if(entities.isEmpty()) {
            throw new RepoException("[!]There are no entities in the repository!");
        }
        return entities.values();
    }
}
