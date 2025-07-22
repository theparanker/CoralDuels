package me.theparanker.duel.managers.impl.duels.structure;

import lombok.With;
import me.theparanker.duel.managers.impl.arenas.structure.Arena;
import me.theparanker.duel.managers.impl.kits.structure.Kit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@With
public record Duel(
    Arena arena,
    Kit kit,
    int timeLeft,
    ArrayList<Player> players,
    ArrayList<Player> spectators
) {}
