package me.theparanker.duel.managers.impl.leaderboard;

import lombok.Getter;
import me.theparanker.duel.managers.impl.persistance.MySqlManager;
import me.theparanker.duel.managers.structure.Manager;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

@Getter
public class LeaderboardManager implements Manager {

    private static LeaderboardManager INSTANCE;
    private final Map<String, List<LeaderboardEntry>> leaderboardCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final List<String> supportedStats = List.of("wins", "losses", "ties", "streak");

    @Override
    public void start() {
        INSTANCE = this;
        updateCache();
        scheduler.scheduleAtFixedRate(this::updateCache, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        leaderboardCache.clear();
        scheduler.shutdownNow();
        INSTANCE = null;
    }

    public static LeaderboardManager get() {
        return INSTANCE;
    }

    private void updateCache() {
        for (String stat : supportedStats) {
            String column = switch (stat) {
                case "wins" -> "duelWins";
                case "losses" -> "duelLosses";
                case "ties" -> "duelsTied";
                case "streak" -> "duelStreak";
                default -> null;
            };
            if (column == null) continue;

            List<LeaderboardEntry> topPlayers = new ArrayList<>();
            String sql = "SELECT name, " + column + " FROM users ORDER BY " + column + " DESC LIMIT 100";

            try (Connection conn = MySqlManager.get().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String name = rs.getString("name");
                    int value = rs.getInt(column);
                    topPlayers.add(new LeaderboardEntry(name, value));
                }

                leaderboardCache.put(stat, topPlayers);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public @Nullable LeaderboardEntry getEntry(String stat, int position) {
        List<LeaderboardEntry> list = leaderboardCache.get(stat);
        if (list == null || position < 0 || position >= list.size()) return null;
        return list.get(position);
    }

    public record LeaderboardEntry(String name, int value) {}
}
