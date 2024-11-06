package com.movie.menu;

import com.movie.model.Movie;
import com.movie.service.MovieService;
import com.movie.model.Reservation;
import com.movie.service.ReservationService;

import java.util.InputMismatchException;
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
    print();
    //TODO
    String command = getCommand();

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

          System.out.println("Enter any non-number to exit reservation page.");
          System.out.print("# of row: ");
          int row = scanner.nextInt() - 1;
          System.out.print("# of col: ");
          int col = scanner.nextInt() - 1;
          if(isReserved[row][col]){
            System.out.println("Seat is already reserved");
            return;
          }

          long key = ReservationService.createReservation(movie,row,col);
          if( key < 0){
            System.out.println("Reservation interrupted unexpectedly. Sorry");
          }else{
            System.out.println("Reservation Completed");
            System.out.println("key is: " + key);
          }
          // it has internal vulnerabilities if concurrent main menu
          // Since we check reserved at local not from database, it might make some error.
          // should be handled at this scope, something like transaction to check seat and insert.
        }catch(InputMismatchException ignored){
        }catch(IndexOutOfBoundsException e){
          System.out.println("Input data out of range");
        }
        break;
      case "2":
        System.out.println("CHECK YOUR RESERVATION");
        System.out.print("Reservation ID: ");
        try{
          long id = scanner.nextLong();
          Reservation reservation = ReservationService.readReservation(id);
          if(reservation == null){
            System.out.println("No reservation found with given ID");
            return;
          }
          System.out.println(reservation.toListEntry());
          System.out.println("Enter 1 to modify your entry");
          System.out.println("Enter other word to exit");
          while(true){
            String additionalCommand = getCommand();
            if( ! additionalCommand.equals("1")) break;
            //TODO feature
            // show seat state
            System.out.print("# of row: ");
            int row = scanner.nextInt() - 1;
            System.out.print("# of col: ");
            int col = scanner.nextInt() - 1;
            try{
              ReservationService.updateReservation(reservation, row, col);
            } catch (RuntimeException e){
              System.out.println("That seat is already reserved");
              continue;
            }
            break;
          }
        }catch (InputMismatchException e){
          System.out.println("No reservation found with given ID ");
        }
        break;
      case "3":
        System.out.println("RESERVATION DELETE");
        System.out.print("Reservation ID: ");
        try{
          long id = scanner.nextLong();
          ReservationService.deleteReservation(id);
        }catch (InputMismatchException e){
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
