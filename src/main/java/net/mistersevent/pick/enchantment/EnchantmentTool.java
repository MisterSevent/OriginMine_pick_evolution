package net.mistersevent.pick.enchantment;

import net.mistersevent.pick.Main;
import net.mistersevent.pick.enchantment.enums.EnchantmentName;
import net.mistersevent.pick.model.UserModel;
import net.mistersevent.pick.utils.NBTTag;
import net.mistersevent.pick.utils.Serialize;
import net.mistersevent.pick.utils.Toolchain;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnchantmentTool {

    private final Main plugin;


    public EnchantmentTool(Main plugin) {
        this.plugin = plugin;
    }

    public ItemStack provideItem(UserModel userModel) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_PICKAXE);
        this.updateMeta(itemStack, userModel);
        String serialized = Serialize.toBase64(itemStack);
        return itemStack;
    }

    public void updateMeta(ItemStack itemStack, UserModel userModel) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.values());
        itemMeta.setDisplayName("§bPicareta §7[" + Toolchain.formatWithoutReduce(userModel.getCount() + 1)  + "]");
        List<String> lore = new ArrayList();
        lore.add("§7Inquebrável ∞");
        for (Enchantment value : Enchantment.values()) {
            int level = userModel.getEnchantmentLevel(EnchantmentName.valueOf(value));
            if (level > 0) {
                lore.add("§7" + EnchantmentName.valueOf(value) + " " + Toolchain.formatWithoutReduce(level));
                itemMeta.addEnchant(value, level, true);
            }

        }

        lore.add("");
        lore.add("§7XP: " + userModel.getXp() + "/" + userModel.getBasePrice() * (1 + userModel.getCount()));

        itemMeta.addEnchant(Enchantment.DURABILITY, 30000, true);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    private List<String> parseLore(UserModel userModel){
        List<String> lore = new ArrayList();
        lore.add("§7Inquebrável ∞");
        for (Enchantment value : Enchantment.values()) {
            int level = userModel.getEnchantmentLevel(EnchantmentName.valueOf(value));
            if (level > 0) {
                lore.add("§7" + value.getName() + " " + Toolchain.formatWithoutReduce(level));
            }
        }

        lore.add("§7XP: " + userModel.getXp() + "/" + userModel.getBasePrice() * (1 + userModel.getCount()));
        lore.add("");

        return lore;
    }

    public void updateDisplayName(ItemStack itemStack, UserModel userModel) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§bPicareta §7[" + userModel.getCount() + "]");
        itemStack.setItemMeta(itemMeta);
    }
}
