package infrastructure.file;

import domain.Entity;
import exception.RepoException;
import infrastructure.memory.InMemoryRepo;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public abstract class AbstractFileRepo<ID, E extends Entity<ID>> extends InMemoryRepo<ID, E> {
    private final String fileName;

    protected abstract E assembleEntity(List<String> attributes);

    protected abstract String convertEntityToString(E entity);

    protected void loadData() {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] dataTokens = data.split(";");
                E entity = assembleEntity(Arrays.asList(dataTokens));
                super.add(entity);
            }
        } catch(IOException ex) {
            System.err.println("[!]Error at reading data from file!");
            ex.printStackTrace();
        } catch(RepoException ex) {
            System.err.println("[!]Error at adding entity to repository!");
            ex.printStackTrace();
        }
    }

    private void appendToFile(E entity) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))) {
            bufferedWriter.write(convertEntityToString(entity));
            bufferedWriter.newLine();
        } catch(IOException ex) {
            System.err.println("[!]Error at appending data to file!");
            ex.printStackTrace();
        }
    }

    private void storeToFile() {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, false))) {
            Iterable<E> entities = super.getAll();
            for(E entity : entities) {
                bufferedWriter.write(convertEntityToString(entity));
                bufferedWriter.newLine();
            }
        } catch(IOException ex) {
            System.err.println("[!]Error at writing data to file!");
            ex.printStackTrace();
        } catch(RepoException ignored) {}
    }

    public AbstractFileRepo(String fileName, boolean loadDataEnabled) throws IOException, RepoException {
        this.fileName = fileName;
        if(loadDataEnabled) {
            loadData();
        }
    }

    @Override
    public void add(E entity) throws RepoException, IllegalArgumentException {
        super.add(entity);
        appendToFile(entity);
    }

    @Override
    public E remove(ID id) throws RepoException, IllegalArgumentException {
        E deletedEntity = super.remove(id);
        storeToFile();
        return deletedEntity;
    }

    @Override
    public E modify(E entity) throws RepoException, IllegalArgumentException {
        E modifiedEntity = super.modify(entity);
        storeToFile();
        return modifiedEntity;
    }

    @Override
    public E search(ID id) throws RepoException, IllegalArgumentException {
        return super.search(id);
    }

    @Override
    public int len() {
        return super.len();
    }

    @Override
    public Iterable<E> getAll() throws RepoException {
        return super.getAll();
    }
}
