package net.mistersevent.pick.adapter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.mistersevent.pick.model.UserModel;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class UserAdapter {

    private final Gson gson = new Gson();
    private final Type type = (new TypeToken<Map<Enchantment, Integer>>() {
    }).getType();

    public UserModel read(ResultSet resultSet) throws SQLException {
        UserModel userModel = new UserModel(UUID.fromString(resultSet.getString("id")), resultSet.getString("name"));
        userModel.setCount(resultSet.getDouble("count"));
        userModel.setXp(resultSet.getDouble("xp"));
        userModel.getEnchantments().putAll((Map) this.gson.fromJson(resultSet.getString("enchantments"), this.type));
        return userModel;
    }

    public void write(PreparedStatement stm, UserModel model) throws SQLException {
        stm.setString(1, model.getId().toString());
        stm.setString(2, model.getName());
        stm.setDouble(3, model.getCount());
        stm.setDouble(4, model.getXp());
        stm.setString(5, this.gson.toJson(model.getEnchantments()));
        stm.setDouble(6, model.getCount());
        stm.setDouble(7, model.getXp());
        stm.setString(8, this.gson.toJson(model.getEnchantments()));
    }
}
