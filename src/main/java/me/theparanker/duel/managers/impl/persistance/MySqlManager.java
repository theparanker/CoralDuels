package me.theparanker.duel.managers.impl.persistance;

import lombok.Getter;
import me.theparanker.duel.CoralDuel;
import me.theparanker.duel.managers.structure.Manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class MySqlManager implements Manager {

    private static MySqlManager INSTANCE;
    private Connection connection;

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
        try {
            connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        INSTANCE = null;
        disconnect();
    }


    private void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) return;
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
        connection = DriverManager.getConnection(url, user, password);
    }

    private void disconnect() {
        if (connection != null) {
            try { connection.close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static MySqlManager get() {
        return INSTANCE;
    }
}
