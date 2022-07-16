package net.mistersevent.pick.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

public class NBTTag {

	private String nmsVersion;
	private Class<?> craftMetaItemClass;
	private Class<?> nbtTagCompound;

	private Field unhandledTagsField;

	private Method setString;
	private Method getString;

	public NBTTag() {
		nmsVersion = getNMSVersion();

		craftMetaItemClass = getOBCClass("inventory.CraftMetaItem");
		nbtTagCompound = getNMSClass("NBTTagCompound");

		try {
			if (nbtTagCompound != null) {
				setString = nbtTagCompound.getMethod("setString", String.class, String.class);
				getString = nbtTagCompound.getMethod("getString", String.class);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			if (craftMetaItemClass != null) {
				unhandledTagsField = craftMetaItemClass.getDeclaredField("unhandledTags");
				unhandledTagsField.setAccessible(true);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public Class<?> getNMSClass(String name) {
		try {
			return Class.forName("net.minecraft.server." + nmsVersion + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Class<?> getOBCClass(String name) {
		try {
			return Class.forName("org.bukkit.craftbukkit." + nmsVersion + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getNMSVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().split(Pattern.quote("."))[3];
	}

	@SuppressWarnings({ "unchecked" })
	public void setNBTString(ItemMeta meta, String key, String value) {
		try {
			Object tag = nbtTagCompound.newInstance();
			setString.invoke(tag, key, value);
			((Map<String, Object>) unhandledTagsField.get(meta)).put(key, tag);
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public String getNBTString(ItemMeta meta, String key) {
		try {
			Object tag = ((Map<String, Object>) unhandledTagsField.get(meta)).get(key);

			if (tag == null)
				return null;

			return (String) getString.invoke(tag, key);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void setNBTDouble(ItemMeta meta, String key, Double value) {
		setNBTString(meta, key, "" + value);
	}

	public Double getNBTDouble(ItemMeta meta, String key) {
		return Double.parseDouble(getNBTString(meta, key));
	}

	public void setNBTInteger(ItemMeta meta, String key, int value) {
		setNBTString(meta, key, "" + value);
	}

	public int getNBTInteger(ItemMeta meta, String key) {
		return Integer.parseInt(getNBTString(meta, key));
	}

}