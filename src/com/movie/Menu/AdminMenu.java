package com.movie.Menu;

import com.movie.Movie;
import com.movie.MovieService;
import com.movie.ReservationService;

import java.util.*;

class AdminMenu extends AbstractMenu{
  static boolean verifyPassword(String password){
    return password != null && password.equals(
      "aprieta" // hardcoded string!!! vulnerable
    );
  }

  @Override
  public void print() {
    System.out.println("""
      ADMINADMINADMINADMINADMINADMINADMINADMINADMINAD
      1. 영화관리 | 2. 예매확인 | 3.   홈으로 | 0.    종료
      --------------type password to enter Admin menu
      ADMINISTRATORADMINISTRATORADMINISTRATORADMINIST
      """);
  }

  @Override
  public void execute() {
    String command;
    do{
      command= scanner.nextLine().strip();
    }while(command.isEmpty());

    switch(command){
      case "1":
        List<Movie> movies = MovieService.readAll();
        //Todo : Somehow Fix your code smell.
        //Movie service - read all
        //reservation service - print all
        //both read all requires admin
        //in principle, it should be read all only for code consistency
        for(int i = 0; i < movies.size(); i++){
          System.out.println(String.format("%03d",i + 1)
            + movies.get(i).toListEntry());
        }
        System.out.println("""
        ADMINADMINADMINADMINADMINADMINADMINADMINADMINAD
        1. 영화추가 | 2. 영화변경 | 3. 영화삭제 | 0. 뒤로가기
        --------------type password to enter Admin menu
        ADMINISTRATORADMINISTRATORADMINISTRATORADMINIST
        """);
        //Todo: separate menu to other class
        //constraint - after manipulation, need to go back to admin menu, which requires password in current impl
        //solution - stack nav structure on MenuManager.
        //  admin, home on same level, movie manage page stack on admin
        //  nav structure expandable to additional features.
        //Todo: Stack Nav structure on MenuManager
        {
          String additionalCommand; // this will have name command if refactored to another class
          do{
            additionalCommand= scanner.nextLine().strip();
          }while(additionalCommand.isEmpty());

          switch(additionalCommand) {
            case "1":
              System.out.println("MOVIE ADD");
              System.out.print("title: ");String title = scanner.nextLine();
              System.out.print("genre: ");String genre = scanner.nextLine();
              int key = new GregorianCalendar().get(GregorianCalendar.DATE);
              key += (int)(Math.random() * 942607);
              // id generation vulnerable, not unique.
              // hash title + genre?
              Movie.create(key, title, genre, null, null)
                .ifPresentOrElse(
                  MovieService::createMovie,
                  () -> {
                    System.out.println("No genre matching for your input");
                    System.out.println("Available genres: Fantasy, Romance, Bass");
                  });
              break;
            case "3":
              System.out.println("MOVIE DELETE");
              System.out.print("movie index: ");
              try{
                int index = scanner.nextInt();
                MovieService.deleteMovie(movies.get(index).getKey());
              }catch (NumberFormatException e){
                System.out.println("Wrong ID Format");
              }catch (IndexOutOfBoundsException e){
                System.out.println("No movie there");
              }
              break;
            case "0":
              return;
          }
        }
        break;
      case "2":
        ReservationService.printAll();
        break;
      case "3":
        MenuManager.toMainMenu();
        break;
      case "0":
        MenuManager.haltProgram();
        break;
    }
  }
}
