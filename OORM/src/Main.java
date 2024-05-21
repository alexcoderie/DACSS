import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.lang.reflect.Field;

public class Main {
    private static final String DB_URL = "jdbc:derby://localhost/1527/OORMDB;create=true";
    private static Map<Class<?>, String> classToTableMap = new HashMap<>();
    private static Set<Class<?>> processedClasses = new HashSet<>();
    private static Map<Object, Integer> objectToIdMap = new HashMap<>();
    private static Connection conn;
    public static void main(String[] args) throws Exception {
        try {
            conn = DriverManager.getConnection(DB_URL);

            generateTables(Employee.class, Department.class);

//            Address invoiceAddress = new Address("Marului", "Timisoara");
//            Address deliveryAddress = new Address("Ciresului", "Timisoara");
//            Address invoiceAddress1 = new Address("Visinului", "Timisoara");
//            UserAccount userAccount = new UserAccount("username1234", "email@email.com", invoiceAddress, deliveryAddress);
//
//            insertData(userAccount);
//            insertData(invoiceAddress1);

            Department d1 = new Department("HR");
            Department d2 = new Department("IT");
            Employee e1 = new Employee("Maria");
            Employee e2 = new Employee("Andrei");

            e1.addDepartment(d1);
            e1.addDepartment(d2);
            e2.addDepartment(d1);

            insertData(e1);
            insertData(e2);

            conn.close();
            System.out.println("Done");
        } catch(Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void generateTables(Class <?>... classes) {
        try {
            for(Class<?> c : classes) {
                if(!processedClasses.contains(c)) {
                    createTableForHierarchy(c);
                }
            }
            createAssociationTables();
        } catch (SQLException e) {
            if(e.getSQLState().equals("X0Y32")) {
                return;
            }
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTableForHierarchy(Class<?> rootClass) throws SQLException {
        if(processedClasses.contains(rootClass)) {
            return;
        }

        String tableName = rootClass.getSimpleName().toLowerCase();
        classToTableMap.put(rootClass, tableName);
        processedClasses.add(rootClass);

        StringBuilder query = new StringBuilder("CREATE TABLE ")
                .append(tableName)
                .append(" (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, type VARCHAR(255)");

        for(Field field : rootClass.getDeclaredFields()) {
            String columnName = field.getName().toLowerCase();
            String columnType = determineColumnType(field.getType());

            if(isAggregatedType(field.getType())) {
                createTableForHierarchy(field.getType());
                String aggregatedTableName = field.getType().getSimpleName().toLowerCase();
                query.append(", ")
                        .append(columnName).append("_id INT")
                        .append(", FOREIGN KEY (").append(columnName).append("_id) REFERENCES ")
                        .append(aggregatedTableName).append("(id)");
            } else if(isCollectionType(field.getType())) {

            } else {
                query.append(", ")
                        .append(columnName)
                        .append(" ")
                        .append(columnType);
            }
        }

        if(rootClass.getSuperclass() != Object.class) {
            createTableForHierarchy(rootClass.getSuperclass());
            String parentTableName = classToTableMap.get(rootClass.getSuperclass());
            query.append(", parent_id INT, FOREIGN KEY (parent_id) REFERENCES ")
                    .append(parentTableName).append("(id)");
        }

        query.append(")");
        System.out.println("Query for " + tableName + ": " + query.toString());

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(query.toString());
        }
    }

    private static void createAssociationTables() throws SQLException {
        for(Class<?> c : classToTableMap.keySet()) {
            for(Field f: c.getDeclaredFields()) {
                if(isCollectionType(f.getType())) {
                    Class<?> associatedClass = getCollectionGenericType(f);
                    if(associatedClass != null) {
                        String tableName1 = classToTableMap.get(c);
                        String tableName2 = classToTableMap.get(associatedClass);
                        String associationTableName = tableName1 + "_" + tableName2;
                        String query = "CREATE TABLE "
                                + associationTableName + " ("
                                + tableName1 + "_id INT, "
                                + tableName2 + "_id INT, "
                                + "FOREIGN KEY (" + tableName1 + "_id) REFERENCES " + tableName1 + "(id), "
                                + "FOREIGN KEY (" + tableName2 + "_id) REFERENCES " + tableName2 + "(id)" + ")";
                        System.out.println("Query for " + associationTableName + ": " + query);

                        try (Statement statement = conn.createStatement()) {
                            statement.executeUpdate(query);
                        }
                    }
                }
            }
        }
    }

    private  static void insertData(Object obj) throws SQLException {
        Class<?> c = obj.getClass();
        String tableName = classToTableMap.get(c);

        StringBuilder query = new StringBuilder("INSERT INTO ")
                .append(tableName)
                .append(" (type");
        StringBuilder values = new StringBuilder("VALUES (?");

        List<Object> params = new ArrayList<>();
        params.add(c.getSimpleName());

        if(c.getSuperclass() != Object.class) {
            Object parentObj;
            try {
                parentObj = c.getSuperclass().getConstructor().newInstance();
                insertData(parentObj);
                query.append(", parent_id");
                values.append(", ?");
                params.add(getId(parentObj));
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (isAggregatedType(field.getType())) {
                    insertData(value);
                    query.append(", ").append(field.getName().toLowerCase()).append("_id");
                    values.append(", ?");
                    params.add(getId(value));
                } else if (isCollectionType(field.getType())) {

                } else {
                    query.append(", ").append(field.getName().toLowerCase());
                    values.append(", ?");
                    params.add(value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        query.append(") ").append(values).append(")");

        System.out.println("SQL: " + query.toString());

        try (PreparedStatement stmt = conn.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                setId(obj, generatedKeys.getInt(1));
            }
        }

        insertDataIntoAssociatedTables(obj);
    }

    private static void insertDataIntoAssociatedTables(Object obj) throws SQLException {
        Class<?> c = obj.getClass();
        String tableName1 = classToTableMap.get(c);

        for(Field f: c.getDeclaredFields()) {
            if(isCollectionType(f.getType())) {
                f.setAccessible(true);

                try {
                    Collection<?> collection = (Collection<?>) f.get(obj);

                    if(collection != null) {
                        Class<?> associatedClass = getCollectionGenericType(f);
                        String tableName2 = classToTableMap.get(associatedClass);
                        String associationTableName = tableName1 + "_" + tableName2;

                        for(Object associatedObject : collection) {
                            insertData(associatedObject);
                            String query = "INSERT INTO " + associationTableName + " (" +
                                    tableName1 + "_id, " +
                                    tableName2 + "_id) VALUES (?, ?)";

                            try (PreparedStatement stmt = conn.prepareStatement(query.toString())) {
                                stmt.setInt(1, getId(obj));
                                stmt.setInt(2, getId(associatedObject));
                                stmt.executeUpdate();
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean isCollectionType(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    private static Class<?> getCollectionGenericType(Field field) {
        try {
            return (Class<?>) ((java.lang.reflect.ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        } catch (Exception e) {
            return null;
        }
    }

    private static String determineColumnType(Class<?> type) {
        if(type == String.class) {
            return "VARCHAR(255)";
        } else if(type == int.class || type == Integer.class) {
            return "INT";
        } else if(type == boolean.class || type == Boolean.class) {
            return "BOOLEAN";
        }

        return "VARCHAR(255)";
    }

    private static boolean isAggregatedType(Class<?> type) {
        return !type.isPrimitive() && type != String.class && !isWrapperType(type) && !isCollectionType(type);
    }

    private static boolean isWrapperType(Class<?> type) {
        return type == Integer.class
                || type == Boolean.class
                || type == Byte.class
                || type == Character.class
                || type == Double.class
                || type == Float.class
                || type == Long.class
                || type == Short.class;
    }

    private static int getId(Object obj) throws SQLException {
        return objectToIdMap.getOrDefault(obj, -1);
    }

    private static void setId(Object obj, int id) throws SQLException {
        objectToIdMap.put(obj, id);
    }
}