package net.mistersevent.pick.enchantment.enums;

import org.bukkit.enchantments.Enchantment;

public enum EnchantmentName {
    DIG_SPEED("Eficiência"),
    LOOT_BONUS_BLOCKS("Fortuna");

    private String name;

    private EnchantmentName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static EnchantmentName valueOf(Enchantment enchantment) {
        return valueOf(enchantment.getName());
    }

    public String toString() {
        return this.name;
    }
}
