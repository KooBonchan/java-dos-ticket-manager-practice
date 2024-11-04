package com.movie;

/*
* ---------------------------------------------------
* Writer: KooBonchan
* Reference Project: MovieReservationReference Program
* Start date: 241031
*
* ---------------------------------------------------
*
* Project Structure
* View - menu<Interface>
*        menu<Abstract> - (MainMenu, AdminMenu)
* Data - Reservation, Seats, Movie
* Main  -ref-> (Reservation, Seats)
* Movie -ref-> (Movie)
*
* IO   - (Reservation, Movie)
*  as txt file, at project root directory
*  currently using fileIO
*  which should be integrated to DB.
*
*
* ---------------------------------------------------
* Project Flow - 2 State machine
*
* Main Menu
*   Reserve a movie
*   Check Reservation
*   Cancel Reservation
*   -> AdminMenu, requires password
*
* Admin Menu
*   Register a movie
*   Check movie list
*   Delete movie
*   + CRUD reservation list
*   -> Main menu
*
* ---------------------------------------------------
* Data type
*
* Movie
* id        hashedInteger - string(10)
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
* */


import com.movie.Menu.Menu;
import com.movie.Menu.MenuManager;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    while(true){
      if(MenuManager.isEnd()) break;
      Menu current = MenuManager.getCurrent();
      current.print();

      String command;
      do{
        command = scanner.nextLine();
      }while(command.isBlank());
      current.execute(command);
    }
    System.out.println("Program Halts...");
  }
}