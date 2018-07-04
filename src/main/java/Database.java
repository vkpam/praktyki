import java.sql.*;

public class Database {

    private static Connection conn;

    public static void connect() throws SQLException {
        String url = "jdbc:sqlite:Database.db";
        conn = DriverManager.getConnection(url);
    }

    public static void sendQueryToDB(String query) throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute(query);
    }

    public static ResultSet select(String sql) throws SQLException {
        Statement statement = conn.createStatement();
        return statement.executeQuery(sql);
    }

    public static void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Error while disconnecting the database: " + e.toString());
        }
    }
}
