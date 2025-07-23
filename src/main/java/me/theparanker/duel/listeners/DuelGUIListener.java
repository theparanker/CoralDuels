package me.theparanker.duel.listeners;

import me.theparanker.duel.managers.impl.kits.core.KitsManager;
import me.theparanker.duel.managers.impl.kits.structure.Kit;
import me.theparanker.duel.utils.DuelRequestManager;
import me.theparanker.duel.utils.MessagesManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class DuelGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (!title.contains("Select Kit - Duel") && !title.contains("Seleziona Kit - Duello")) {
            return;
        }
        
        event.setCancelled(true);
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        
        if (clickedItem.getType() == Material.BARRIER) {
            player.closeInventory();
            player.sendMessage(me.theparanker.duel.utils.MessagesManager.getMessage("gui.request-cancelled"));
            DuelRequestManager.removeTargetPlayer(player);
            return;
        }
        
        Player target = DuelRequestManager.getTargetPlayer(player);
        if (target == null) {
            player.closeInventory();
            player.sendMessage(me.theparanker.duel.utils.MessagesManager.getMessage("gui.target-not-found"));
            return;
        }
        
        Kit selectedKit = null;
        Map<String, Kit> kits = KitsManager.get().getAllKits();
        
        for (Kit kit : kits.values()) {
            ItemStack kitIcon = kit.icon();
            if (kitIcon != null && kitIcon.getType() == clickedItem.getType()) {
                if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                    String displayName = clickedItem.getItemMeta().getDisplayName();
                    if (displayName.equals(ChatColor.GOLD + kit.displayName())) {
                        selectedKit = kit;
                        break;
                    }
                }
            }
        }
        
        if (selectedKit == null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasLore()) {
            for (String loreLine : clickedItem.getItemMeta().getLore()) {
                if (loreLine.contains("Kit: ")) {
                    String kitName = loreLine.replaceAll("§[0-9a-fk-or]", "").replace("Kit: ", "").trim();
                    selectedKit = KitsManager.get().getKit(kitName);
                    break;
                }
            }
        }
        
        if (selectedKit == null) {
            player.sendMessage(me.theparanker.duel.utils.MessagesManager.getMessage("gui.kit-not-identified"));
            return;
        }

        if (!player.hasPermission(selectedKit.permission())) {
            player.sendMessage(MessagesManager.getMessage("request.no-permision"));
            return;
        }
        
        player.closeInventory();
        DuelRequestManager.removeTargetPlayer(player);
        DuelRequestManager.sendDuelRequest(player, target, selectedKit);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        DuelRequestManager.cleanupPlayer(event.getPlayer());
    }
}
