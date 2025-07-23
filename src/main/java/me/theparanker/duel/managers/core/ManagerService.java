package me.theparanker.duel.managers.core;

import me.theparanker.duel.managers.impl.arenas.core.ArenasManager;
import me.theparanker.duel.managers.impl.duels.core.DuelsManager;
import me.theparanker.duel.managers.impl.kits.core.KitsManager;
import me.theparanker.duel.managers.impl.leaderboard.LeaderboardManager;
import me.theparanker.duel.managers.impl.persistance.MySqlManager;
import me.theparanker.duel.managers.impl.scoreboard.ScoreboardManager;
import me.theparanker.duel.managers.impl.user.core.UserManager;
import me.theparanker.duel.managers.structure.Manager;

import java.util.ArrayList;
import java.util.List;

public class ManagerService {
    private List<Manager> MANAGERS;

    public void init() {
        this.MANAGERS = new ArrayList<>();

        register(new MySqlManager());
        register(new UserManager());
        register(new ArenasManager());
        register(new KitsManager());
        register(new ScoreboardManager());
        register(new DuelsManager());
        register(new LeaderboardManager());

        this.MANAGERS.forEach(Manager::start);
    }

    public void shutdown() {
        this.MANAGERS.reversed().forEach(Manager::stop);
    }

    public void register(Manager manager) {
        this.MANAGERS.add(manager);
    }

}