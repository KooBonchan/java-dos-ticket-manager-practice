package Movie;

import lombok.Data;

import java.util.Optional;

/*
id        hashedInteger - string(10)
* title     String
* genre     Enum
* startDate date
* endDate   date
*
* Reservation
* id        hashedInteger - string(15)
* movieId   hashedInteger - string(10) >- Movie(id)
* seatRow   integer
* seatCol   integer
* reserveDate date
 */

public class Movie {
  private enum Genre{
    Fantasy("Fantasy"),
    Horror("Horror"),
    ;
    String name;
    Genre(String name){
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  private String title;


  private Movie(

  ){};
  public Optional<Movie> create(){
    return Optional.of(new Movie());
    // to handle value with wrong genre.
    // since genre not important data, using it as string is a better approach
    // though, keeping control with genre will give some sort/filter feature.
  };

}
