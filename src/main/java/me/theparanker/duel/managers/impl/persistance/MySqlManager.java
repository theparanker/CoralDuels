package me.theparanker.duel.managers.impl.persistance;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.theparanker.duel.CoralDuel;
import me.theparanker.duel.managers.structure.Manager;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
public class MySqlManager implements Manager {

    private static MySqlManager INSTANCE;
    private HikariDataSource dataSource;

    private final String host, port, database, user, password;

    public MySqlManager() {
        this.host = CoralDuel.get().getStorageFile().getString("MySQL.host");
        this.port = CoralDuel.get().getStorageFile().getString("MySQL.port");
        this.database = CoralDuel.get().getStorageFile().getString("MySQL.database");
        this.user = CoralDuel.get().getStorageFile().getString("MySQL.Auth.user");
        this.password = CoralDuel.get().getStorageFile().getString("MySQL.Auth.password");
    }

    @Override
    public void start() {
        INSTANCE = this;
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC");
        config.setUsername(user);
        config.setPassword(password);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(60000);
        config.setConnectionTimeout(10000);
        config.setMaxLifetime(600000);

        config.setPoolName("CoralDuel-MySQL-Pool");

        dataSource = new HikariDataSource(config);
    }

    @Override
    public void stop() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
        INSTANCE = null;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static MySqlManager get() {
        return INSTANCE;
    }
}
