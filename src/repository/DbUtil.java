package repository;

import java.sql.*;

/**
 * Utility class for handling database operations.
 * This class provides methods for executing SQL queries and managing database connections.
 */
public class DbUtil {
    private static Connection connection;

    /**
     * Constructs a DbUtil instance with the specified database connection.
     *
     * @param connection The database connection to be used for executing SQL queries.
     */
    public DbUtil(Connection connection) {
        this.connection = connection;
    }

    /**
     * Executes a SQL update statement (INSERT, UPDATE, DELETE) with the provided parameters.
     *
     * @param sql    The SQL query to be executed.
     * @param params The parameters to be set in the SQL query.
     * @throws SQLException if there is an error executing the SQL statement.
     */
    public static void executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves the current database connection.
     *
     * @return The current database connection.
     */
    public static Connection getConnection() {
        return connection;
    }
}