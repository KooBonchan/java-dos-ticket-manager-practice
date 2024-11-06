package com.movie.menu;

import java.util.Scanner;

abstract class AbstractMenu implements Menu{
  protected static Scanner scanner;
  static {
    scanner = new Scanner(System.in);
  }


  /** Menu Utility Functions ******************************/
  public static void cleanScreen(){
    for(int i = 0; i < 100; i++){
      System.out.println();
    } // clean screen
  }

  public static String getCommand(){
    String command; // this will have name command if refactored to another class
    System.out.print("Enter command: ");
    do{
      command= scanner.nextLine().strip();
    }while(command.isEmpty());
    return command;
  }
}
