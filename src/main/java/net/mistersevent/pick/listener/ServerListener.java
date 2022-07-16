package net.mistersevent.pick.listener;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.mistersevent.pick.Main;
import net.mistersevent.pick.chance.XP;
import net.mistersevent.pick.enchantment.attributes.EnchantmentAttributes;
import net.mistersevent.pick.enchantment.enums.EnchantmentName;
import net.mistersevent.pick.model.UserModel;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ServerListener implements Listener {

    private final Main plugin;
    private final Random random = new Random();

    public ServerListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        if (b.getType().name().contains("ORE") && p.getGameMode() == GameMode.SURVIVAL) {
            UserModel userModel = this.plugin.getUserCache().getById(p.getUniqueId());
            this.addXp(b, userModel);

            if (userModel.getXp() >= userModel.getBasePrice() * (userModel.getCount() + 1)) {
                userModel.setXp(0.0D);
                userModel.setCount(userModel.getCount() + 1);
                setEnchantment(userModel);
            }

            this.plugin.getEnchantmentTool().updateMeta(p.getItemInHand(), userModel);
            p.updateInventory();
        }

        if (!b.getType().name().contains("ORE") && p.getGameMode() == GameMode.SURVIVAL) {
            e.setCancelled(true);
            p.sendMessage(Main.getInstance().getConfig().getString("only-ores").replace("&", "§"));
        }
    }

    private void addXp(Block b, UserModel userModel) {

        Material type = b.getType();

        switch (type) {
            case EMERALD_ORE:
                userModel.setXp(userModel.getXp() + XP.EMERALD.getXp());
            case DIAMOND_ORE:
                userModel.setXp(userModel.getXp() + XP.DIAMOND.getXp());
            case REDSTONE_ORE:
                userModel.setXp(userModel.getXp() + XP.REDSTONE.getXp());
            case LAPIS_ORE:
                userModel.setXp(userModel.getXp() + XP.LAPIS.getXp());
            case GOLD_ORE:
                userModel.setXp(userModel.getXp() + XP.GOLD.getXp());
            case COAL_ORE:
                userModel.setXp(userModel.getXp() + XP.COAL.getXp());
        }

    }

    private boolean addEnchantment(UserModel userModel, Enchantment type) {

        Player p = Bukkit.getPlayer(userModel.getId());
        EnchantmentAttributes attributes = Main.getInstance().getAttributesCache().getByType(type);
        int currentLevel = userModel.getEnchantmentLevel(type);

        if (currentLevel < attributes.getMaxLevel()) {
            userModel.addEnchantment(type, 1);
            return true;
        }
        return false;
    }

    private void setEnchantment(UserModel userModel) {

        Iterator var1 = Main.getInstance().getAttributesCache().getElements().iterator();
        List<Pair<Enchantment, Double>> list = new ArrayList<>();
        Player player = Bukkit.getPlayer(userModel.getName());
        while (var1.hasNext()) {
            EnchantmentAttributes attributes = (EnchantmentAttributes) var1.next();

            if (userModel.getEnchantmentLevel(attributes.getType()) < attributes.getMaxLevel()) {
                list.add(new Pair<>(attributes.getType(), attributes.getChance()));
            }
        }

        if (list.isEmpty()) {
            player.sendMessage(Main.getInstance().getConfig().getString("pickaxe-dont-have-more-levels").replace("&", "§"));
            return;
        }

        EnumeratedDistribution e = new EnumeratedDistribution(list);
        Enchantment encatamento = (Enchantment) e.sample();
        addEnchantment(userModel, encatamento);
        player.sendMessage("");
        TextComponent level = new net.md_5.bungee.api.chat.TextComponent(Main.getInstance().getConfig().getString("pickaxe-level-up").replace("&", "§"));
        level.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§7Encantamento: §b" + EnchantmentName.valueOf(encatamento))).create()));
        player.spigot().sendMessage(level);
        player.sendMessage("");
    }
}
