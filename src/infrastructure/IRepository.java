package infrastructure;

import domain.Entity;
import exception.RepoException;

public interface IRepository<ID, E extends Entity<ID>> {
    void add(E entity) throws RepoException;

    E remove(ID id) throws RepoException;

    E modify(E entity) throws RepoException;

    E search(ID id) throws RepoException;

    int len();

    Iterable<E> getAll() throws RepoException;
}
