package net.mistersevent.pick.cache;

import net.mistersevent.pick.Main;
import net.mistersevent.pick.enchantment.attributes.EnchantmentAttributes;
import net.mistersevent.pick.utils.Cache;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentAttributesCache extends Cache<EnchantmentAttributes> {

    private final Main plugin;

    public EnchantmentAttributesCache(Main plugin) {
        this.plugin = plugin;
    }

    public EnchantmentAttributes getByType (Enchantment type) {
        return (EnchantmentAttributes) this.get((attributes) -> {
           return attributes.getType() == type;
        });
    }
}
