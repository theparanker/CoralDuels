package me.theparanker.duel.managers.impl.arenas.structure;

import org.bukkit.Location;

public record Arena(
        int id,
        Location firstSpawn,
        Location secondSpawn,
        Location spectatorSpawn,
        boolean occupied
) {
}
