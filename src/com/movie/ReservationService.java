package com.movie;

import com.movie.database.DatabaseConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/*
Logics for sql
no exception handling this scope.

 */

public class ReservationService {
  public static boolean createReservation(Movie movie, int row, int col){
    String sql = "INSERT INTO reservation (id, movie_id, seat_row, seat_col, reserve_date) VALUES (?, ?, ?, ?, ?)";
    try(
      Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
    ) {
      preparedStatement.setLong(1, (new Date()).getTime() - row << 3 + col << 5);
      preparedStatement.setInt(2, movie.getKey());
      preparedStatement.setInt(3, row);
      preparedStatement.setInt(4, col);
      preparedStatement.setDate(5, new java.sql.Date(new Date().getTime()));
      int i = preparedStatement.executeUpdate();
      return i > 0;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void printAll(){
    // For Admin usage.
    // Add User feature?
    String sql = "SELECT id, title, seat_row, seat_col, reserve_date " +
      "FROM reservation_list";
    try(
      Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      ResultSet resultSet = preparedStatement.executeQuery()
      ){
      while(resultSet.next()){
        Reservation reservation = new Reservation.ReservationBuilder()
          .id(resultSet.getLong("id"))
          .movieTitle(resultSet.getString("title"))
          .row(resultSet.getInt("seat_row"))
          .col(resultSet.getInt("seat_col"))
          .reserveDate(resultSet.getDate("reserve_date"))
          .build();
        System.out.println(reservation.toListEntry());
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void readReservation(long id){
    String sql = "SELECT id, title, seat_row, seat_col, reserve_date " +
      "FROM reservation_list " +
      "WHERE id = ?";

    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setLong(1, id);
      try(ResultSet resultSet = preparedStatement.executeQuery()){
        if(resultSet.next()){
          Reservation reservation = new Reservation.ReservationBuilder()
            .id(resultSet.getLong("id"))
            .movieTitle(resultSet.getString("title"))
            .row(resultSet.getInt("seat_row"))
            .col(resultSet.getInt("seat_col"))
            .reserveDate(resultSet.getDate("reserve_date"))
            .build();
        }
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
      preparedStatement.setLong(1, movie.getKey());

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

  public static boolean verifySeat(long id, int row, int col){
    String sql = "SELECT id " +
      "FROM reservation " +
      "WHERE movie_id = (SELECT movie_id FROM reservation WHERE id = ?) " +
      "and seat_row = ? " +
      "and seat_col = ?";
    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setLong(1,id);
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
      "    reserve_date = current_date " +
      "WHERE id = ?";

    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setInt(1, row);
      preparedStatement.setInt(2, col);
      preparedStatement.setLong(3, reservation.getId());

      preparedStatement.executeUpdate();
      reservation.setReserveDate(new Date()); //local data and db data will mismatch.
      reservation.setRow(row);
      reservation.setCol(col);
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
      int rows = preparedStatement.executeUpdate();
      if(rows == 0) System.out.println("No data was deleted. Check reservation id");
      else System.out.println("Deleted your reservation. Wish to see you again.");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
