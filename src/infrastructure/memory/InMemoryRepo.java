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

        entities.remove(entity.getId());
        entities.put(entity.getId(), entity);
        return modifiedEntity;
    }

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

    @Override
    public int len() {
        return entities.values().size();
    }

    @Override
    public Iterable<E> getAll() throws RepoException {
        if(entities.isEmpty()) {
            throw new RepoException("[!]There are no entities in the repository!\n");
        }

        return entities.values();
    }
}
