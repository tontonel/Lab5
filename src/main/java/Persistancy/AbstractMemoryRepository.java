package Persistancy;

import Domain.Identifiable;
import Exception.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public abstract class AbstractMemoryRepository<K extends Identifiable<T>, E extends Identifiable<T>, T> extends AbstractRepository<K, E, T> {

    protected String fileName;

    public abstract void readFromFile() throws IOException, NoIdenticEntitiesException, ParseException;

    public abstract void writeToFile() throws IOException;

    public AbstractMemoryRepository(String FileName) throws IOException, NoIdenticEntitiesException, ParseException {
        super(new HashMap<>());
        this.fileName = FileName;
        readFromFile();
    }

    @Override
    public void addEntity(K key, E value) throws NoIdenticEntitiesException, IOException {
        super.addEntity(key, value);
        writeToFile();
    }

    @Override
    public boolean deleteEntity(K key, E entity) throws IOException {
        boolean flag = super.deleteEntity(key, entity);
        writeToFile();
        return flag;
    }

    @Override
    public void updateEntity(T id, E entity) throws NoEntityFound, IOException, NoIdenticEntitiesException {
        super.updateEntity(id, entity);
        writeToFile();
    }
}
