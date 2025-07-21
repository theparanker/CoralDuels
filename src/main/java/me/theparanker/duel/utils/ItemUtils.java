package me.theparanker.duel.utils;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public interface ItemUtils {

    static ItemStack deserializeItem(ConfigurationSection section) {
        if (section == null || !section.contains("type")) {
            return null;
        }

        Material material = Material.getMaterial(section.getString("type").toUpperCase());
        if (material == null) return null;

        ItemBuilder builder = new ItemBuilder(material);

        if (section.contains("amount")) {
            builder.setAmount(section.getInt("amount"));
        }

        if (section.contains("name")) {
            builder.setName(section.getString("name"));
        }

        if (section.contains("lore")) {
            builder.setLore(section.getStringList("lore"));
        }

        if (section.contains("enchantments")) {
            ConfigurationSection enchantsSection = section.getConfigurationSection("enchantments");
            for (String enchantKey : enchantsSection.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByName(enchantKey.toUpperCase());
                int level = enchantsSection.getInt(enchantKey);
                if (enchantment != null) {
                    builder.addEnchantment(enchantment, level);
                }
            }
        }

        if (section.contains("unbreakable")) {
            builder.setUnbreakable(section.getBoolean("unbreakable"));
        }

        return builder.get();
    }

}
