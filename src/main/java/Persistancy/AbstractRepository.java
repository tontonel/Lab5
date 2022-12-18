package Persistancy;

import java.io.IOException;
import java.util.*;

import Domain.Identifiable;
import Exception.NoIdenticEntitiesException;
import Exception.NoEntityFound;

public abstract class AbstractRepository<K extends Identifiable<T>, E extends Identifiable<T>, T> implements  Repository<K, E, T> {

    protected HashMap<K, ArrayList<E>> entityMap = new HashMap<K, ArrayList<E>>();

    public AbstractRepository(HashMap<K, ArrayList<E>> entityMap) {
        this.entityMap = entityMap;
    }

    @Override
    public void addEntity(K key, E value) throws NoIdenticEntitiesException, IOException {
        for(ArrayList<E> entities : entityMap.values())
            for(E entity : entities)
                if(entity.equals(value))
                    throw new NoIdenticEntitiesException("Entity already exists");
        if(entityMap.containsKey(key))
            entityMap.get(key).add(value);
        else
            entityMap.put(key, new ArrayList<>(List.of(value)));
    }

    @Override
    public boolean deleteEntity(K key, E entity) throws IOException {
        return entityMap.get(key).remove(entity);
    }

    @Override
    public void updateEntity(T id, E entity) throws NoEntityFound, IOException, NoIdenticEntitiesException {
        Map.Entry<K, E> pair = findById(id);
        entityMap.get(pair.getKey()).remove(pair.getValue());
        for(ArrayList<E> entities : entityMap.values())
            for(E e : entities)
                if(e.equals(entity)) {
                    entityMap.get(pair.getKey()).add(pair.getValue());
                    throw new NoIdenticEntitiesException("Entity already exists");
                }
        entityMap.get(pair.getKey()).add(entity);
    }

    @Override
    public Map.Entry<K, E> findById(T id) throws NoEntityFound {
        for(Map.Entry<K, ArrayList<E>> collection : entityMap.entrySet()) {
            K key = collection.getKey();
            for (E value : collection.getValue())
                if (value.getId() == id)
                    return Map.entry(key, value);
        }
        throw new NoEntityFound(id.toString());
    }

    public ArrayList<E> findListByID(T id) throws NoEntityFound {
        for(Map.Entry<K, ArrayList<E>> collection : entityMap.entrySet()) {
            K key = collection.getKey();
            if(key.getId() == id)
                return collection.getValue();
        }
        throw new NoEntityFound(id.toString());
    }

    @Override
    public HashMap<K, ArrayList<E>> getAll() {
        return entityMap;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "entityList=" + entityMap +
                '}';
    }
}
