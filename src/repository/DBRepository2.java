//package repository;
//
//import  model.HasID;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class DBRepository2<T extends HasID> implements IRepository<T> {
//    private final Connection connection;
//    private final Class<T> type;
//    private final String tableName;
//    private final List<String> columnNames;
//    private final Constructor<T> constructor;
//    private final String primaryKeyColumn;
//
//    public DBRepository2(String tableName, Class<T> type, List<String> columnNames, String primaryKeyColumn, Connection connection) {
//        this.connection = connection;
//        this.type = type;
//        this.tableName = tableName;
//        this.columnNames = columnNames;
//        this.constructor = getConstructor();
//        this.primaryKeyColumn = primaryKeyColumn;
//    }
//
//
//
//    @Override
//    public void create(T obj) {
//        PreparedStatement stmt = null;
//        try {
//            StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
//            StringBuilder values = new StringBuilder(") VALUES (");
//
//            for (int i = 0; i < columnNames.size(); i++) {
//                sql.append(columnNames.get(i));
//                values.append("?");
//                if (i < columnNames.size() - 1) {
//                    sql.append(", ");
//                    values.append(", ");
//                }
//            }
//            sql.append(values).append(")");
//
//            stmt = connection.prepareStatement(sql.toString());
//            for (int i = 0; i < columnNames.size(); i++) {
//                String columnName = columnNames.get(i);
//                String fieldName = toCamelCase(columnName);
//
//                Field field = findField(type, fieldName);
//                if (field == null) {
//                    throw new NoSuchFieldException(fieldName + " not found in class " + type.getName());
//                }
//
//                field.setAccessible(true);
//                Object value = field.get(obj);
//
//                if (value instanceof List) {
//                    Array sqlArray = connection.createArrayOf("text", ((List<?>) value).toArray());
//                    stmt.setArray(i + 1, sqlArray);
//                } else {
//                    stmt.setObject(i + 1, value);
//                }
//            }
//
//            int affectedRows = stmt.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("Creating object failed, no rows affected.");
//            }
//
//        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
//            e.printStackTrace();
//        } finally {
//            closeResources(connection, stmt, null);
//        }
//    }
//
//
//    @Override
//    public T get(Integer id) {
//        String query = "SELECT * FROM " + tableName + " WHERE " + primaryKeyColumn + " = ?";
//        T result = null;
//
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, id);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                if (rs.next()) {
//                    // Dynamically construct arguments for the constructor
//                    Object[] args = new Object[columnNames.size()];
//                    for (int i = 0; i < columnNames.size(); i++) {
//                        String columnName = columnNames.get(i);
//                        if (columnName.equals(primaryKeyColumn)) {
//                            args[i] = rs.getInt(columnName); // Explicitly get primary key as int
//                        } else {
//                            args[i] = rs.getObject(columnName); // Dynamically fetch other column values
//                        }
//                    }
//
//                    // Use the constructor to instantiate the object
//                    result = constructor.newInstance(args);
//
//                    // Explicitly set the primary key (if applicable)
//                    result.setId(rs.getInt(primaryKeyColumn));
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Error retrieving object with id " + id);
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    @Override
//    public void update(T obj) {
//        PreparedStatement statement = null;
//        try{
//            StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
//            String idColumnName;
//            if (tableName.equalsIgnoreCase("admin") ||
//                    tableName.equalsIgnoreCase("instructor") ||
//                    tableName.equalsIgnoreCase("student")) {
//                idColumnName = "userid"; // Use "userid" for these specific tables
//            } else if (tableName.equalsIgnoreCase("assignmentquiz")) {
//                idColumnName = "assignmentid";
//            } else if (tableName.equalsIgnoreCase("coursemodule")) {
//                idColumnName = "courseid";
//            } else if (tableName.equalsIgnoreCase("messageforum")) {
//                idColumnName = "messageid";
//            } else if (tableName.equalsIgnoreCase("moduleassignment")) {
//                idColumnName = "moduleid";
//            } else if (tableName.equalsIgnoreCase("studentcourse")) {
//                idColumnName = "studentid";
//            } else {
//                idColumnName = tableName.toLowerCase() + "id"; // Default logic
//            }
//
//            for(int i = 0; i < columnNames.size(); i++){
//                if(!columnNames.get(i).equals(idColumnName)){
//                    sql.append(columnNames.get(i)).append(" = ?");
//                    if(i < columnNames.size() - 1){
//                        sql.append(", ");
//                    }
//                }
//            }
//            sql.append(" WHERE ").append(idColumnName).append(" = ?");
//
//            statement = connection.prepareStatement(sql.toString());
//            int parameterIndex = 1;
//            for(String columnnName : columnNames){
//                if(!columnnName.equals(idColumnName)){
//                    String fieldName = toCamelCase(columnnName);
//                    Field field = findField(type,fieldName);
//                    if(field == null){
//                        throw new NoSuchFieldException(fieldName + " not found in class " + type.getName());
//                    }
//
//                    field.setAccessible(true);
//                    Object value = field.get(obj);
//
//                    if(value instanceof List) {
//                        Array sqlArray = connection.createArrayOf("text",((List<?>) value).toArray());
//                        statement.setArray(parameterIndex++, sqlArray);
//                    }else {
//                        statement.setObject(parameterIndex++, value);
//                    }
//                }
//            }
//            statement.setInt(parameterIndex, obj.getId());
//
//            int affectedRows = statement.executeUpdate();
//            if(affectedRows == 0){
//                throw new SQLException("Updating object failed, no rows affected.");
//
//            }
//        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
//            e.printStackTrace();
//        }finally {
//            closeResources(connection,statement,null);
//        }
//    }
//
//    @Override
//    public void delete(Integer id) {
//        Connection connection = null;
//        PreparedStatement statement = null;
//        try{
//            connection = getConnection();
//            String idColumnName;
//            if (tableName.equalsIgnoreCase("admin") ||
//                    tableName.equalsIgnoreCase("instructor") ||
//                    tableName.equalsIgnoreCase("student")) {
//                idColumnName = "userid"; // Use "userid" for these specific tables
//            } else if (tableName.equalsIgnoreCase("assignmentquiz")) {
//                idColumnName = "assignmentid";
//            } else if (tableName.equalsIgnoreCase("coursemodule")) {
//                idColumnName = "courseid";
//            } else if (tableName.equalsIgnoreCase("messageforum")) {
//                idColumnName = "messageid";
//            } else if (tableName.equalsIgnoreCase("moduleassignment")) {
//                idColumnName = "moduleid";
//            } else if (tableName.equalsIgnoreCase("studentcourse")) {
//                idColumnName = "studentid";
//            } else {
//                idColumnName = tableName.toLowerCase() + "id"; // Default logic
//            }
//            String sql = "DELETE FROM " + tableName.toLowerCase() + " WHERE " + idColumnName + " = ?";
//
//            statement = connection.prepareStatement(sql);
//            statement.setInt(1,id);
//
//            int affectedRows = statement.executeUpdate();
//            if(affectedRows == 0){
//                throw new SQLException("Deleeting object failed, no rows affected. ID may not exist");
//
//            }
//        } catch (SQLException e) {
//            System.err.println("Error deleteing object with id " + id);
//            e.printStackTrace();
//        }finally {
//            closeResources(connection, statement, null);
//        }
//    }
//
//    @Override
//    public List getAll() {
//        Connection connection = null;
//        Statement statement = null;
//        ResultSet rs = null;
//        List<T> results = new ArrayList<>();
//
//        try{
//            connection = getConnection();
//            String sql = "SELECT * FROM " + tableName;
//            statement = connection.createStatement();
//            rs = statement.executeQuery(sql);
//
//            while(rs.next()){
//                Object[] args = new Object[columnNames.size()];
//                for(int i = 0; i < columnNames.size(); i++){
//                    String columnName = columnNames.get(i);
//                    args[i] = rs.getObject(columnName);
//                }
//                T obj = constructor.newInstance(args);
//
//                //Set the ID
//                String idColumnName;
//                if (tableName.equalsIgnoreCase("admin") ||
//                        tableName.equalsIgnoreCase("instructor") ||
//                        tableName.equalsIgnoreCase("student")) {
//                    idColumnName = "userid"; // Use "userid" for these specific tables
//                } else if (tableName.equalsIgnoreCase("assignmentquiz")) {
//                    idColumnName = "assignmentid";
//                } else if (tableName.equalsIgnoreCase("coursemodule")) {
//                    idColumnName = "courseid";
//                } else if (tableName.equalsIgnoreCase("messageforum")) {
//                    idColumnName = "messageid";
//                } else if (tableName.equalsIgnoreCase("moduleassignment")) {
//                    idColumnName = "moduleid";
//                } else if (tableName.equalsIgnoreCase("studentcourse")) {
//                    idColumnName = "studentid";
//                } else {
//                    idColumnName = tableName.toLowerCase() + "id"; // Default logic
//                }
//                obj.setId(rs.getInt(idColumnName));
//
//                results.add(obj);
//            }
//        } catch (SQLException e) {
//            System.err.println("Error reyrieving all objects from " + tableName);
//            e.printStackTrace();
//        }catch (ReflectiveOperationException e){
//            System.err.println("Error creating object instance");
//            e.printStackTrace();
//        }finally {
//            closeResources(connection,statement,rs);
//        }
//        return results;
//    }
//
//
//    private Constructor<T> getConstructor() {
//        return Arrays.stream(type.getDeclaredConstructors())
//                .filter(c -> c.getParameterCount() == columnNames.size()) // Match exact parameter count
//                .map(c -> (Constructor<T>) c)
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("No suitable constructor found"));
//    }
//
//    private Field findField(Class<?> clazz, String fieldName) {
//        Class<?> current = clazz;
//        while (current != null) {
//            try {
//                return current.getDeclaredField(fieldName);
//            } catch (NoSuchFieldException e) {
//                // Move to the superclass
//                current = current.getSuperclass();
//            }
//        }
//        return null; // Field not found
//    }
//
//    protected void closeResources(Connection conn, Statement stmt, ResultSet rs) {
//        try {
//            if (rs != null) rs.close();
//            if (stmt != null) stmt.close();
//            if (conn != null) conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String toCamelCase(String s) {
//        String[] parts = s.split("_");
//        StringBuilder camelCaseString = new StringBuilder(parts[0]);
//        for (int i = 1; i < parts.length; i++) {
//            camelCaseString.append(Character.toUpperCase(parts[i].charAt(0)))
//                    .append(parts[i].substring(1));
//        }
//        return camelCaseString.toString();
//    }
//
//}