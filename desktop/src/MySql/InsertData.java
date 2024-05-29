package MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertData {
    //FOR DEBUG
    public static void main(String[] args) {
        try(Connection c = MySqlConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT into dbexile (username, score,rank) VALUES (?,?,?)")){
            String username = "Kyle T. Vasquez";
            String score = "1234567890";
            String rank = "1";
            statement.setString(1,username);
            statement.setString(2, score);
            statement.setString(3, rank);
            int rows = statement.executeUpdate();

            if(rows > 0){
                System.out.println("Rows inserted: " + rows);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}