package net.mistersevent.pick.enchantment.attributes;

import net.mistersevent.pick.enchantment.enums.EnchantmentName;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentAttributes {

    private final EnchantmentName type;
    private final int maxLevel;
    private final double chance;

    public EnchantmentAttributes(EnchantmentName type, int maxLevel, double chance) {
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

    public EnchantmentName getType() {
        return type;
    }
}
