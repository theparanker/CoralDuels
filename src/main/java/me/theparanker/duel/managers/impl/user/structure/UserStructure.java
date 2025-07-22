package me.theparanker.duel.managers.impl.user.structure;

import lombok.With;
import me.theparanker.duel.managers.impl.user.core.UserManager;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

@With
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

    @Override
    public String toString() {
        return "UserStructure{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", duelWins=" + duelWins +
                ", duelLosses=" + duelLosses +
                ", duelsTied=" + duelsTied +
                ", duelStreak=" + duelStreak +
                ", inDuel=" + inDuel +
                ", inventorySize=" + (inventory != null ? inventory.size() : 0) +
                ", armorSize=" + (armor != null ? armor.size() : 0) +
                ", offHand=" + (offHand != null ? offHand.getType() : "null") +
                '}';
    }
}
