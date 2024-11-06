package com.movie.service;

import com.movie.database.DatabaseConnectionPool;
import com.movie.model.Movie;
import com.movie.model.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
Logics for sql
no exception handling this scope.

 */

public class ReservationService {
  public static long createReservation(Movie movie, int row, int col){
    String sql = "INSERT INTO reservation (id, movie_id, seat_row, seat_col, reserve_timestamp) VALUES (?, ?, ?, ?, ?)";
    try(
      Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      long key = (new Date()).getTime() - row << 3 + col << 5;
      // key generated here - should return here | separate logic.
      preparedStatement.setLong(1, key);
      preparedStatement.setInt(2, movie.getId());
      preparedStatement.setInt(3, row);
      preparedStatement.setInt(4, col);
      preparedStatement.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
      int i = preparedStatement.executeUpdate();
      if(i > 0) return key;
      else return -1;

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<Reservation> readAll(){
    // For Admin usage.
    // Add User feature?
    String sql = "SELECT id, title, seat_row, seat_col, reserve_timestamp " +
      "FROM reservation_list";
    List<Reservation> reservations = new ArrayList<>();
    try(
      Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      ResultSet resultSet = preparedStatement.executeQuery()
      ){
      while(resultSet.next()){
        Reservation reservation = new Reservation(
          resultSet.getLong("id"),
          resultSet.getString("title"),
          resultSet.getInt("seat_row"),
          resultSet.getInt("seat_col"),
          resultSet.getTimestamp("reserve_timestamp")
        );
        reservations.add(reservation);
      }
      return reservations;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static Reservation readReservation(long id){
    String sql = "SELECT id, title, seat_row, seat_col, reserve_timestamp " +
      "FROM reservation_list " +
      "WHERE id = ?";

    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setLong(1, id);
      try(ResultSet resultSet = preparedStatement.executeQuery()){
        if(resultSet.next()){
          return new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("title"),
            resultSet.getInt("seat_row"),
            resultSet.getInt("seat_col"),
            resultSet.getTimestamp("reserve_timestamp")
          );
        } else return null;
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean[][] checkRemainingSeat(Movie movie){
    String sql = "SELECT seat_row, seat_col " +
      "FROM reservation " +
      "WHERE movie_id = ?";

    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setLong(1, movie.getId());

      try(ResultSet resultSet = preparedStatement.executeQuery()){
        boolean[][] isReserved = new boolean[Reservation.MAX_ROW][Reservation.MAX_COL];
        while(resultSet.next()){
          int r = resultSet.getInt("seat_row");
          int c = resultSet.getInt("seat_col");
          isReserved[r][c] = true;
        }
        return isReserved;
      }catch(IndexOutOfBoundsException e){
        throw new RuntimeException("DB Data Error occurred");
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean verifySeat(long movieID, int row, int col){
    String sql = "SELECT id " +
      "FROM reservation " +
      "WHERE movie_id = (SELECT movie_id FROM reservation WHERE id = ?) " +
      "and seat_row = ? " +
      "and seat_col = ?";
    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setLong(1,movieID);
      preparedStatement.setInt(2,row);
      preparedStatement.setInt(3,col);

      try(
        ResultSet resultSet = preparedStatement.executeQuery()
        ){
        return !resultSet.next();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void updateReservation(Reservation reservation, int row, int col){
    if(!verifySeat(reservation.getId(), row, col))
      throw new RuntimeException("That seat is already reserved");

    String sql = "UPDATE reservation " +
      "SET seat_ROW = ?," +
      "    seat_col = ?," +
      "    reserve_timestamp = current_timestamp " +
      "WHERE id = ?";

    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setInt(1, row);
      preparedStatement.setInt(2, col);
      preparedStatement.setLong(3, reservation.getId());

      preparedStatement.executeUpdate();
      reservation.setReserveTimestamp(new Date()); //local data and db data will mismatch.
      reservation.setRow(row);
      reservation.setCol(col);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static int deleteReservation(long id){
    String sql = "DELETE from reservation " +
      "WHERE id = ?";
    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setLong(1,id);
      int rows = preparedStatement.executeUpdate();
      if(rows == 0) System.out.println("No data was deleted. Check reservation id");
      else System.out.println("Deleted your reservation. Wish to see you again.");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return 0;
  }
}
