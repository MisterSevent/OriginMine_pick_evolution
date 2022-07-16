package net.mistersevent.pick.enchantment.attributes;

import net.mistersevent.pick.enchantment.enums.EnchantmentName;
import net.mistersevent.pick.utils.Cache;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.util.Iterator;

public class EnchantmentAttributesLoader {

    public void load(Cache<EnchantmentAttributes> cache, FileConfiguration configuration) {
        Iterator var1 = configuration.getConfigurationSection("enchantments").getKeys(false).iterator();

        while (var1.hasNext()) {
            String key = (String) var1.next();
            EnchantmentName type = EnchantmentName.valueOf(key);
            int maxLevel = configuration.getInt("enchantments." + key + ".max_level");
            double chance = configuration.getDouble("enchantments." + key + ".chance");
            EnchantmentAttributes attributes = new EnchantmentAttributes(type, maxLevel, chance);
            cache.addElement(attributes);
        }
    }
}
