package net.mistersevent.pick.controller;

import net.mistersevent.pick.Main;
import net.mistersevent.pick.cache.UserCache;
import net.mistersevent.pick.model.UserModel;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UserController {

    private final ExecutorService service;
    private final Main plugin;
    private final UserCache cache;

    public UserController(Main plugin) {
        this.service = new ThreadPoolExecutor(2, 4, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        this.plugin = plugin;
        this.cache = plugin.getUserCache();
    }

    public void load(Player p) {
        this.service.submit(() -> {
            UserModel model = this.cache.getById(p.getUniqueId());
            if (model == null) {
                model = new UserModel(p.getUniqueId(), p.getName());
                model.setCount(0.0D);
                model.setXp(0.0D);
                this.plugin.getUserDao().insertOrUpdate(model);
                this.cache.addElement(model);
            }
        });
    }

    public void save(Player p) {
        this.service.submit(() -> {
           UserModel model = this.cache.getById(p.getUniqueId());
            if (model != null) {
                this.plugin.getUserDao().insertOrUpdate(model);
            }
        });
    }
}
