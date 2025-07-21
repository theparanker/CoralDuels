package me.theparanker.duel.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public interface LocationUtils {

    static Location deserializeLocation(ConfigurationSection section, String worldName) {
        if (section == null) return null;
        double x = section.getDouble("X");
        double y = section.getDouble("Y");
        double z = section.getDouble("Z");
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }

}
