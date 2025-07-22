package me.theparanker.duel.managers.impl.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import me.theparanker.duel.CoralDuel;
import me.theparanker.duel.config.ConfigFile;
import me.theparanker.duel.managers.impl.duels.core.DuelsManager;
import me.theparanker.duel.managers.impl.duels.structure.Duel;
import me.theparanker.duel.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class ScoreboardProvider {

    private final ConfigFile cfg;

    public ScoreboardProvider() {
        this.cfg = CoralDuel.get().getScoreboardFile();
        Bukkit.getScheduler().runTaskLater(
                CoralDuel.get().getPlugin(),
                () -> Bukkit.getScheduler().runTaskTimerAsynchronously(
                        CoralDuel.get().getPlugin(),
                        this::updateAllBoards,
                        0L,
                        20L
                ),
                30L
        );

    }

    public void addBoard(Player player) {
        FastBoard board = new FastBoard(player);
        ScoreboardManager.get().getBoards().put(player.getUniqueId(), board);
        updateBoard(board, player);
    }

    public void removeBoard(Player player) {
        var board = ScoreboardManager.get().getBoards().remove(player.getUniqueId());
        if (board != null) board.delete();
    }

    public void updateAllBoards() {
        for (var entry : ScoreboardManager.get().getBoards().entrySet()) {
            var board = entry.getValue();
            var player = board.getPlayer();
            if (player != null) updateBoard(board, player);
        }
    }

    private void updateBoard(FastBoard board, Player player) {
        Duel duel = DuelsManager.get().getDuelByPlayer(player);
        if (duel == null) removeBoard(player);

        List<String> rawLines = cfg.getStringList("scoreboard.lines");
        String title = cfg.getString("scoreboard.title");
        rawLines.replaceAll(replace -> CC.translate(replace
                .replace("%kit%", duel.kit().displayName())
                .replace("%time_remain%", String.valueOf(duel.timeLeft()))));

        board.updateTitle(CC.translate(title));
        board.updateLines(rawLines);
    }
}