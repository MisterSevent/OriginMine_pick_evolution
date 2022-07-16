package net.mistersevent.pick.cache;

import net.mistersevent.pick.Main;
import net.mistersevent.pick.model.UserModel;
import net.mistersevent.pick.utils.Cache;

import java.util.UUID;

public class UserCache extends Cache<UserModel> {
    private final Main plugin;

    public UserCache(Main plugin) {
        this.plugin = plugin;
    }

    public UserModel getById(UUID id) {
        return (UserModel)this.get((userModel) -> {
            return userModel.getId().equals(id);
        });
    }

    public UserModel getByName(String name) {
        return (UserModel)this.get((userModel) -> {
            return userModel.getName().equals(name);
        });
    }
}
