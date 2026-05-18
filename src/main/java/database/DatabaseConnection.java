package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static final String URL = "postgresql://neondb_owner:npg_zbGepchkdQ78@ep-solitary-butterfly-aq5mpxtd-pooler.c-8.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require";
    private static final String DB_URL = "jdbc:postgresql://ep-solitary-butterfly-aq5mpxtd-pooler.c-8.us-east-1.aws.neon.tech:5432/neondb";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_zbGepchkdQ78";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy kết nối đến database
     * @return Connection object
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    /**
     * Test kết nối database
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Kết nối PostgreSQL thành công!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Kết nối PostgreSQL thất bại: " + e.getMessage());
        }
        return false;
    }
}
