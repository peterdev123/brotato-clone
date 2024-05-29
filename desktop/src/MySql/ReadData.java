package MySql;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadData {
    public static void main(String[] args) {
        try(Connection c = MySqlConnection.getConnection();
            Statement statement = c.createStatement()){
            String query = "Select * FROM dbexile";
            ResultSet res = statement.executeQuery(query);
            while(res.next()){
                int id = res.getInt("id");
                String username = res.getString("username");
                String score = res.getString("score");
                String rank = res.getString("rank");
                System.out.println("ID: "+ id);
                System.out.println("Username: " + username);
                System.out.println("Score: " + score);
                System.out.println("Rank: " + rank);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

