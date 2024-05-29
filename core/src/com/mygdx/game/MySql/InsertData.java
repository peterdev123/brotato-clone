package com.mygdx.game.MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InsertData {

    public void setData(String username, String score) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement insertStatement = c.prepareStatement(
                     "INSERT INTO dbexile (username, score, rank) VALUES (?, ?, ?)")) {

            int scoreValue = Integer.parseInt(score);
            if (!isScoreHighEnough(scoreValue)) {
                System.out.println("Score not high enough for top 10.");
                return;
            }

            insertStatement.setString(1, username);
            insertStatement.setInt(2, scoreValue);
            insertStatement.setInt(3, 0); // Temporary rank, will be updated later
            insertStatement.executeUpdate();

            updateRankings();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isScoreHighEnough(int score) throws SQLException {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT score FROM dbexile ORDER BY score DESC LIMIT 10");
             ResultSet resultSet = statement.executeQuery()) {
            int count = 0;
            while (resultSet.next()) {
                count++;
                if (resultSet.getInt("score") <= score) {
                    return true;
                }
            }
            return count < 10;
        }
    }

    private void updateRankings() throws SQLException {
        UpdateData updateData = new UpdateData();
        ReadData readData = new ReadData();
        List<Rankings> rankings = readData.readData();
        updateData.updateRanking(rankings);

        if (rankings.size() > 10) {
            Rankings lowestRanking = rankings.get(10);
            DeleteData deleteData = new DeleteData();
            deleteData.deleteLowestRanking(lowestRanking);
        }
    }
}
