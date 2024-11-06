package com.movie.menu;

import com.movie.model.Movie;
import com.movie.service.MovieService;

import java.util.GregorianCalendar;
import java.util.List;

public class MovieMenu extends AbstractMenu{
  List<Movie> movies;

  public MovieMenu() {
    //not callable from anywhere, but from AdminMenu only
    movies = MovieService.readAll();
    for(int i = 0; i < movies.size(); i++){
      System.out.println(String.format("%03d",i + 1)
        + movies.get(i).toListEntry());
    }
  }

  @Override
  public void print() {
    System.out.println("""
    ADMINADMINADMINADMINADMINADMINADMINADMINADMINAD
    1. 영화추가 | 2. 영화변경 | 3. 영화삭제 | 0. 뒤로가기
    --------------type password to enter Admin menu
    ADMINISTRATORADMINISTRATORADMINISTRATORADMINIST
    """);
  }

  @Override
  public void execute() {
    String command; // this will have name command if refactored to another class
    do{
      command= scanner.nextLine().strip();
    }while(command.isEmpty());

    switch(command) {
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
}
