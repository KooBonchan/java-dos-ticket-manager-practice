package com.movie;

import com.movie.menu.AdminMenu;
import com.movie.menu.MainMenu;
import com.movie.menu.Menu;

import java.util.Stack;

public class MenuManager {
  //Todo: Stack Nav structure on MenuManager
  private static Stack<Menu> nav;
  static {
    nav = new Stack<>();
    nav.push(new MainMenu());
  }

  /**
   * It is awkward to add such function
   * whenever adding new menu.
   *
   * @return true if succeed loading menu
   */

  /*
  * TODO: Better way to manage states
  *  dependency to its parent structure?
  * */
  protected static boolean toAdminMenu(String password){
    if(AdminMenu.verifyPassword(password)){
      for(int i = 0; i < 100; i++){
        System.out.println();
      }
      closeCurrentWindow();
      nav.push(new AdminMenu());
      return true;
    }
    return false;
  }
  protected static boolean toMainMenu(){
    closeCurrentWindow();
    nav.push(new MainMenu());
    return true;
  }

  protected static void closeCurrentWindow(){
    nav.pop();
  }
  protected static void haltProgram(){
    nav = new Stack<>();
  }
  public static boolean isEnd(){
    return nav.isEmpty();
  }

  public static void execute(){
    nav.peek().print();
    nav.peek().execute();
  }
}
