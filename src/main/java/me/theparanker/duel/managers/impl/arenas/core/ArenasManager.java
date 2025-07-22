package me.theparanker.duel.managers.impl.arenas.core;

import lombok.Getter;
import me.theparanker.duel.CoralDuel;
import me.theparanker.duel.managers.impl.arenas.structure.Arena;
import me.theparanker.duel.managers.structure.Manager;
import me.theparanker.duel.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ArenasManager implements Manager {

    private static ArenasManager INSTANCE;
    private Map<Integer,Arena> arenas;

    @Override
    public void start() {
        INSTANCE = this;
        this.arenas = new HashMap<>();
        initializeArenas();
    }

    @Override
    public void stop() {
        INSTANCE = null;
    }

    private void initializeArenas() {
        ConfigurationSection arenasSection = CoralDuel.get().getArenasFile().getConfigurationSection("Arenas");
        if (arenasSection == null) return;

        for (String key : arenasSection.getKeys(false)) {
            ConfigurationSection section = arenasSection.getConfigurationSection(key);
            if (section == null) continue;

            String worldName = section.getString("World");
            if (worldName == null) continue;

            Location first = LocationUtils.deserializeLocation(section.getConfigurationSection("FirstSpawn"), worldName);
            Location second = LocationUtils.deserializeLocation(section.getConfigurationSection("SecondSpawn"), worldName);
            Location spectator = LocationUtils.deserializeLocation(section.getConfigurationSection("SpectatorSpawn"), worldName);

            if (first == null || second == null || spectator == null) continue;

            Arena arena = new Arena(Integer.valueOf(key), first, second, spectator, false);
            arenas.put(arena.id(), arena);
        }
    }

    public Arena getFirstAvailableArena() {
        for (Arena arena : arenas.values()) {
            if (!arena.occupied()) {
                return arena;
            }
        }
        return null;
    }

    public void occupyArena(Arena arena) {
        if (arenas.containsKey(arena.id())) {
            Arena updatedArena = arena.withOccupied(true);
            arenas.put(updatedArena.id(), updatedArena);
        }
    }

    public static ArenasManager get() {
        return INSTANCE;
    }
}
