package com.movie.menu;

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
      GWANLIZAGWANLIZAGWANLIZAGWANLIZAGWANLIZAGWANLIZ
      ADMINISTRATORADMINISTRATORADMINISTRATORADMINIST
      """);
  }

  @Override
  public void execute() {
    print();
    String command = getCommand();

    switch(command){
      case "1":
        // this will call MovieMenu
        MenuManager.navigateTo(this,new MovieMenu());
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
