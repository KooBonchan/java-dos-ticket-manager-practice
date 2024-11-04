package com.movie.Menu;

import java.util.Scanner;

public abstract class AbstractMenu implements Menu{
  protected static Scanner scanner;
  static {
    scanner = new Scanner(System.in);
  }
}
