package com.movie.menu;

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
      AbstractMenu.cleanScreen();
      while( ! nav.isEmpty()) nav.pop();
      nav.push(new AdminMenu());
      return true;
    }
    return false;
  }
  protected static boolean toMainMenu(){
    while( ! nav.isEmpty()) nav.pop();
    nav.push(new MainMenu());
    return true;
  }
  protected static boolean navigateTo(Menu current, Menu next){
    if((next instanceof AdminMenu) ||
      (next instanceof MovieMenu && ! (current instanceof AdminMenu))
    ){
      //Admin only access verifier
      System.out.println("Illegal access to Admin menu");
      return false;
    }
    if(next instanceof MainMenu){
      return toMainMenu();
    }

    if(next.getClass() == current.getClass()){
      // refresh? check use cases.
      nav.pop();
    }
    nav.push(next);
    return true;
  }

  protected static void closeCurrentWindow(){
    nav.pop();
  }
  protected static void haltProgram() {
    nav = null;
    AbstractMenu.scanner.close();
    // ??? wierd dependency?
  }
  public static boolean isEnd(){
    return nav == null || nav.isEmpty();
  }

  public static void execute(){
    nav.peek().execute();
  }
}
