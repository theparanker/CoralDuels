package me.theparanker.duel.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.theparanker.duel.managers.impl.leaderboard.LeaderboardManager;
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
            position = Integer.parseInt(args[2]) - 1; // index 0-based
        } catch (NumberFormatException e) {
            return null;
        }
        String field = args[3];

        LeaderboardManager.LeaderboardEntry entry = LeaderboardManager.get().getEntry(stat, position);
        if (entry == null) return null;

        return switch (field) {
            case "name" -> entry.name();
            case "value" -> String.valueOf(entry.value());
            default -> null;
        };
    }


}