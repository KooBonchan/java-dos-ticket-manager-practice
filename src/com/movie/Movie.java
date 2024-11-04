package com.movie;

import lombok.Getter;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/*
* Movie
* id        hashedInteger - string(10)
* title     String
* genre     Enum
* startDate date
* endDate   date
*/

@Getter
public class Movie {
  private enum Genre{
    //constraint: length 10 currently
    Fantasy("Fantasy"),
    Horror("Horror"),
    ;
    int id;
    String name;
    Genre(String name){
      this.name = name;
    }
    Date startDate;


    @Override
    public String toString() {
      return name;
    }
  }

  private int key;
  private String title;
  private Genre genre;
  Date startDate;
  Date endDate;

  public String getGenre() {
    return genre.toString();
  }

  private Movie(){};
  public static Optional<Movie> create(
    int key, String title, String genre, Date startDate, Date endDate
  ){
  return Optional.of(new Movie());
    // to handle value with wrong genre.
    // since genre not important data, using it as string is a better approach
    // though, keeping control with genre will give some sort/filter feature.
  };

}
