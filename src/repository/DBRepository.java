package repository;

import model.HasID;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generic repository class for interacting with the database.
 * @param <T> The type of the entity that the repository manages, which extends {@link HasID}.
 */
public class DBRepository<T extends HasID> implements IRepository<T> {
    private final Connection connection;
    private final String tableName;
    private final String primaryKeyColumn;
    private final RowMapper<T> rowMapper;

    /**
     * Constructor for initializing the repository with necessary parameters.
     * @param connection The database connection.
     * @param tableName The name of the table in the database.
     * @param rowMapper The row mapper used to map result set rows to entities of type T.
     * @param primaryKeyColumn The name of the primary key column in the table.
     */
    public DBRepository(Connection connection, String tableName, RowMapper<T> rowMapper, String primaryKeyColumn) {
        this.connection = connection;
        this.tableName = tableName;
        this.rowMapper = rowMapper;
        this.primaryKeyColumn = primaryKeyColumn;
    }

    /**
     * Inserts a new entity into the database.
     * @param obj The entity to be inserted.
     */
    public void create(T obj) {
        try {
            String columns = (String) obj.getClass().getMethod("getColumns").invoke(null);
            String values = (String) obj.getClass().getMethod("getValues").invoke(obj);
            String query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.executeUpdate();
            }
        } catch (SQLException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all entities from the database.
     * @return A list of all entities.
     */
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

    /**
     * Updates an existing entity in the database.
     * @param obj The entity to be updated.
     */
    public void update(T obj) {
        try {
            String updateValues = (String) obj.getClass().getMethod("getUpdateValues").invoke(obj);
            String query = "UPDATE " + tableName + " SET " + updateValues + " WHERE " + primaryKeyColumn + " = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, (Integer) obj.getClass().getMethod("getId").invoke(obj));
                ps.executeUpdate();
            }
        } catch (SQLException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an entity from the database based on its ID.
     * @param id The ID of the entity to be deleted.
     */
    public void delete(Integer id) {
        String query = "DELETE FROM " + tableName + " WHERE " + primaryKeyColumn + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a single entity from the database based on its ID.
     * @param id The ID of the entity to be retrieved.
     * @return The entity, or null if not found.
     */
    public T get(Integer id) {
        String query = "SELECT * FROM " + tableName + " WHERE " + primaryKeyColumn + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all primary key values from the table.
     * @return A set containing all primary key values.
     */
    public Set<Integer> getKeys() {
        Set<Integer> keys = new HashSet<>();
        String query = "SELECT " + primaryKeyColumn + " FROM " + tableName;
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                keys.add(rs.getInt(primaryKeyColumn));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keys;
    }

    /**
     * Executes a custom SQL query and maps the results to a list of entities.
     * @param sql The SQL query to be executed.
     * @param params The parameters to be set in the query.
     * @return A list of entities resulting from the query.
     */
    public List<T> executeQuery(String sql, Object... params) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            ResultSet rs = stmt.executeQuery();
            List<T> resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(rowMapper.mapRow(rs));
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
