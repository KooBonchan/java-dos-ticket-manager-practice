package com.movie;


import lombok.Getter;

import java.util.Date;

@Getter
public class Reservation {

  /*
   * Reservation
   * id        hashedInteger - string(15)
   * movieId   hashedInteger - string(10) >- Movie(id)
   * seatRow   integer
   * seatCol   integer
   * reserveDate date
   */
  long id;
  int movieId;
  int row;
  int col;
  Date reserveDate;
}
