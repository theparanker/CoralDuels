package me.theparanker.duel.managers.impl.arenas.structure;

import lombok.With;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@With
public record Arena(
        int id,
        Location firstSpawn,
        Location secondSpawn,
        Location spectatorSpawn,
        boolean occupied
) {

    public void teleportPlayers(Player player1, Player player2) {
        player1.teleport(firstSpawn);
        player2.teleport(secondSpawn);

    }
}
