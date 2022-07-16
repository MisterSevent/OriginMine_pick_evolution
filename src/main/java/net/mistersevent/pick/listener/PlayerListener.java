package net.mistersevent.pick.listener;

import net.mistersevent.pick.Main;
import net.mistersevent.pick.cache.UserCache;
import net.mistersevent.pick.model.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            Main.getInstance().getUserController().load(e.getPlayer());
            UserModel userModel = Main.getInstance().getUserCache().getById(e.getPlayer().getUniqueId());
            p.getInventory().addItem(Main.getInstance().getEnchantmentTool().provideItem(userModel));
        }, 40L);

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UserModel userModel = Main.getInstance().getUserCache().getById(p.getUniqueId());
        if (userModel != null) {
            userModel.removeIfContains(p.getInventory());
        }
    }
}
