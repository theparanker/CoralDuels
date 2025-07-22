package me.theparanker.duel.managers.impl.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import me.theparanker.duel.managers.structure.Manager;

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

    public static ScoreboardManager get() {
        return INSTANCE;
    }
}

