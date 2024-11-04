package com.movie.Menu;

import com.movie.ReservationService;

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
      1. 영화관리 | 2. 예매관리 | 3.   홈으로 | 0.    종료
      --------------type password to enter Admin menu
      ADMINISTRATORADMINISTRATORADMINISTRATORADMINIST
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

        break;
      case "2":
        ReservationService.printAll();
        break;
      case "3":
        break;
      case "0":
        MenuManager.haltProgram();
        break;
      default:
        MenuManager.toMainMenu();
    }
  }
}
