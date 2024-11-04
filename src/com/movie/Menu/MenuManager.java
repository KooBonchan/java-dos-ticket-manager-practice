package com.movie.Menu;

import lombok.Getter;

public class MenuManager {
  @Getter
  private static Menu current = new MainMenu();

  /**
   * It is awkward to add such function
   * whenever adding new menu.
   *
   * @return true if succeed loading menu
   */
  protected static boolean toAdminMenu(String password){
    if(AdminMenu.verifyPassword(password)){
      for(int i = 0; i < 100; i++){
        System.out.println();
      }
      current = new AdminMenu();
      return true;
    }
    return false;
  }
  protected static boolean toMainMenu(){
    current = new MainMenu();
    return true;
  }
  protected static void haltProgram(){
    current = null;
  }
  public static boolean isEnd(){
    return current == null;
  }
}
