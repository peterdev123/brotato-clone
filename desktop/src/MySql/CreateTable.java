package MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    public static void main(String[] args) {
        try(Connection c = MySqlConnection.getConnection();
            Statement statement = c.createStatement()){

            String query = "CREATE TABLE IF NOT EXISTS dbexile (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "Username VARCHAR(50) NOT NULL," +
                    "score INT(100000) NOT NULL)," +
                    "Rank VARCHAR(50) NOT NULL";
            statement.execute(query);
            System.out.println("Table created successfully!");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void createTable() {
        try(Connection c = MySqlConnection.getConnection();
            Statement statement = c.createStatement()){

            String query = "CREATE TABLE IF NOT EXISTS dbexile (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
            "username VARCHAR(50) NOT NULL," +
                    "score INT(100000) NOT NULL)," +
                    "rank VARCHAR(50) NOT NULL";
            statement.execute(query);
            System.out.println("Table created successfully!");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }



}
