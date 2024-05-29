package com.mygdx.game.MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteData {

    public void deleteLowestRanking(Rankings ranking) throws SQLException {
        try (Connection connection = MySqlConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM dbexile WHERE username = ? AND score = ?")) {

            preparedStatement.setString(1, ranking.getUsername());
            preparedStatement.setInt(2, ranking.getScore());
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Deleted lowest ranking player: " + ranking.getUsername());
            }
        }
    }
}
