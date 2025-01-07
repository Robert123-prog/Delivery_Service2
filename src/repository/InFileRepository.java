package repository;

import model.HasID;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * A repository implementation that stores data in a file.
 * This class provides methods for creating, reading, updating, and deleting objects
 * that implement the HasID interface.
 *
 * @param <T> the type of objects managed by this repository, which must implement HasID.
 */
public class InFileRepository<T extends HasID> implements IRepository<T> {
    private final String filePath;
    private final Function<T, String> serializer;
    private final Function<String, T> deserializer;

    /**
     * Constructs an InFileRepository with the specified file path, serializer, and deserializer.
     *
     * @param filePath    The path to the file where data will be stored.
     * @param serializer  A function that converts an object of type T to a String for storage.
     * @param deserializer A function that converts a String back to an object of type T.
     */
    public InFileRepository(String filePath, Function<T, String> serializer, Function<String, T> deserializer) {
        this.filePath = filePath;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    /**
     * Creates a new object in the repository.
     *
     * @param obj The object to be created.
     */
    @Override
    public void create(T obj) {
        List<T> data = readDataFromFile();
        data.add(obj);
        writeDataToFile(data);
    }

    /**
     * Retrieves all objects from the repository.
     *
     * @return A list of all objects in the repository.
     */
    @Override
    public List<T> readAll() {
        return readDataFromFile();
    }

    /**
     * Deletes an object from the repository by its ID.
     *
     * @param id The ID of the object to be deleted.
     */
    @Override
    public void delete(Integer id) {
        List<T> data = readDataFromFile();
        data.removeIf(item -> item.getId().equals(id));
        writeDataToFile(data);
    }

    /**
     * Retrieves an object from the repository by its ID.
     *
     * @param id The ID of the object to be retrieved.
     * @return The object with the specified ID, or null if not found.
     */
    @Override
    public T get(Integer id) {
        return readDataFromFile().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all keys (IDs) of the objects in the repository.
     *
     * @return A set of IDs of all objects in the repository.
     */
    @Override
    public Set<Integer> getKeys() {
        List<T> data = readDataFromFile();
        Set<Integer> keys = new HashSet<>();
        for (T item : data) {
            keys.add(item.getId());
        }
        return keys;
    }

    /**
     * Updates an existing object in the repository.
     *
     * @param obj The object with updated data.
     */
    @Override
    public void update(T obj) {
        List<T> data = readDataFromFile();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(obj.getId())) {
                data.set(i, obj);
                break;
            }
        }
        writeDataToFile(data);
    }

    /**
     * Writes a list of objects to the file.
     *
     * @param data The list of objects to be written to the file.
     */
    private void writeDataToFile(List<T> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T item : data) {
                writer.write(serializer.apply(item));  // Use the provided serializer
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing data to file", e);
        }
    }

    /**
     * Reads a list of objects from the file.
     *
     * @return A list of objects read from the file.
     */
    private List<T> readDataFromFile() {
        List<T> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T obj = deserializer.apply(line);  // Use the provided deserializer
                data.add(obj);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading data from file", e);
        }
        return data;
    }
}