package me.theparanker.duel.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ItemBuilder {
    private final ItemStack itemStack;

    public static ItemBuilder copyOf(ItemBuilder builder) {
        return new ItemBuilder(builder.get());
    }

    public static ItemBuilder copyOf(ItemStack item) {
        return new ItemBuilder(item);
    }

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(Math.min(amount, 64));
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLoreLine(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        lore.add(CC.translate(name));
        meta.setLore(lore);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(Collection<String> lore) {
        ArrayList toSet = new ArrayList();
        ItemMeta meta = this.itemStack.getItemMeta();
        lore.forEach(string -> toSet.add(CC.translate(string)));
        meta.setLore(toSet);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        this.itemStack.setDurability((short)durability);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setUnbreakable(unbreakable);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setGlowing(boolean glowing) {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta == null) {
            return this;
        }
        if (glowing) {
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            meta.addEnchant(Enchantment.SWIFT_SNEAK, 1, true);
        } else {
            meta.removeEnchant(Enchantment.SWIFT_SNEAK);
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setFlags(ItemFlag ... flags) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.addItemFlags(flags);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setData(int data) {
        this.itemStack.setData(new MaterialData(this.itemStack.getType(), (byte)data));
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment) {
        this.itemStack.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder setType(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(new ArrayList());
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        this.itemStack.getEnchantments().keySet().forEach(arg_0 -> ((ItemStack)this.itemStack).removeEnchantment(arg_0));
        return this;
    }

    public ItemBuilder setColor(Color color) {
        if (this.itemStack.getType() == Material.LEATHER_BOOTS || this.itemStack.getType() == Material.LEATHER_CHESTPLATE || this.itemStack.getType() == Material.LEATHER_HELMET || this.itemStack.getType() == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta)this.itemStack.getItemMeta();
            meta.setColor(color);
            this.itemStack.setItemMeta((ItemMeta)meta);
            return this;
        }
        throw new IllegalArgumentException("color() only applicable for leather armor.");
    }

    public ItemBuilder setOwner(String owner) {
        SkullMeta meta = (SkullMeta)this.itemStack.getItemMeta();
        meta.setOwner(owner);
        this.itemStack.setItemMeta((ItemMeta)meta);
        return this;
    }

    public ItemStack get() {
        return this.itemStack;
    }

    public ItemBuilder hideStackData() {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setColorArmor(Color color) {
        if (this.itemStack.getType() == Material.LEATHER_BOOTS || this.itemStack.getType() == Material.LEATHER_CHESTPLATE || this.itemStack.getType() == Material.LEATHER_HELMET || this.itemStack.getType() == Material.LEATHER_LEGGINGS) {
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            if (itemMeta instanceof LeatherArmorMeta) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)itemMeta;
                leatherArmorMeta.setColor(color);
                this.itemStack.setItemMeta((ItemMeta)leatherArmorMeta);
            }
            return this;
        }
        throw new IllegalArgumentException("color() only applicable for leather armor.");
    }

    public static void rename(ItemStack stack, String name) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        stack.setItemMeta(meta);
    }

    public static ItemStack reloreItem(ItemStack stack, String ... lore) {
        return ItemBuilder.reloreItem(ReloreType.OVERWRITE, stack, lore);
    }

    public static ItemStack reloreItem(ReloreType type, ItemStack stack, String ... lores) {
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new LinkedList<String>();
        }
        switch (type.ordinal()) {
            case 2: {
                lore.addAll(List.of(lores));
                meta.setLore(CC.translateStrings(lore));
                break;
            }
            case 1: {
                LinkedList<String> nLore = new LinkedList<String>(List.of(lores));
                nLore.addAll(CC.translateStrings(lore));
                meta.setLore(CC.translateStrings(nLore));
                break;
            }
            case 0: {
                meta.setLore(List.of(lores));
            }
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, String name, int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, String name, int amount, short damage) {
        ItemStack item = new ItemStack(material, amount, damage);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        item.setItemMeta(meta);
        return item;
    }


    public static enum ReloreType {
        OVERWRITE,
        PREPEND,
        APPEND;

    }
}
