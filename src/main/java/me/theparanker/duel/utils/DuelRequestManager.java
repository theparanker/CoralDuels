package me.theparanker.duel.utils;

import me.theparanker.duel.CoralDuel;
import me.theparanker.duel.managers.impl.duels.core.DuelsManager;
import me.theparanker.duel.managers.impl.kits.core.KitsManager;
import me.theparanker.duel.managers.impl.kits.structure.Kit;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DuelRequestManager {
    
    private static final Map<Player, Player> targetPlayers = new HashMap<>();
    private static final Map<UUID, DuelRequest> pendingRequests = new HashMap<>();
    private static final Map<UUID, BukkitTask> requestTimeouts = new HashMap<>();
    
    public static class DuelRequest {
        private final Player challenger;
        private final Player target;
        private final Kit kit;
        private final long timestamp;
        
        public DuelRequest(Player challenger, Player target, Kit kit) {
            this.challenger = challenger;
            this.target = target;
            this.kit = kit;
            this.timestamp = System.currentTimeMillis();
        }
        
        public Player getChallenger() { return challenger; }
        public Player getTarget() { return target; }
        public Kit getKit() { return kit; }
        public long getTimestamp() { return timestamp; }
    }
    
    public static void setTargetPlayer(Player player, Player target) {
        targetPlayers.put(player, target);
    }
    
    public static Player getTargetPlayer(Player player) {
        return targetPlayers.get(player);
    }
    
    public static void removeTargetPlayer(Player player) {
        targetPlayers.remove(player);
    }
    
    public static void sendDuelRequest(Player challenger, Player target, Kit kit) {
        UUID challengerId = challenger.getUniqueId();
        if (pendingRequests.containsKey(challengerId)) {
            challenger.sendMessage(MessagesManager.getMessage("request.already-pending"));
            return;
        }
        
        for (DuelRequest request : pendingRequests.values()) {
            if (request.getTarget().equals(target)) {
                challenger.sendMessage(MessagesManager.getMessage("request.target-has-pending", "player", target.getName()));
                return;
            }
        }
        
        DuelRequest request = new DuelRequest(challenger, target, kit);
        pendingRequests.put(challengerId, request);
        
        challenger.sendMessage(MessagesManager.getMessage("request.sent", "player", target.getName(), "kit", kit.displayName()));
        
        TextComponent message = new TextComponent(MessagesManager.getMessage("request.challenge-message", "player", challenger.getName(), "kit", kit.displayName()));
        message.addExtra("\n");
        
        TextComponent acceptButton = new TextComponent(MessagesManager.getMessage("buttons.accept"));
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duelaccept " + challenger.getName()));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessagesManager.getMessage("buttons.accept-hover")).create()));
        
        TextComponent separator = new TextComponent(ChatColor.GRAY + " | ");
        
        TextComponent denyButton = new TextComponent(MessagesManager.getMessage("buttons.deny"));
        denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dueldeny " + challenger.getName()));
        denyButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessagesManager.getMessage("buttons.deny-hover")).create()));
        
        message.addExtra(acceptButton);
        message.addExtra(separator);
        message.addExtra(denyButton);
        
        target.spigot().sendMessage(message);
        
        BukkitTask timeoutTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (pendingRequests.containsKey(challengerId)) {
                    pendingRequests.remove(challengerId);
                    requestTimeouts.remove(challengerId);
                    
                    challenger.sendMessage(MessagesManager.getMessage("request.expired", "player", target.getName()));
                    target.sendMessage(MessagesManager.getMessage("request.target-expired", "player", challenger.getName()));
                }
            }
        }.runTaskLater(CoralDuel.get().getPlugin(), 20L * 60);
        
        requestTimeouts.put(challengerId, timeoutTask);
    }
    
    public static boolean acceptDuelRequest(Player target, String challengerName) {
        Player challenger = org.bukkit.Bukkit.getPlayer(challengerName);
        if (challenger == null) {
            target.sendMessage(MessagesManager.getMessage("request.challenger-offline"));
            return false;
        }
        
        UUID challengerId = challenger.getUniqueId();
        DuelRequest request = pendingRequests.get(challengerId);
        
        if (request == null || !request.getTarget().equals(target)) {
            target.sendMessage(MessagesManager.getMessage("request.no-pending-request", "player", challengerName));
            return false;
        }
        
        BukkitTask timeoutTask = requestTimeouts.remove(challengerId);
        if (timeoutTask != null) {
            timeoutTask.cancel();
        }
        
        pendingRequests.remove(challengerId);
        
        if (DuelsManager.get().isInDuel(challenger)) {
            target.sendMessage(MessagesManager.getMessage("duel.target-in-duel", "player", challenger.getName()));
            challenger.sendMessage(MessagesManager.getMessage("duel.already-in-duel"));
            return false;
        }
        
        if (DuelsManager.get().isInDuel(target)) {
            target.sendMessage(MessagesManager.getMessage("duel.already-in-duel"));
            challenger.sendMessage(MessagesManager.getMessage("duel.target-in-duel", "player", target.getName()));
            challenger.sendMessage(MessagesManager.getMessage("duel.target-in-duel", "player", target.getName()));
            return false;
        }
        
        target.sendMessage(MessagesManager.getMessage("request.accepted", "player", challenger.getName()));
        challenger.sendMessage(MessagesManager.getMessage("request.challenger-accepted", "player", target.getName()));
        
        DuelsManager.get().startDuel(request.getKit(), challenger, target);
        return true;
    }
    
    public static boolean denyDuelRequest(Player target, String challengerName) {
        Player challenger = org.bukkit.Bukkit.getPlayer(challengerName);
        if (challenger == null) {
            target.sendMessage(MessagesManager.getMessage("request.challenger-offline"));
            return false;
        }
        
        UUID challengerId = challenger.getUniqueId();
        DuelRequest request = pendingRequests.get(challengerId);
        
        if (request == null || !request.getTarget().equals(target)) {
            target.sendMessage(MessagesManager.getMessage("request.no-pending-request", "player", challengerName));
            return false;
        }
        
        BukkitTask timeoutTask = requestTimeouts.remove(challengerId);
        if (timeoutTask != null) {
            timeoutTask.cancel();
        }
        
        pendingRequests.remove(challengerId);
        
        target.sendMessage(MessagesManager.getMessage("request.denied", "player", challenger.getName()));
        challenger.sendMessage(MessagesManager.getMessage("request.challenger-denied", "player", target.getName()));
        
        return true;
    }
    
    public static void cleanupPlayer(Player player) {
        targetPlayers.remove(player);
        
        UUID playerId = player.getUniqueId();
        if (pendingRequests.containsKey(playerId)) {
            BukkitTask timeoutTask = requestTimeouts.remove(playerId);
            if (timeoutTask != null) {
                timeoutTask.cancel();
            }
            pendingRequests.remove(playerId);
        }
        
        pendingRequests.entrySet().removeIf(entry -> {
            if (entry.getValue().getTarget().equals(player)) {
                BukkitTask timeoutTask = requestTimeouts.remove(entry.getKey());
                if (timeoutTask != null) {
                    timeoutTask.cancel();
                }
                return true;
            }
            return false;
        });
    }
}
