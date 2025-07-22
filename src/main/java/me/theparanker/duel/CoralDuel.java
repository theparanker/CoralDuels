package me.theparanker.duel;

import lombok.Getter;
import me.theparanker.duel.bootstrap.CoralDuelPlugin;
import me.theparanker.duel.commands.TestCommand;
import me.theparanker.duel.config.ConfigFile;
import me.theparanker.duel.managers.core.ManagerService;
import org.bukkit.event.Listener;

@Getter
public class CoralDuel {

    private static CoralDuel INSTANCE;
    private CoralDuelPlugin plugin;

    private ManagerService managerService;
    private ConfigFile storageFile, arenasFile, kitsFile, scoreboardFile, configFile;

    public void init(CoralDuelPlugin plugin) {
        this.plugin = plugin;
        INSTANCE = this;

        registerConfig();

        this.managerService = new ManagerService();
        this.managerService.init();

        getPlugin().getCommand("test").setExecutor(new TestCommand());
    }

    public void shutdown() {
        this.managerService.shutdown();
        INSTANCE = null;
    }

    private void registerConfig() {
        this.storageFile = new ConfigFile(this.plugin, "storage");
        this.arenasFile = new ConfigFile(this.plugin, "arenas");
        this.kitsFile = new ConfigFile(this.plugin, "kits");
        this.scoreboardFile = new ConfigFile(this.plugin, "scoreboard");
        this.configFile = new ConfigFile(this.plugin, "config");
    }

    public void registerListener(Listener listener) {
        getPlugin().getServer().getPluginManager().registerEvents(listener, this.plugin);
    }

    public static CoralDuel get() {
        return INSTANCE;
    }

}
