package me.theparanker.duel.managers.impl.kits.core;

import lombok.Getter;
import me.theparanker.duel.CoralDuel;
import me.theparanker.duel.managers.impl.kits.structure.Kit;
import me.theparanker.duel.managers.structure.Manager;
import me.theparanker.duel.utils.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class KitsManager implements Manager {

    private static KitsManager INSTANCE;
    private Map<String, Kit> kits;

    @Override
    public void start() {
        INSTANCE = this;
        this.kits = new HashMap<>();
        initializeKits();
    }

    @Override
    public void stop() {
        INSTANCE = null;
    }

    private void initializeKits() {
        ConfigurationSection kitsSection = CoralDuel.get().getKitsFile().getConfigurationSection("Kits");

        if (kitsSection == null) return;

        for (String kitKey : kitsSection.getKeys(false)) {
            ConfigurationSection kitSection = kitsSection.getConfigurationSection(kitKey);

            String displayName = kitSection.getString("display-name", kitKey);

            ConfigurationSection iconSection = kitSection.getConfigurationSection("icon");
            ItemStack icon = ItemUtils.deserializeItem(iconSection);

            ItemStack[] armor = new ItemStack[4];
            ConfigurationSection armorSection = kitSection.getConfigurationSection("armor");

            if (armorSection != null) {
                if (armorSection.contains("helmet"))
                    armor[3] = ItemUtils.deserializeItem(armorSection.getConfigurationSection("helmet"));
                if (armorSection.contains("chestplate"))
                    armor[2] = ItemUtils.deserializeItem(armorSection.getConfigurationSection("chestplate"));
                if (armorSection.contains("leggings"))
                    armor[1] = ItemUtils.deserializeItem(armorSection.getConfigurationSection("leggings"));
                if (armorSection.contains("boots"))
                    armor[0] = ItemUtils.deserializeItem(armorSection.getConfigurationSection("boots"));
            }


            ItemStack[] inventory = new ItemStack[36];
            ConfigurationSection invSection = kitSection.getConfigurationSection("inventory");
            if (invSection != null) {
                for (String slot : invSection.getKeys(false)) {
                    int index = Integer.parseInt(slot);
                    inventory[index] = ItemUtils.deserializeItem(invSection.getConfigurationSection(slot));
                }
            }

            Kit kit = new Kit(kitKey, displayName, icon, armor, inventory);
            kits.put(kitKey, kit);
        }
    }

    private void giveKitToPlayer(Player player, Kit kit) {
        player.getInventory().clear();

        ItemStack[] armor = kit.armor();
        if (armor != null) {
            if (armor.length > 0 && armor[0] != null) player.getInventory().setBoots(armor[0]);
            if (armor.length > 1 && armor[1] != null) player.getInventory().setLeggings(armor[1]);
            if (armor.length > 2 && armor[2] != null) player.getInventory().setChestplate(armor[2]);
            if (armor.length > 3 && armor[3] != null) player.getInventory().setHelmet(armor[3]);
        }

        ItemStack[] contents = kit.inventoryContents();
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                if (contents[i] != null) {
                    player.getInventory().setItem(i, contents[i]);
                }
            }
        }

        player.updateInventory();
    }


    public static KitsManager get() {
        return INSTANCE;
    }
}
