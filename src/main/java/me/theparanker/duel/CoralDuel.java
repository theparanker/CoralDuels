package me.theparanker.duel;

import lombok.Getter;
import me.theparanker.duel.bootstrap.CoralDuelPlugin;
import me.theparanker.duel.config.ConfigFile;
import me.theparanker.duel.managers.core.ManagerService;

@Getter
public class CoralDuel {

    private static CoralDuel INSTANCE;
    private CoralDuelPlugin plugin;

    private ManagerService managerService;
    private ConfigFile storageFile;

    public void init(CoralDuelPlugin plugin) {
        this.plugin = plugin;
        INSTANCE = this;

        this.managerService = new ManagerService();
        this.managerService.init();
    }

    public void shutdown() {
        this.managerService.shutdown();
        INSTANCE = null;
    }

    private void registerConfig() {
        this.storageFile = new ConfigFile(this.plugin, "storage");
    }

    public static CoralDuel get() {
        return INSTANCE;
    }

}
