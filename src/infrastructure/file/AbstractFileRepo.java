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

    public abstract E assembleEntity(List<String> attributes);

    public abstract String convertEntityToString(E entity);

    private void loadData() throws IOException, RepoException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            String data = scanner.nextLine();
            String[] dataTokens = data.split(";");
            E entity = assembleEntity(Arrays.asList(dataTokens));
            super.add(entity);
        }
    }

    private void appendToFile(E entity) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));
        bufferedWriter.write(convertEntityToString(entity));
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    private void storeToFile() throws IOException, RepoException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, false));
        try {
            Iterable<E> entities = super.getAll();
            for(E entity : entities) {
                bufferedWriter.write(convertEntityToString(entity));
                bufferedWriter.newLine();
            }
        } catch(RepoException ignored) {}
        bufferedWriter.close();
    }

    public AbstractFileRepo(String fileName) throws IOException, RepoException {
        this.fileName = fileName;
        loadData();
    }

    @Override
    public void add(E entity) throws RepoException, IllegalArgumentException {
        super.add(entity);
        try {
            appendToFile(entity);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public E remove(ID id) throws RepoException, IllegalArgumentException {
        E deletedEntity = super.remove(id);
        try {
            storeToFile();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return deletedEntity;
    }

    @Override
    public E modify(E entity) throws RepoException, IllegalArgumentException {
        E modifiedEntity = super.modify(entity);
        try {
            storeToFile();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
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
