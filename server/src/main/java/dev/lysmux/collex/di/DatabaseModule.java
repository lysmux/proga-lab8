package dev.lysmux.collex.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.lysmux.collex.config.DbConfig;
import dev.lysmux.collex.infrastructure.database.SqlExecutor;
import dev.lysmux.collex.infrastructure.repository.CollectionRepository;
import dev.lysmux.collex.infrastructure.repository.SqlCollectionRepository;
import dev.lysmux.collex.infrastructure.repository.SqlUserRepository;
import dev.lysmux.collex.infrastructure.repository.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseModule extends AbstractModule {
    private final HikariDataSource dataSource;

    public DatabaseModule(DbConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.jdbc());
        hikariConfig.setUsername(config.user());
        hikariConfig.setPassword(config.password());

        dataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    protected void configure() {
        bind(SqlExecutor.class);
        bind(UserRepository.class).to(SqlUserRepository.class);
        bind(CollectionRepository.class).to(SqlCollectionRepository.class);
    }

    @Provides
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
