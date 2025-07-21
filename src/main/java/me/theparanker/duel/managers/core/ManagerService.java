package me.theparanker.duel.managers.core;

import me.theparanker.duel.managers.structure.Manager;

import java.util.ArrayList;
import java.util.List;

public class ManagerService {
    private List<Manager> MANAGERS;

    public void init() {
        this.MANAGERS = new ArrayList<>();



        this.MANAGERS.forEach(Manager::start);
    }

    public void shutdown() {
        this.MANAGERS.forEach(Manager::stop);
    }

    public void register(Manager manager) {
        this.MANAGERS.add(manager);
    }

}