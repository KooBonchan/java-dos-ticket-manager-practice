package com.movie.menu;

import com.movie.model.Movie;
import com.movie.service.MovieService;

import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.List;

// TODO: Service do not handle sql error, throw.
//  need to handle
class MovieMenu extends AbstractMenu{
  List<Movie> movies;

  @Override
  public void print() {
    System.out.println("""
    ADMINADMINADMINADMINADMINADMINADMINADMINADMINAD
    1. 영화추가 | 2. 영화변경 | 3. 영화삭제 | 0. 뒤로가기
    GWANLIZAGWANLIZAGWANLIZAGWANLIZAGWANLIZAGWANLIZ
    ADMINISTRATORADMINISTRATORADMINISTRATORADMINIST
    """);
  }

  @Override
  public void execute() {
    movies = MovieService.readAll();
    for(int i = 0; i < movies.size(); i++){
      System.out.println(String.format("%03d",i + 1)
        + movies.get(i).toListEntry());
    }
    print();

    String command = getCommand();

    switch(command) {
      case "1": {
        System.out.println("MOVIE ADD");
        System.out.print("title: ");
        String title = scanner.nextLine();
        if(title.isBlank()){
          System.out.println("Title is too short");
          return;
        }
        System.out.print("genre: ");
        String genre = scanner.nextLine();
        int key = new GregorianCalendar().get(GregorianCalendar.DATE);
        key += (int) (Math.random() * 942607);
        // id generation vulnerable, not unique.
        // hash title + genre?
        // refactor hashing to other method.
        // key just auto increment and get from db?
        Movie.create(key, title, genre, null, null)
          .ifPresentOrElse(
            MovieService::createMovie,
            () -> {
              System.out.println("No genre matching for your input");
              System.out.println("Available genres: Fantasy, Romance, Bass");
            });
      }
        break;

      case "2": {
        System.out.println("MOVIE EDIT");
        try {
          //TODO
          // If project enough feasible, refactor to a diff menu
          System.out.print("Choose index: ");
          int index = Integer.parseInt(scanner.nextLine()) - 1;
          Movie movie = movies.get(index);
          System.out.println("""
            ADMINADMINADMINADMINADMINADMINADMINADMINADMINAD
            1. 제목변경 | 2. 장르변경 | 3. 영화삭제 | 0. 뒤로가기
            GWANLIZAGWANLIZAGWANLIZAGWANLIZAGWANLIZAGWANLIZ
            ADMINISTRATORADMINISTRATORADMINISTRATORADMINIST
            """);
          String additionalCommand = getCommand();
          switch (additionalCommand) {
            case "1":
              System.out.print("title: ");
              String title = scanner.nextLine();
              if(title.isBlank()){
                System.out.println("Title is too short");
                return;
              }
              movie.setTitle(title);
              break;
            case "2":
              System.out.print("genre: ");
              String genre = scanner.nextLine();
              try{
                movie.trySetGenre(genre);
              } catch (RuntimeException e){
                System.out.println("no such genre");
                return;
              }
              break;
            case "3":
              MovieService.deleteMovie(movies.get(index).getId());
            default:
              return;
          }
          MovieService.updateMovie(movie);
        } catch (NumberFormatException e) {
          System.out.println("Wrong Input");
          return;
        } catch (IndexOutOfBoundsException e) {
          System.out.println("No movie there");
        }
      }
        break;
      case "3":
        System.out.println("MOVIE DELETE");
        System.out.print("movie index: ");
        try{
          int index = scanner.nextInt();
          MovieService.deleteMovie(movies.get(index).getId());
        }catch (InputMismatchException e){
          System.out.println("Wrong ID Format");
        }catch (IndexOutOfBoundsException e){
          System.out.println("No movie there");
        }
        break;
      case "0":
        MenuManager.closeCurrentWindow();
    }
  }
}