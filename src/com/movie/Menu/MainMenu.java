package com.movie.Menu;

import com.movie.Movie;
import com.movie.MovieService;
import com.movie.Reservation;
import com.movie.ReservationService;

import java.util.Date;
import java.util.List;

class MainMenu extends AbstractMenu {
  @Override
  public void print() {
    System.out.println("""
      -----------------------------------------------
      1. 영화예매 | 2. 예매확인 | 3. 예매취소 | 0.    종료
      
      --------------type password to enter Admin menu
      -----------------------------------------------
      """);
  }

  @Override
  public void execute() {
    //TODO
    String command;
    do{
      command= scanner.nextLine().strip();
    }while(command.isEmpty());

    switch(command){
      case "1":
        List<Movie> movies = MovieService.readAll();

        for(int i = 0; i < movies.size(); i++){
          System.out.println(String.format("%03d",i + 1)
            + movies.get(i).toListEntry());
        }

        try{
          System.out.println("Enter a index of movie");
          System.out.print("index: ");
          int index = scanner.nextInt();
          Movie movie = movies.get(index - 1);
          boolean[][] isReserved =ReservationService.checkRemainingSeat(movie);
          Reservation.printAvailableSeats(isReserved);

          System.out.print("# of row: ");
          int row = scanner.nextInt() - 1;
          System.out.print("# of col: ");
          int col = scanner.nextInt() - 1;
          if(isReserved[row][col]){
            System.out.println("Seat is already reserved");
            return;
          }

          if(! ReservationService.createReservation(movie,row,col)){
            System.out.println("Reservation interrupted unexpectedly. Sorry");
          }else{
            System.out.println("Reservation Completed");
          }

          // it has internal vulnerabilities if concurrent
          // Since we check reserved at local not from database, it might make some error.
          // should be handled at this scope, something like transaction to check seat and insert.


        }catch(NumberFormatException e){
          System.out.println("Wrong command");
        }catch(IndexOutOfBoundsException e){
          System.out.println("Input data out of range");
        }
        break;
      case "2":
        break;
      case "3":
        System.out.println("RESERVATION DELETE");
        System.out.print("Reservation ID: ");
        try{
          long id = scanner.nextLong();
          ReservationService.deleteReservation(id);
        }catch (NumberFormatException e){
          System.out.println("Wrong ID Format");
        }
        break;
      case "0":
        MenuManager.haltProgram();
        break;
      default:
        MenuManager.toAdminMenu(command);
    }
  }
}
