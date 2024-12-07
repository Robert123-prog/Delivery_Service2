package repository;

import model.HasID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * A repository implementation that stores data in memory.
 *
 * @param <T> The type of objects stored in the repository, which must implement HasId.
 */
public class InMemoryRepo<T extends HasID> implements IRepository<T> {
    private final Map<Integer,T> data = new HashMap<>();
    private List<T> entities;
    /**
     * {@inheritDoc}
     */
    @Override
    public void create(T obj) {
        data.putIfAbsent(obj.getId(), obj);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> readAll() {
        return data.values().stream().toList();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void update(T obj) {
        data.replace(obj.getId(), obj);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Integer id) {
        data.remove(id);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public T get(Integer id) {
        return data.get(id);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Integer> getKeys() {
        return data.keySet();
    }


}
