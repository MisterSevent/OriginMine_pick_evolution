package net.mistersevent.pick.model;

import net.mistersevent.pick.Main;
import net.mistersevent.pick.enchantment.EnchantmentTool;
import net.mistersevent.pick.enchantment.attributes.EnchantmentAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserModel {

    private final UUID id;
    private final String name;
    private double count;
    private double xp;

    private double basePrice = Main.getInstance().getConfig().getDouble("xp-base-price");

    private final Map<Enchantment, Integer> enchantments = new HashMap<>();

    public UserModel(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getEnchantmentLevel(Enchantment type) {
        return (Integer) this.enchantments.getOrDefault(type, 0);
    }

    public void addEnchantment(Enchantment type, int level) {
        level += this.getEnchantmentLevel(type);
        this.enchantments.put(type, level);
        Player p = Bukkit.getPlayer(this.id);
        if (p != null) {
            EnchantmentTool enchantmentTool = Main.getInstance().getEnchantmentTool();
            enchantmentTool.updateMeta(p.getItemInHand(), this);
        }
    }

    public void removeIfContains(Inventory inventory) {

        for (ItemStack content : inventory.getContents()) {
            if (content != null && content.getType() == Material.DIAMOND_PICKAXE && content.hasItemMeta() && content.getItemMeta().hasDisplayName() && content.getItemMeta().getDisplayName().contains("§bPicareta")) {
                inventory.removeItem(content);
            }
        }
    }

    public String getName() {
        return name;
    }

    public double getXp() {
        return xp;
    }

    public UUID getId() {
        return id;
    }

    public double getCount() {
        return count;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public double getBasePrice() {
        return basePrice;
    }
}
