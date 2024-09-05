package com.dmitrenko.testcontainertest.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

  @Value("${spring.datasource.driver-class-name}")
  private String driver;

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.maximumPoolSize}")
  private int maximumPoolSize;

  @Value("${spring.datasource.minimumIdle}")
  private int minimumIdle;

  @Value("${spring.datasource.idleTimeout}")
  private int idleTimeout;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Bean
  public DataSource dataSource() {
    var dataSource = new HikariDataSource();
    dataSource.setDriverClassName(driver);
    dataSource.setJdbcUrl(url);
    dataSource.setMaximumPoolSize(maximumPoolSize);
    dataSource.setMinimumIdle(minimumIdle);
    dataSource.setIdleTimeout(idleTimeout);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }

  @Bean
  public BeanPostProcessor retryableDataSourceWrapperPostProcessor() {
    return new RetryableDataSourceWrapperBeanPostProcessor();
  }
}
