package net.mistersevent.pick.enchantment.attributes;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentAttributes {

    private final Enchantment type;
    private final int maxLevel;
    private final double chance;

    public EnchantmentAttributes(Enchantment type, int maxLevel, double chance) {
        this.type = type;
        this.maxLevel = maxLevel;
        this.chance = chance;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public double getChance() {
        return chance;
    }

    public Enchantment getType() {
        return type;
    }
}
