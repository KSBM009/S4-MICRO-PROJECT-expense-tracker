import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App{
    public static void main(String[] args){
        Connection conn = null;

        try{
            // Load the MySQL Connector/J driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the MySQL database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_s4_mini_project", "root", "4112003");

            // Connection successful
            System.out.println("Connected to the database!");

            // Perform database operations...

        } catch (SQLException | ClassNotFoundException e) {
            // Connection error
            e.printStackTrace();
        } finally {
            // Close the connection
            if (conn != null) {
                try{
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}