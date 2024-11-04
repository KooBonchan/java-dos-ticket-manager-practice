package com.movie.database;

import lombok.Getter;
import org.apache.commons.dbcp2.BasicDataSource;

import java.time.Duration;

public class DatabaseConnectionPool {
  @Getter
  private static final BasicDataSource dataSource;

  static {
    dataSource = new BasicDataSource();
    dataSource.setUrl("jdbc:mysql://localhost:3306/kbc");
    dataSource.setUsername("kbc");
    dataSource.setPassword("12345");

    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setInitialSize(5);
    dataSource.setMaxTotal(10);
    dataSource.setMinIdle(1);
    dataSource.setMaxIdle(5);
    dataSource.setMaxWait(Duration.ofMillis(7000));

  }

}
