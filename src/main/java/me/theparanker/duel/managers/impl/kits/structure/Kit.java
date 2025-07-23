package me.theparanker.duel.managers.impl.kits.structure;

import lombok.With;
import org.bukkit.inventory.ItemStack;

@With
public record Kit(
        String name,
        String displayName,
        String permission,
        ItemStack icon,
        ItemStack[] armor,
        ItemStack[] inventoryContents
) {}

