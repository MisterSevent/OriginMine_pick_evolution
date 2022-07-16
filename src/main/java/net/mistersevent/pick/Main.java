package net.mistersevent.pick;

import net.mistersevent.pick.cache.EnchantmentAttributesCache;
import net.mistersevent.pick.cache.UserCache;
import net.mistersevent.pick.controller.UserController;
import net.mistersevent.pick.dao.UserDao;
import net.mistersevent.pick.enchantment.EnchantmentTool;
import net.mistersevent.pick.enchantment.attributes.EnchantmentAttributes;
import net.mistersevent.pick.enchantment.attributes.EnchantmentAttributesLoader;
import net.mistersevent.pick.listener.PlayerListener;
import net.mistersevent.pick.listener.ServerListener;
import net.mistersevent.pick.model.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.DriverManager;
import java.sql.SQLException;

public final class Main extends JavaPlugin {

    private static Main instance;
    private EnchantmentTool enchantmentTool;
    private UserCache userCache;
    private UserDao userDao;
    private UserController userController;

    private EnchantmentAttributesCache attributesCache;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        instance = this;
        this.userCache = new UserCache(this);
        this.attributesCache = new EnchantmentAttributesCache(this);
        this.enchantmentTool = new EnchantmentTool(this);
        this.userController = new UserController(this);

        try {
            this.userDao = new UserDao(DriverManager.getConnection(this.getConfig().getString("database.jdbcUrl"), this.getConfig().getString("database.username"), this.getConfig().getString("database.password")));
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

        this.userDao.loadAll();
        (new EnchantmentAttributesLoader()).load(attributesCache, this.getConfig());
        registerListeners();

    }

    @Override
    public void onDisable() {

        for (Player players : Bukkit.getOnlinePlayers()) {
            UserModel userModel = this.userCache.getById(players.getUniqueId());
            if (userModel != null) {
                this.userDao.insertOrUpdate(userModel);
            }
        }

    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ServerListener(this), this);
    }

    public static Main getInstance() {
        return instance;
    }

    public EnchantmentTool getEnchantmentTool() {
        return enchantmentTool;
    }

    public UserCache getUserCache() {
        return userCache;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public UserController getUserController() {
        return userController;
    }

    public EnchantmentAttributesCache getAttributesCache() {
        return attributesCache;
    }
}
