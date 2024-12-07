package repository;

import model.HasID;

import java.util.List;
import java.util.Set;
/**
 * An interface that defines the basic CRUD operations for a repository.
 *
 * @param <T> The type of objects stored in the repository, which must implement HasId.
 */
public interface IRepository<T extends HasID> {
    /**
     * Creates a new object in the repository.
     *
     * @param obj The object to create.
     */
    void create(T obj);
    /**
     * Retrieves all objects from the repository.
     *
     * @return A list of all objects in the repository.
     */
    List<T> readAll();
    /**
     * Updates an existing object in the repository.
     *
     * @param obj The object to update.
     */
    void update(T obj);
    /**
     * Deletes an object from the repository by its ID.
     *
     * @param id The unique identifier of the object to delete.
     */
    void delete(Integer id);
    /**
     * Retrieves an object from the repository by its ID.
     *
     * @param id The unique identifier of the object to retrieve.
     * @return The object with the specified ID, or null if not found.
     */
    T get(Integer id);
    Set<Integer> getKeys();
}
