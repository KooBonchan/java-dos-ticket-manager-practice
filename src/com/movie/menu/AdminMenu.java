package com.movie.menu;

import com.movie.MenuManager;
import com.movie.service.ReservationService;

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
        // this will call MovieMenu
        break;
      case "2":
        for(var reservation : ReservationService.readAll()){
          System.out.println(reservation.toListEntry());
        }
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
