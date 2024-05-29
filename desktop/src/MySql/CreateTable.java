package MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    public static void main(String[] args) {
        createTable(); // Call the createTable method
    }

    public static void createTable() {
        try (Connection c = MySqlConnection.getConnection();
             Statement statement = c.createStatement()) {

            String query = "CREATE TABLE IF NOT EXISTS dbexile (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," + // Corrected column name to lowercase
                    "score INT NOT NULL," + // Removed unnecessary size specification
                    "rank INT NOT NULL)";
            statement.execute(query);
            System.out.println("Table created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

