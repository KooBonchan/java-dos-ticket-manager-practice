package Movie.Menu;

class AdminMenu implements Menu{
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
  public void execute(String command) {
    //TODO
    System.out.println(command);
    switch(command){
      case "1":

        break;
      case "2":
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
