package repository;

import model.HasID;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBRepository<T extends HasID> {
    private final Connection connection;
    private final String tableName;
    private final RowMapper<T> rowMapper;

    public DBRepository(Connection connection, String tableName, RowMapper<T> rowMapper) {
        this.connection = connection;
        this.tableName = tableName;
        this.rowMapper = rowMapper;
    }

    public void create(T obj) {
        try {
            String query = "INSERT INTO " + tableName + " (" + obj.getClass().getMethod("getColumns").invoke(null) + ") VALUES (" + obj.getClass().getMethod("getValues").invoke(obj) + ")";
            try (Statement st = connection.createStatement()) {
                st.executeUpdate(query);
            }
        } catch (SQLException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public List<T> readAll() {
        List<T> result = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void update(T obj) {
        try {
            String query = "UPDATE " + tableName + " SET " + obj.getClass().getMethod("getUpdateValues").invoke(obj) + " WHERE id = " + obj.getId();
            try (Statement st = connection.createStatement()) {
                st.executeUpdate(query);
            }
        } catch (SQLException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public void delete(Integer id) {
        String query = "DELETE FROM " + tableName + " WHERE id = " + id;
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public T get(Integer id) {
        String query = "SELECT * FROM " + tableName + " WHERE id = " + id;
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<Integer> getKeys() {
        Set<Integer> keys = new HashSet<>();
        String query = "SELECT id FROM " + tableName;
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                keys.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keys;
    }
}