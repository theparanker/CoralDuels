package me.theparanker.duel.managers.impl.duels.core;

import lombok.Getter;
import me.theparanker.duel.CoralDuel;
import me.theparanker.duel.managers.impl.arenas.core.ArenasManager;
import me.theparanker.duel.managers.impl.arenas.structure.Arena;
import me.theparanker.duel.managers.impl.duels.structure.Duel;
import me.theparanker.duel.managers.impl.kits.core.KitsManager;
import me.theparanker.duel.managers.impl.kits.structure.Kit;
import me.theparanker.duel.managers.impl.scoreboard.ScoreboardManager;
import me.theparanker.duel.managers.impl.user.core.UserManager;
import me.theparanker.duel.managers.impl.user.structure.UserStructure;
import me.theparanker.duel.managers.structure.Manager;
import me.theparanker.duel.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;

@Getter
public class DuelsManager implements Manager {

    private static DuelsManager INSTANCE;
    private List<Duel> activeDuels;

    @Override
    public void start() {
        INSTANCE = this;
        this.activeDuels = new ArrayList<>();
    }

    @Override
    public void stop() {
        INSTANCE = null;
    }

    public void startDuel(@NotNull Kit kit,@NotNull Player player1,@NotNull Player player2) {

    }

    public void endDuel(@NotNull Duel duel, Player winner) {

    }

    public Duel getDuelByPlayer(@NotNull Player player) {
        for (Duel duel : activeDuels) {
            if (duel.players().contains(player)) {
                return duel;
            }
        }
        return null;
    }

    public static DuelsManager get() {
        return INSTANCE;
    }
}
