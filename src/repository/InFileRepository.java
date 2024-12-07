package repository;

import model.HasID;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class InFileRepository<T extends HasID> implements IRepository<T> {
    private final String filePath;
    private final Function<T, String> serializer;
    private final Function<String, T> deserializer;

    public InFileRepository(String filePath, Function<T, String> serializer, Function<String, T> deserializer) {
        this.filePath = filePath;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @Override
    public void create(T obj) {
        List<T> data = readDataFromFile();
        data.add(obj);
        writeDataToFile(data);
    }

    @Override
    public List<T> readAll() {
        return readDataFromFile();
    }

    @Override
    public void delete(Integer id) {
        List<T> data = readDataFromFile();
        data.removeIf(item -> item.getId().equals(id));
        writeDataToFile(data);
    }

    @Override
    public T get(Integer id) {
        return readDataFromFile().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<Integer> getKeys() {
        List<T> data = readDataFromFile();
        Set<Integer> keys = new HashSet<>();
        for (T item : data) {
            keys.add(item.getId());
        }
        return keys;
    }

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