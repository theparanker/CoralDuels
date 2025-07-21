package me.theparanker.duel.managers.impl.user.listener;

import me.theparanker.duel.managers.impl.user.core.UserManager;
import me.theparanker.duel.managers.impl.user.structure.UserStructure;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataListener implements Listener {

    @EventHandler
    public void preLoginEvent(AsyncPlayerPreLoginEvent e) {
        UserManager.get().loadUser(e.getUniqueId(), e.getName()).thenAccept(user -> {
            if (user == null) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Failed to load user data.");
            }else {
                UserManager.get().updateCache(user, false);
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UserStructure user = UserStructure.getUser(player.getUniqueId());
        UserManager.get().save(user);
        UserManager.get().updateCache(user, true);
    }

}
