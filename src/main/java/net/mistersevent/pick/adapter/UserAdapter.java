package net.mistersevent.pick.adapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.mistersevent.pick.Main;
import net.mistersevent.pick.enchantment.EnchantmentTool;
import net.mistersevent.pick.enchantment.enums.EnchantmentName;
import net.mistersevent.pick.model.UserModel;
import net.mistersevent.pick.utils.NBTTag;
import net.mistersevent.pick.utils.Serialize;
import net.mistersevent.pick.utils.Toolchain;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class UserAdapter {



    private final Gson gson;
    private final Type type;

    public UserAdapter() {
        this.gson = new Gson();
        this.type = (new TypeToken<Map<EnchantmentName, Integer>>() {
        }).getType();;
    }

    public UserModel read(ResultSet resultSet) throws SQLException {

        UserModel userModel = new UserModel(UUID.fromString(resultSet.getString("id")), resultSet.getString("name"));
        ItemStack itemStack = Serialize.fromBase64(resultSet.getString("enchantments"));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setItemMeta(itemMeta);

        if (itemMeta.getEnchantLevel(Enchantment.DIG_SPEED) > 0) {
            userModel.addEnchantment(EnchantmentName.valueOf(Enchantment.DIG_SPEED), itemMeta.getEnchantLevel(Enchantment.DIG_SPEED));
        }

        if (itemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) > 0) {
            userModel.addEnchantment(EnchantmentName.valueOf(Enchantment.LOOT_BONUS_BLOCKS), itemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS));
        }

        Main.getInstance().getEnchantmentTool().updateMeta(itemStack, userModel);

        userModel.setCount(resultSet.getDouble("count"));
        userModel.setXp(resultSet.getDouble("xp"));

        for (Enchantment value : Enchantment.values()) {
            int level = userModel.getEnchantmentLevel(EnchantmentName.valueOf(value));
            if (level > 0) {
                userModel.getEnchantments().put(EnchantmentName.valueOf(value), level);
            }

        }

        System.out.println(userModel.getEnchantments());
        return userModel;
    }

    public void write(PreparedStatement stm, UserModel model) throws SQLException {
        stm.setString(1, model.getId().toString());
        stm.setString(2, model.getName());
        stm.setDouble(3, model.getCount());
        stm.setDouble(4, model.getXp());
        stm.setString(5, Serialize.toBase64(Main.getInstance().getEnchantmentTool().provideItem(model)));
        stm.setDouble(6, model.getCount());
        stm.setDouble(7, model.getXp());
        stm.setString(8, Serialize.toBase64(Main.getInstance().getEnchantmentTool().provideItem(model)));
    }
}
