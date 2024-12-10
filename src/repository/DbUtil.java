package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbUtil {
    private static Connection connection;

    public DbUtil(Connection connection) {
        this.connection = connection;
    }

    // Metodă pentru execuția interogărilor SQL personalizate
    public static void executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
