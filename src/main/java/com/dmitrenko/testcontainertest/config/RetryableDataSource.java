package com.dmitrenko.testcontainertest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class RetryableDataSource extends AbstractDataSource {

    private final DataSource delegate;

    @Override
    @Retryable(maxAttempts = 99999999, backoff = @Backoff(delay = 3000), include = Throwable.class)
    public Connection getConnection() throws SQLException {
      return delegate.getConnection();
    }

    @Override
    @Retryable(maxAttempts = 99999999, backoff = @Backoff(delay = 3000), include = Throwable.class)
    public Connection getConnection(String username, String password) throws SQLException {
      return delegate.getConnection(username, password);
    }
}
