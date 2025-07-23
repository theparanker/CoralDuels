package me.theparanker.duel.commands;

import me.theparanker.duel.managers.impl.duels.core.DuelsManager;
import me.theparanker.duel.managers.impl.kits.core.KitsManager;
import me.theparanker.duel.managers.impl.kits.structure.Kit;
import me.theparanker.duel.utils.DuelRequestManager;
import me.theparanker.duel.utils.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DuelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessagesManager.getMessage("duel.only-players"));
            return true;
        }

        Player player = (Player) sender;

        if (DuelsManager.get().isInDuel(player)) {
            player.sendMessage(MessagesManager.getMessage("duel.already-in-duel"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(MessagesManager.getMessage("duel.usage"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(MessagesManager.getMessage("duel.player-not-found"));
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(MessagesManager.getMessage("duel.cannot-duel-yourself"));
            return true;
        }

        if (DuelsManager.get().isInDuel(target)) {
            player.sendMessage(MessagesManager.getMessage("duel.target-in-duel", "player", target.getName()));
            return true;
        }

        openKitSelectionGUI(player, target);
        return true;
    }

    private void openKitSelectionGUI(Player player, Player target) {
        Map<String, Kit> kits = KitsManager.get().getAllKits();
        
        if (kits.isEmpty()) {
            player.sendMessage(MessagesManager.getMessage("duel.no-kits-available"));
            return;
        }

        int size = Math.min(54, ((kits.size() + 8) / 9) * 9);
        Inventory gui = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', MessagesManager.getMessage("gui.select-kit-title", "player", target.getName())));

        int slot = 0;
        for (Kit kit : kits.values()) {
            ItemStack kitItem = kit.icon() != null ? kit.icon().clone() : new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta meta = kitItem.getItemMeta();
            
            if (meta != null) {
                meta.setDisplayName(ChatColor.GOLD + kit.displayName());
                
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.translateAlternateColorCodes('&', MessagesManager.getMessage("gui.click-to-challenge", "player", target.getName())));
                lore.add(ChatColor.translateAlternateColorCodes('&', MessagesManager.getMessage("gui.with-this-kit")));
                lore.add("");
                lore.add(ChatColor.translateAlternateColorCodes('&', MessagesManager.getMessage("gui.kit-name", "kit", kit.name())));
                meta.setLore(lore);
                
                kitItem.setItemMeta(meta);
            }

            gui.setItem(slot, kitItem);
            slot++;
        }

        ItemStack cancelItem = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = cancelItem.getItemMeta();
        if (cancelMeta != null) {
            cancelMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', MessagesManager.getMessage("gui.cancel")));
            List<String> cancelLore = new ArrayList<>();
            cancelLore.add(ChatColor.translateAlternateColorCodes('&', MessagesManager.getMessage("gui.cancel-lore")));
            cancelMeta.setLore(cancelLore);
            cancelItem.setItemMeta(cancelMeta);
        }
        gui.setItem(size - 1, cancelItem);

        DuelRequestManager.setTargetPlayer(player, target);
        
        player.openInventory(gui);
        player.sendMessage(MessagesManager.getMessage("duel.select-kit", "player", target.getName()));
    }
}
