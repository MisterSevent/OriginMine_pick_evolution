package net.mistersevent.pick.model;

import net.mistersevent.pick.Main;
import net.mistersevent.pick.enchantment.EnchantmentTool;
import net.mistersevent.pick.enchantment.enums.EnchantmentName;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserModel {

    private final UUID id;
    private final String name;
    private double count;
    private double xp;

    private double basePrice = Main.getInstance().getConfig().getDouble("xp-base-price");

    private final Map<EnchantmentName, Integer> enchantments = new HashMap<>();

    public UserModel(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getEnchantmentLevel(EnchantmentName type) {
        return (Integer) this.enchantments.getOrDefault(type, 0);
    }

    public void addEnchantment(EnchantmentName type, int level) {
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

    public Map<EnchantmentName, Integer> getEnchantments() {
        return this.enchantments;
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
