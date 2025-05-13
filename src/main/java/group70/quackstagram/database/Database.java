package group70.quackstagram.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quackstagram"; // MySQL URL
    private static final String USER = "root"; // MySQL owner
    private static final String PASSWORD = "password"; // MySQL password
    private static Connection connection = null;

    public Database() {}

    // Get or create the database connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }
        return connection;
    }

}
