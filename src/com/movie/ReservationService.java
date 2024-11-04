package com.movie;

import com.movie.database.DatabaseConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
Logics for sql
no exception handling this scope.

 */

public class ReservationService {
  public static void createReservation(Movie movie, Reservation reservation){
    String sql = "INSERT INTO reservation () VALUES ()";
    try(
      Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
    ) {
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean verifySeat(int movieId, int row, int col){
    String sql = "READ id " +
      "FROM reservation " +
      "WHERE movie_id = ? " +
      "and seat_row = ? " +
      "and seat_col = ?";
    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setInt(1,movieId);
      preparedStatement.setInt(2,row);
      preparedStatement.setInt(3,col);

      try(
        ResultSet resultSet = preparedStatement.executeQuery()
        ){
        if(resultSet.next()) return false;
        return true;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void updateReservation(Reservation reservation, int row, int col){
    if(!verifySeat(reservation.movieId, row, col))
      throw new RuntimeException("No such.");

    String sql = "UPDATE reservation " +
      "SET seat_ROW = ?," +
      "    seat_col = ? " +
      "WHERE id = ?";

    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setInt(1, row);
      preparedStatement.setInt(2, col);
      preparedStatement.setLong(3, reservation.getId());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void deleteReservation(long id){
    String sql = "DELETE from reservation " +
      "WHERE id = ?";
    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setLong(1,id);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
