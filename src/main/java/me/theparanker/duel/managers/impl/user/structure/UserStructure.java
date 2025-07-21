package me.theparanker.duel.managers.impl.user.structure;

import me.theparanker.duel.managers.impl.user.core.UserManager;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public record UserStructure(
    UUID uuid,
    String name,
    int duelWins,
    int duelLosses,
    int duelsTied,
    int duelStreak,
    // NOT TO STORE IN DATABASE \/
    boolean inDuel,
    ArrayList<ItemStack> inventory,
    ArrayList<ItemStack> armor,
    ItemStack offHand
) {
    public static UserStructure getUser(UUID uuid) {
        return UserManager.get().getUsers().get(uuid);
    }
}
