package me.theparanker.duel.managers.impl.kits.structure;

import org.bukkit.inventory.ItemStack;

public record Kit(
        String name,
        String displayName,
        ItemStack icon,
        ItemStack[] armor,
        ItemStack[] inventoryContents
) {}

