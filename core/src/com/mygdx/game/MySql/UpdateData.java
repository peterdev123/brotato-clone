package com.mygdx.game.MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpdateData {

    public void updateRanking(List<Rankings> rankings) throws SQLException {
        Collections.sort(rankings, Comparator.comparingInt(Rankings::getScore).reversed());

        try (Connection connection = MySqlConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE dbexile SET rank = ? WHERE username = ?")) {

            for (int i = 0; i < rankings.size(); i++) {
                Rankings ranking = rankings.get(i);
                ranking.setRank(i + 1); // Update rank based on position in sorted list

                preparedStatement.setInt(1, ranking.getRank());
                preparedStatement.setString(2, ranking.getUsername());
                preparedStatement.addBatch();
            }

            int[] rowsUpdated = preparedStatement.executeBatch();
            System.out.println(rowsUpdated.length + " rows updated successfully!");
        }
    }
}
