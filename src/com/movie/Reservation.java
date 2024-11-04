package com.movie;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class Reservation {
  public static final int MAX_ROW = 10;
  public static final int MAX_COL = 13;

  /*
   * Reservation
   * id        hashedInteger - string(15)
   * movieId   hashedInteger - string(10) >- Movie(id)
   * seatRow   integer
   * seatCol   integer
   * reserveDate date
   */
  private final long id;
  private final String movieTitle;
  @Setter
  private int row;
  @Setter
  private int col;
  @Setter
  private Date reserveDate;

  @Builder
  public Reservation(long id, String movieTitle, int row, int col, Date reserveDate) {
    this.id = id;
    this.movieTitle = movieTitle;
    this.row = row;
    this.col = col;
    this.reserveDate = reserveDate;
  }

  public String toListEntry(){
    return String.format("%-15d | %20s | %2d | %2d | %15s",
      id, movieTitle, row, col,
      new SimpleDateFormat("yyMMdd HH:mi")
        .format(reserveDate)
    );
  }

  public static void printAvailableSeats(boolean[][] isReserved){
    // index starts from 1
    if(isReserved.length != MAX_ROW
    || isReserved[0].length != MAX_COL) {
      System.out.println("Wrong Theatre You've come");
    }

    for(int r = 0; r < MAX_ROW; r++){
      StringBuilder output = new StringBuilder()
        .append(String.format("%3d", r+1))
        .append(" | ");
      for(int c = 0; c < MAX_COL; c++){
        output.append(
          isReserved[r][c] ? " X " : " O "
        );
      }
      System.out.println(output);
    }
    System.out.println("row");
    System.out.print("col | ");
    for(int i = 1; i <= MAX_COL; i++) System.out.printf("%2d ", i);
    System.out.println();
  }
}
