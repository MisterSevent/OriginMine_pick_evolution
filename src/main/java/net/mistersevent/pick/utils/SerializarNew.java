package net.mistersevent.pick.utils;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SerializarNew {

    public static String convertToBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            Map<String, Object>[] serializedData = serializeItemStack(items);
            oos.writeObject(serializedData);
            oos.flush();
            return DatatypeConverter.printBase64Binary(bos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static ItemStack[] convertToItems(String base64) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(
                    DatatypeConverter.parseBase64Binary(base64));
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object result = ois.readObject();
            Map<String, Object>[] returnValue = (Map<String, Object>[]) result;
            return deserializeItemStack(returnValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ItemStack[]{
                new ItemStack(Material.AIR)
        };
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object>[] serializeItemStack(ItemStack[] items) {
        Map<String, Object>[] result = new Map[items.length];
        for (int i = 0; i < items.length; i++) {
            ItemStack is = items[i];
            if (is == null) {
                result[i] = new HashMap<>();
            } else {
                result[i] = is.serialize();
                if(!is.hasItemMeta())
                    continue;
                result[i].put("meta", is.getItemMeta().serialize());
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private static ItemStack[] deserializeItemStack(Map<String, Object>[] map) {
        ItemStack[] items = new ItemStack[map.length];

        for (int i = 0; i < items.length; i++) {
            Map<String, Object> s = map[i];
            if (s.size() == 0) {
                items[i] = null;
            } else {
                try {
                    if (s.containsKey("meta")) {
                        Map<String, Object> im = new HashMap<>(
                                (Map<String, Object>) s.remove("meta"));
                        im.put("==", "ItemMeta");
                        ItemStack is = ItemStack.deserialize(s);
                        is.setItemMeta((ItemMeta) ConfigurationSerialization
                                .deserializeObject(im));
                        items[i] = is;
                    } else {
                        items[i] = ItemStack.deserialize(s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    items[i] = null;
                }
            }

        }
        return items;
    }
}
