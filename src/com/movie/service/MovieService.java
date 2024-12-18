package com.movie.service;

import com.movie.database.DatabaseConnectionPool;
import com.movie.model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MovieService {
  public static void createMovie(Movie movie){
    String sql = "INSERT INTO movie (id, title, genre, start_date, end_date) " +
      "VALUES (?, ?, ?, ?, ?)";

    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setInt(1, movie.getId());
      preparedStatement.setString(2, movie.getTitle());
      preparedStatement.setString(3, movie.getGenre());

      if (movie.getStartDate() != null) {
        preparedStatement.setDate(4, new Date(movie.getStartDate().getTime()));
      } else {
        preparedStatement.setNull(4, Types.DATE);
      }

      if (movie.getEndDate() != null) {
        preparedStatement.setDate(5, new Date(movie.getEndDate().getTime()));
      } else {
        preparedStatement.setNull(5, Types.DATE);
      }

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static Movie readMovie(int key) throws NoSuchElementException {
    String sql = "SELECT title, genre, start_date, end_date " +
      "FROM movie " +
      "WHERE id = ? ";

    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setInt(1, key);
      try(
        ResultSet resultSet = preparedStatement.executeQuery()
      ){
        if(resultSet.next()){
          return Movie.create(
            key,
            resultSet.getString("title"),
            resultSet.getString("genre"),
            resultSet.getDate("start_date"),
            resultSet.getDate("end_date")
          ).orElseThrow();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    throw new NoSuchElementException("Could not find movie with given key");
  }

  public static List<Movie> readAll(){
    String sql = "SELECT id, title, genre, start_date, end_date " +
      "FROM movie ";

    List<Movie> movies = new ArrayList<>();
    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      try(
        ResultSet resultSet = preparedStatement.executeQuery()
      ){
        while(resultSet.next()){
          Movie.create(
            resultSet.getInt("id"),
            resultSet.getString("title"),
            resultSet.getString("genre"),
            resultSet.getDate("start_date"),
            resultSet.getDate("end_date")
          ).ifPresent(movies::add);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return movies;
  }

  public static void updateMovie(Movie movie){
    String sql = "UPDATE movie " +
      "SET title=?, genre=?, start_date=?, end_date=? " +
      "WHERE id=?";

    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setString(1, movie.getTitle());
      preparedStatement.setString(2, movie.getGenre());


      if (movie.getStartDate() != null) {
        preparedStatement.setDate(3, new Date(movie.getStartDate().getTime()));
      } else {
        preparedStatement.setNull(3, Types.DATE);
      }
      if (movie.getEndDate() != null) {
        preparedStatement.setDate(4, new Date(movie.getEndDate().getTime()));
      } else {
        preparedStatement.setNull(4, Types.DATE);
      }
      preparedStatement.setInt(5, movie.getId());

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void deleteMovie(int id) {
    String sql = "DELETE FROM movie " +
      "WHERE id = ? ";
    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setInt(1, id);
      int rows = preparedStatement.executeUpdate();
      if(rows == 0) System.out.println("No data was deleted. Reload page.");
      else System.out.println("Deleted movie. Wish to see you again.");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  public static void deleteMovie(Movie movie){
    if(movie != null) deleteMovie(movie.getId());
  }
}