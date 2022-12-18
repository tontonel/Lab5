package Persistancy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import Exception.*;
public interface Repository<K, E, T> {
    public void addEntity(K key, E value) throws NoIdenticEntitiesException, IOException;

    public boolean deleteEntity(K key, E entity) throws IOException;

    public void updateEntity(T id, E entity) throws NoEntityFound, IOException, NoIdenticEntitiesException;

    public Map.Entry<K, E> findById(T id) throws NoEntityFound;

    public ArrayList<E> findListByID(T id) throws NoEntityFound;

    public HashMap<K, ArrayList<E>> getAll();
}
