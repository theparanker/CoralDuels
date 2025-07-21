package me.theparanker.duel.managers.core;

import me.theparanker.duel.managers.impl.arenas.core.ArenasManager;
import me.theparanker.duel.managers.impl.kits.core.KitsManager;
import me.theparanker.duel.managers.impl.persistance.MySqlManager;
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

        this.MANAGERS.forEach(Manager::start);
    }

    public void shutdown() {
        this.MANAGERS.forEach(Manager::stop);
    }

    public void register(Manager manager) {
        this.MANAGERS.add(manager);
    }

}