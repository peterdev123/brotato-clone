package MySql;


import com.mygdx.game.MySql.MySqlConnection;
import com.mygdx.game.MySql.Rankings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReadData {
    public static void main(String[] args) {
        try(Connection c = com.mygdx.game.MySql.MySqlConnection.getConnection();
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

    public List<Rankings> readData(){

        List<Rankings> rankList = new ArrayList<>();
        try(Connection c = MySqlConnection.getConnection();
            Statement statement = c.createStatement()){
            String query = "Select * FROM dbexile";
            String rank;
            String score;
            String username;
            ResultSet res = statement.executeQuery(query);
            while(res.next()){
                int id = res.getInt("id");
                username = res.getString("username");
                int scores = Integer.parseInt(score = res.getString("score"));
                int ranks = Integer.parseInt(rank = res.getString("rank"));
                System.out.println("ID: "+ id);
                System.out.println("Username: " + username);
                System.out.println("Score: " + score);
                System.out.println("Rank: " + rank);
                rankList.add(new Rankings(username, scores, ranks));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rankList;
    }
}

