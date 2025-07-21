package me.theparanker.duel.bootstrap;

import me.theparanker.duel.CoralDuel;
import org.bukkit.plugin.java.JavaPlugin;

public class CoralDuelPlugin extends JavaPlugin {

    private CoralDuel provider;

    @Override
    public void onEnable() {
        this.provider = new CoralDuel();
        this.provider.init(this);
    }

    @Override
    public void onDisable() {
        this.provider.shutdown();
    }
}
