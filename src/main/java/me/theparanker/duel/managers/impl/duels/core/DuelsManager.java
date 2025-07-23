package me.theparanker.duel.managers.impl.duels.core;

import lombok.Getter;
import me.theparanker.duel.CoralDuel;
import me.theparanker.duel.managers.impl.arenas.core.ArenasManager;
import me.theparanker.duel.managers.impl.arenas.structure.Arena;
import me.theparanker.duel.managers.impl.duels.structure.Duel;
import me.theparanker.duel.managers.impl.duels.structure.DuelState;
import me.theparanker.duel.managers.impl.kits.core.KitsManager;
import me.theparanker.duel.managers.impl.kits.structure.Kit;
import me.theparanker.duel.managers.impl.scoreboard.ScoreboardManager;
import me.theparanker.duel.managers.impl.user.core.UserManager;
import me.theparanker.duel.managers.impl.user.structure.UserStructure;
import me.theparanker.duel.managers.structure.Manager;
import me.theparanker.duel.utils.LocationUtils;
import me.theparanker.duel.utils.MessagesManager;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DuelsManager implements Manager, Listener {

    private static DuelsManager INSTANCE;
    private List<Duel> activeDuels;
    private int duelTime;
    private Location spawnLocation;

    @Override
    public void start() {
        INSTANCE = this;
        this.activeDuels = new ArrayList<>();
        
        this.duelTime = CoralDuel.get().getConfigFile().getInt("duel-time", 120);
        
        ConfigurationSection spawnSection = CoralDuel.get().getConfigFile().getConfigurationSection("spawn");
        if (spawnSection != null) {
            World world = Bukkit.getWorld(spawnSection.getString("world", "world"));
            this.spawnLocation = new Location(
                world,
                spawnSection.getDouble("X", 0),
                spawnSection.getDouble("Y", 0),
                spawnSection.getDouble("Z", 0)
            );
        }
        
        Bukkit.getPluginManager().registerEvents(this, CoralDuel.get().getPlugin());
    }

    @Override
    public void stop() {
        for (Duel duel : new ArrayList<>(activeDuels)) {
            endDuel(duel, null);
        }
        INSTANCE = null;
    }

    public void startDuel(@NotNull Kit kit, @NotNull Player player1, @NotNull Player player2) {
        Arena arena = ArenasManager.get().getAvailableArena();
        if (arena == null) {
            player1.sendMessage(MessagesManager.getMessage("arena.no-arenas-available"));
            player2.sendMessage(MessagesManager.getMessage("arena.no-arenas-available"));
            return;
        }
        
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        
        Map<Player, ItemStack[]> savedInventories = new HashMap<>();
        Map<Player, ItemStack[]> savedArmor = new HashMap<>();
        
        savePlayerInventory(player1, savedInventories, savedArmor);
        savePlayerInventory(player2, savedInventories, savedArmor);
        
        Duel duel = new Duel(
            arena.withOccupied(true),
            kit,
            duelTime,
            players,
            new ArrayList<>(),
            DuelState.STARTING,
            3,
            savedInventories,
            savedArmor,
            System.currentTimeMillis()
        );
        
        activeDuels.add(duel);
        
        arena.teleportPlayers(player1, player2);
        
        applyKit(player1, kit);
        applyKit(player2, kit);
        
        startCountdown(duel);
        
        ScoreboardManager.get().updateScoreboard(player1);
        ScoreboardManager.get().updateScoreboard(player2);
        
        player1.sendMessage(MessagesManager.getMessage("start.duel-started", "player", player2.getName()));
        player2.sendMessage(MessagesManager.getMessage("start.duel-started", "player", player1.getName()));
    }
    
    private void savePlayerInventory(Player player, Map<Player, ItemStack[]> savedInventories, Map<Player, ItemStack[]> savedArmor) {
        savedInventories.put(player, player.getInventory().getContents().clone());
        savedArmor.put(player, player.getInventory().getArmorContents().clone());
        
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
    }
    
    private void applyKit(Player player, Kit kit) {
        player.getInventory().setContents(kit.inventoryContents().clone());
        player.getInventory().setArmorContents(kit.armor().clone());
        player.updateInventory();
    }
    
    private void startCountdown(Duel duel) {
        new BukkitRunnable() {
            private Duel currentDuel = duel.withState(DuelState.COUNTDOWN);
            
            @Override
            public void run() {
                int duelIndex = -1;
                for (int i = 0; i < activeDuels.size(); i++) {
                    if (activeDuels.get(i).players().equals(currentDuel.players())) {
                        duelIndex = i;
                        currentDuel = activeDuels.get(i);
                        break;
                    }
                }
                
                if (duelIndex == -1) {
                    cancel();
                    return;
                }
                
                if (currentDuel.countdown() > 0) {
                    for (Player player : currentDuel.players()) {
                        player.sendTitle(
                            ChatColor.YELLOW + String.valueOf(currentDuel.countdown()),
                            MessagesManager.getMessage("start.get-ready"),
                            5, 20, 5
                        );
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    }
                    
                    currentDuel = currentDuel.withCountdown(currentDuel.countdown() - 1).withState(DuelState.COUNTDOWN);
                    activeDuels.set(duelIndex, currentDuel);
                } else {
                    currentDuel = currentDuel.withState(DuelState.ACTIVE).withStartTime(System.currentTimeMillis());
                    activeDuels.set(duelIndex, currentDuel);
                    
                    for (Player player : currentDuel.players()) {
                        player.sendTitle(
                                MessagesManager.getMessage("start.fight"),
                                MessagesManager.getMessage("start.duel-has-started"),
                            5, 20, 5
                        );
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                    }
                    
                    startDuelTimer(currentDuel);
                    cancel();
                }
            }
        }.runTaskTimer(CoralDuel.get().getPlugin(), 0L, 20L);
    }
    
    private void startDuelTimer(Duel duel) {
        new BukkitRunnable() {
            private Duel currentDuel = duel;
            
            @Override
            public void run() {
                int duelIndex = -1;
                for (int i = 0; i < activeDuels.size(); i++) {
                    if (activeDuels.get(i).players().equals(currentDuel.players())) {
                        duelIndex = i;
                        currentDuel = activeDuels.get(i);
                        break;
                    }
                }
                
                if (duelIndex == -1 || currentDuel.state() != DuelState.ACTIVE) {
                    cancel();
                    return;
                }
                
                long elapsed = (System.currentTimeMillis() - currentDuel.startTime()) / 1000;
                int timeLeft = currentDuel.timeLeft() - (int) elapsed;
                
                if (timeLeft <= 0) {
                    endDuel(currentDuel, null);
                    cancel();
                } else {
                    if (timeLeft == 30 || timeLeft == 10 || timeLeft <= 5) {
                        for (Player player : currentDuel.players()) {
                            player.sendMessage(MessagesManager.getMessage("timer.time-left", "time", String.valueOf(timeLeft)));
                        }
                    }
                }
            }
        }.runTaskTimer(CoralDuel.get().getPlugin(), 20L, 20L);
    }

    public void endDuel(@NotNull Duel duel, Player winner) {
        if (!activeDuels.contains(duel)) {
            return;
        }
        
        Duel endingDuel = duel.withState(DuelState.ENDING);
        activeDuels.set(activeDuels.indexOf(duel), endingDuel);
        
        for (Player player : duel.players()) {
            restorePlayerInventory(player, duel);
        UserStructure userStruct = UserStructure.getUser(player.getUniqueId());
        if (userStruct != null && userStruct.inDuel()) {
            UserStructure updated = userStruct.withInDuel(false);
            UserManager.get().updateCache(updated, false);
        }
            
            if (spawnLocation != null) {
                player.teleport(spawnLocation);
            }
            
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setSaturation(20);
            
            for (org.bukkit.potion.PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        }
        
        if (winner != null) {
            Player loser = duel.getOpponent(winner);
            
            winner.sendMessage(MessagesManager.getMessage("end.you-won", "player", loser.getName()));
            loser.sendMessage(MessagesManager.getMessage("end.you-lost", "player", winner.getName()));

            String winnerCommand = CoralDuel.get().getConfigFile().getString("commands.winner", "")
                    .replace("{player}", winner.getName());

            String loserCommand = CoralDuel.get().getConfigFile().getString("commands.loser", "")
                    .replace("{player}", loser.getName());

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), winnerCommand);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), loserCommand);

            UserStructure wUser = UserStructure.getUser(winner.getUniqueId());
            UserStructure lUser = UserStructure.getUser(loser.getUniqueId());
            if (wUser != null) {
                UserStructure updated = wUser.withDuelWins(wUser.duelWins() + 1).withDuelStreak(wUser.duelStreak() + 1);
                UserManager.get().updateCache(updated, false);
                UserManager.get().save(updated);
            }
            if (lUser != null) {
                UserStructure updated = lUser.withDuelLosses(lUser.duelLosses() + 1).withDuelStreak(0);
                UserManager.get().updateCache(updated, false);
                UserManager.get().save(updated);
            }

        } else {
            for (Player player : duel.players()) {
                player.sendMessage(MessagesManager.getMessage("end.duel-tie"));
            }
            for(Player p: duel.players()){
                UserStructure us=UserStructure.getUser(p.getUniqueId());
                if(us!=null){
                    UserStructure updated=us.withDuelsTied(us.duelsTied()+1).withDuelStreak(0);
                    UserManager.get().updateCache(updated,false);
                    UserManager.get().save(updated);
                }
            }    
        }
        
        Arena freeArena = duel.arena().withOccupied(false);
        ArenasManager.get().updateArena(freeArena);
        
        activeDuels.removeIf(d -> d.players().containsAll(duel.players()));
        
        for (Player player : duel.players()) {
            ScoreboardManager.get().updateScoreboard(player);
        }
    }
    
    private void restorePlayerInventory(Player player, Duel duel) {
        ItemStack[] savedInventory = duel.savedInventories().get(player);
        ItemStack[] savedArmor = duel.savedArmor().get(player);
        
        if (savedInventory != null) {
            player.getInventory().setContents(savedInventory);
        }
        if (savedArmor != null) {
            player.getInventory().setArmorContents(savedArmor);
        }
        player.updateInventory();
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        Duel duel = getDuelByPlayer(player);
        
        if (duel != null && duel.state() == DuelState.ACTIVE) {
            double finalHealth = player.getHealth() - event.getFinalDamage();
            
            if (finalHealth <= 0) {
                event.setCancelled(true);
                
                Player winner = duel.getOpponent(player);
                
                player.sendMessage(MessagesManager.getMessage("end.eliminated"));
                winner.sendMessage(MessagesManager.getMessage("end.you-eliminated", "player", player.getName()));
                
                endDuel(duel, winner);
            }
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Duel duel = getDuelByPlayer(player);
        
        if (duel != null && duel.state() == DuelState.ACTIVE) {
            event.setDeathMessage(null);
            
            Player winner = duel.getOpponent(player);
            
            player.sendMessage(MessagesManager.getMessage("end.eliminated"));
            winner.sendMessage(MessagesManager.getMessage("end.you-eliminated", "player", player.getName()));
            
            endDuel(duel, winner);
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Duel duel = getDuelByPlayer(player);
        
        if (duel != null && (duel.state() == DuelState.STARTING || duel.state() == DuelState.COUNTDOWN)) {
            if (event.getFrom().getX() != event.getTo().getX() ||
                event.getFrom().getY() != event.getTo().getY() || 
                event.getFrom().getZ() != event.getTo().getZ()) {
                
                event.setTo(event.getFrom().clone().setDirection(event.getTo().getDirection()));
                player.sendMessage(MessagesManager.getMessage("countdown.cannot-move"));
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Duel duel = getDuelByPlayer(player);
        
        if (duel != null) {
            Player opponent = duel.getOpponent(player);
            endDuel(duel, opponent);
        }
    }

    public Duel getDuelByPlayer(@NotNull Player player) {
        for (Duel duel : activeDuels) {
            if (duel.players().contains(player)) {
                return duel;
            }
        }
        return null;
    }
    
    public boolean isInDuel(@NotNull Player player) {
        return getDuelByPlayer(player) != null;
    }

    public static DuelsManager get() {
        return INSTANCE;
    }
}
