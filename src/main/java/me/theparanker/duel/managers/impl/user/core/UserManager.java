package me.theparanker.duel.managers.impl.user.core;

import lombok.Getter;
import me.theparanker.duel.managers.impl.persistance.MySqlManager;
import me.theparanker.duel.managers.impl.user.structure.UserStructure;
import me.theparanker.duel.managers.structure.Manager;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

@Getter
public class UserManager implements Manager {

    private static UserManager INSTANCE;

    private final Map<UUID, UserStructure> users = new ConcurrentHashMap<>();
    private final Executor executor = Executors.newVirtualThreadPerTaskExecutor();

    @Override
    public void start() {
        INSTANCE = this;
        createTable();
    }

    @Override
    public void stop() {
        users.clear();
        INSTANCE = null;
    }

    public static UserManager get() {
        return INSTANCE;
    }

    private void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                uuid VARCHAR(36) PRIMARY KEY,
                name VARCHAR(32),
                duelWins INT,
                duelLosses INT,
                duelsTied INT,
                duelStreak INT
            );
        """;
        try (Connection conn = MySqlManager.get().getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<UserStructure> loadUser(UUID uuid, String name) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = MySqlManager.get().getConnection()) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE uuid = ?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();

                UserStructure user;
                if (rs.next()) {
                    user = new UserStructure(
                            uuid,
                            rs.getString("name"),
                            rs.getInt("duelWins"),
                            rs.getInt("duelLosses"),
                            rs.getInt("duelsTied"),
                            rs.getInt("duelStreak"),
                            false,
                            new ArrayList<>(),
                            new ArrayList<>(),
                            null
                    );
                } else {
                    user = new UserStructure(
                            uuid,
                            name,
                            0,
                            0,
                            0,
                            0,
                            false,
                            new ArrayList<>(),
                            new ArrayList<>(),
                            null
                    );
                    save(user);
                }

                updateCache(user, false);
                return user;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }, executor);
    }

    public void save(UserStructure user) {
        CompletableFuture.runAsync(() -> {
            try (Connection conn = MySqlManager.get().getConnection()) {
                PreparedStatement ps = conn.prepareStatement("""
                    INSERT INTO users (uuid, name, duelWins, duelLosses, duelsTied, duelStreak)
                    VALUES (?, ?, ?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                      name = VALUES(name),
                      duelWins = VALUES(duelWins),
                      duelLosses = VALUES(duelLosses),
                      duelsTied = VALUES(duelsTied),
                      duelStreak = VALUES(duelStreak)
                """);
                ps.setString(1, user.uuid().toString());
                ps.setString(2, user.name());
                ps.setInt(3, user.duelWins());
                ps.setInt(4, user.duelLosses());
                ps.setInt(5, user.duelsTied());
                ps.setInt(6, user.duelStreak());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, executor);
    }

    public void updateCache(UserStructure user, boolean remove) {
        if (remove) users.remove(user.uuid());
        else users.put(user.uuid(), user);
    }

}
