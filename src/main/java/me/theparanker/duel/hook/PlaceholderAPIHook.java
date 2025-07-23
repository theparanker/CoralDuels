package me.theparanker.duel.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.theparanker.duel.managers.impl.persistance.MySqlManager;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "duel";
    }

    @Override
    public @NotNull String getAuthor() {
        return "_theparanker_";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {
        String[] args = params.toLowerCase().split("_");

        if (args.length != 4 || !args[0].equals("leaderboard")) return null;

        String stat = args[1];
        int position;
        try {
            position = Integer.parseInt(args[2]) - 1;
        } catch (NumberFormatException e) {
            return null;
        }
        String field = args[3];

        String column;
        switch (stat) {
            case "wins" -> column = "duelWins";
            case "losses" -> column = "duelLosses";
            case "ties" -> column = "duelsTied";
            case "streak" -> column = "duelStreak";
            default -> {
                return null;
            }
        }

        String sql = "SELECT name, " + column + " FROM users ORDER BY " + column + " DESC LIMIT ?, 1";

        try (Connection conn = MySqlManager.get().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, position);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return switch (field) {
                case "name" -> rs.getString("name");
                case "value" -> String.valueOf(rs.getInt(column));
                default -> null;
            };
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}