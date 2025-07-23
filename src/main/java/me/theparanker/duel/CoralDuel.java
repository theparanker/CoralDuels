package me.theparanker.duel;

import lombok.Getter;
import me.theparanker.duel.bootstrap.CoralDuelPlugin;
import me.theparanker.duel.commands.DuelAcceptCommand;
import me.theparanker.duel.commands.DuelCommand;
import me.theparanker.duel.commands.DuelDenyCommand;
import me.theparanker.duel.config.ConfigFile;
import me.theparanker.duel.hook.PlaceholderAPIHook;
import me.theparanker.duel.listeners.DuelGUIListener;
import me.theparanker.duel.managers.core.ManagerService;
import me.theparanker.duel.utils.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

@Getter
public class CoralDuel {

    private static CoralDuel INSTANCE;
    private CoralDuelPlugin plugin;

    private ManagerService managerService;
    private ConfigFile storageFile, arenasFile, kitsFile, scoreboardFile, configFile, messagesFile;

    public void init(CoralDuelPlugin plugin) {
        this.plugin = plugin;
        INSTANCE = this;

        registerConfig();

        this.managerService = new ManagerService();
        this.managerService.init();
        
        MessagesManager.init();

        getPlugin().getCommand("duel").setExecutor(new DuelCommand());
        getPlugin().getCommand("duelaccept").setExecutor(new DuelAcceptCommand());
        getPlugin().getCommand("dueldeny").setExecutor(new DuelDenyCommand());
        
        registerListener(new DuelGUIListener());

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook().register();
            Bukkit.getLogger().info("PlaceholderAPI hooked!");
        }else {
            Bukkit.getLogger().warning("PlaceholderAPI not found! Some features may not work.");
        }
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
        this.messagesFile = new ConfigFile(this.plugin, "messages");
    }

    public void registerListener(Listener listener) {
        getPlugin().getServer().getPluginManager().registerEvents(listener, this.plugin);
    }

    public static CoralDuel get() {
        return INSTANCE;
    }

}
