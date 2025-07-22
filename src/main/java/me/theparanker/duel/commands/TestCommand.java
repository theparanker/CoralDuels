package me.theparanker.duel.commands;

import me.theparanker.duel.managers.impl.arenas.core.ArenasManager;
import me.theparanker.duel.managers.impl.duels.core.DuelsManager;
import me.theparanker.duel.managers.impl.kits.core.KitsManager;
import me.theparanker.duel.managers.impl.user.structure.UserStructure;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Player player2 = Bukkit.getPlayer("test");
        return true;
    }
}
