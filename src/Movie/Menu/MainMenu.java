package Movie.Menu;

class MainMenu implements Menu {
  @Override
  public void print() {
    System.out.println("""
      -----------------------------------------------
      1. 영화예매 | 2. 예매확인 | 3. 예매취소 | 0.    종료
      
      --------------type password to enter Admin menu
      -----------------------------------------------
      """);
  }

  @Override
  public void execute(String command) {
    //TODO
    System.out.println(command);
    switch(command){
      case "1":
        break;
      case "0":
        MenuManager.haltProgram();
        break;
      default:
        MenuManager.toAdminMenu(command);
    }
  }
}
