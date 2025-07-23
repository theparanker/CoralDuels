package me.theparanker.duel.managers.impl.duels.structure;

import lombok.With;
import me.theparanker.duel.managers.impl.arenas.structure.Arena;
import me.theparanker.duel.managers.impl.kits.structure.Kit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@With
public record Duel(
    Arena arena,
    Kit kit,
    int timeLeft,
    ArrayList<Player> players,
    ArrayList<Player> spectators,
    DuelState state,
    int countdown,
    Map<Player, ItemStack[]> savedInventories,
    Map<Player, ItemStack[]> savedArmor,
    long startTime
) {
    public Duel {
        if (savedInventories == null) {
            savedInventories = new HashMap<>();
        }
        if (savedArmor == null) {
            savedArmor = new HashMap<>();
        }
    }

    public Player getOpponent(Player player) {
        for (Player p : players) {
            if (!p.equals(player)) {
                return p;
            }
        }
        return null;
    }
}
