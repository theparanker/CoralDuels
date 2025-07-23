package me.theparanker.duel.managers.impl.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import me.theparanker.duel.managers.structure.Manager;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ScoreboardManager implements Manager {
    private static ScoreboardManager INSTANCE;
    private final Map<UUID, FastBoard> boards = new ConcurrentHashMap<>();
    public final ScoreboardProvider provider;

    public ScoreboardManager() {
        INSTANCE = this;
        this.provider = new ScoreboardProvider();
    }

    @Override
    public void start() {}

    @Override
    public void stop() {
        for (FastBoard board : boards.values()) {
            board.delete();
        }
        boards.clear();
        INSTANCE = null;
    }
    
    public void updateScoreboard(Player player) {
        FastBoard board = boards.get(player.getUniqueId());
        if (board == null) {
            board = new FastBoard(player);
            boards.put(player.getUniqueId(), board);
        }
        
        provider.updateBoard(board, player);
    }
    
    public void removeScoreboard(Player player) {
        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    public static ScoreboardManager get() {
        return INSTANCE;
    }
}

