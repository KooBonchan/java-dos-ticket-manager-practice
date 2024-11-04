package com.movie;

import com.movie.database.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MovieService {
  public static void createMovie(Movie movie){
    String sql = "INSERT INTO movie (key, title, genre, start_date, end_date) " +
      "VALUES (?, ?, ?, ?, ?)";

    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setInt(1, movie.getKey());
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
    String sql = "READ title, genre, start_date, end_date " +
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
    String sql = "READ id, title, genre, start_date, end_date " +
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

  public static void deleteMovie(int key) {
    String sql = "DELETE FROM movie " +
      "WHERE id = ? ";
    try(Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setInt(1, key);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
