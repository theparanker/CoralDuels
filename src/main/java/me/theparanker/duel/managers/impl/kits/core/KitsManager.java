package me.theparanker.duel.managers.impl.kits.core;

import lombok.Getter;
import me.theparanker.duel.CoralDuel;
import me.theparanker.duel.managers.impl.kits.structure.Kit;
import me.theparanker.duel.managers.impl.user.core.UserManager;
import me.theparanker.duel.managers.impl.user.structure.UserStructure;
import me.theparanker.duel.managers.structure.Manager;
import me.theparanker.duel.utils.CC;
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

            String displayName = CC.translate(kitSection.getString("display-name", kitKey));

            String permission = kitSection.getString("permission");

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

            Kit kit = new Kit(kitKey, displayName, permission, icon, armor, inventory);
            kits.put(kitKey, kit);
        }
    }

    public Kit getKit(String name) {
        return kits.get(name);
    }
    
    public Map<String, Kit> getAllKits() {
        return new HashMap<>(kits);
    }

    public static KitsManager get() {
        return INSTANCE;
    }
}
