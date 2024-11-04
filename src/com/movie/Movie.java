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
    Romance("Romance"),
    Bass("Bass")
    ;
    String name;
    Genre(String name){
      this.name = name;
    }
    static Genre fromString(String input){
      return switch(input){
        case "Fantasy" -> Fantasy;
        case "Horror" -> Horror;
        case "Romance" -> Romance;
        case "Bass" -> Bass;
        default -> null;
      };
    }

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

  private Movie(int key, String title, Genre genre, Date startDate, Date endDate) {
    this.key = key;
    this.title = title;
    this.genre = genre;
    this.startDate = startDate;
    this.endDate = endDate;
  }
  public static Optional<Movie> create(
    int key, String title, String genre, Date startDate, Date endDate
  ){
    if(Genre.fromString(genre) == null){
      return Optional.empty();
    }
    return Optional.of(new Movie(key, title, Genre.fromString(genre), startDate, endDate));
    // to handle value with wrong genre.
    // since genre not important data, using it as string is a better approach
    // though, keeping control with genre will give some sort/filter feature.
  };

  public String toListEntry(){
    return String.format(" | %20s | %10s", title, genre.toString());
  }
}
