package net.mistersevent.pick.utils;

import net.mistersevent.pick.Main;
import org.bukkit.inventory.*;


import java.io.*;
import org.bukkit.entity.*;
import java.lang.reflect.*;

public class Reflection {
	private static String versionPrefix;
	private static Class<?> class_ItemStack;
	private static Class<?> class_NBTBase;
	private static Class<?> class_NBTTagCompound;
	private static Class<?> class_NBTTagList;
	private static Class<?> class_CraftItemStack;
	private static Class<?> class_NBTCompressedStreamTools;
	private static Class<?> class_EntityArmorStand;
	private static Class<?> class_PacketPlayOutSpawnEntityLiving;
	private static Class<?> class_EntityLiving;
	private static Class<?> class_PlayerConnection;
	private static Class<?> class_Packet;
	private static Class<?> class_PacketPlayOutEntityDestroy;
	private static Class<?> class_World;
	private static Class<?> class_CraftWorld;
	private static Class<?> class_Entity;
	private static Method method_asNMSCopy;
	private static Method method_SaveItem;
	private static Method method_Add;
	private static Method method_Save;
	private static Method method_A;
	private static Method method_CreateStack;
	private static Method method_AsBukkitCopy;
	private static Method method_SendPacket;
	private static Method method_GetId;
	private static Method method_Die;
	private static Method method_SetCustomName;
	private static Method method_SetCustomNameVisible;
	private static Method method_SetInvisible;
	private static Method method_SetGravity;
	private static Method method_GetHandle;
	private static Constructor<?> constructor_PacketPlayOutSpawnEntityLiving;
	private static Constructor<?> constructor_PacketPlayOutEntityDestroy;
	private static Constructor<?> constructor_EntityArmorStand;
	private static Main plugin;

	static {
		Reflection.versionPrefix = "";
	}

	public Reflection(Main m) {
		Reflection.plugin = m;
		loadClasses();
		loadMethods();
		loadConstructors();
	}

	public static void loadClasses() {
		try {
			final String className = Reflection.plugin.getServer().getClass().getName();
			final String[] packages = className.split("\\.");
			if (packages.length == 5) {
				Reflection.versionPrefix = String.valueOf(packages[3]) + ".";
			}
			Reflection.class_ItemStack = fixBukkitClass("net.minecraft.server.ItemStack");
			Reflection.class_NBTBase = fixBukkitClass("net.minecraft.server.NBTBase");
			Reflection.class_NBTTagCompound = fixBukkitClass("net.minecraft.server.NBTTagCompound");
			Reflection.class_NBTTagList = fixBukkitClass("net.minecraft.server.NBTTagList");
			Reflection.class_CraftItemStack = fixBukkitClass("org.bukkit.craftbukkit.inventory.CraftItemStack");
			Reflection.class_NBTCompressedStreamTools = fixBukkitClass("net.minecraft.server.NBTCompressedStreamTools");
			Reflection.class_EntityArmorStand = fixBukkitClass("net.minecraft.server.EntityArmorStand");
			Reflection.class_PacketPlayOutSpawnEntityLiving = fixBukkitClass(
					"net.minecraft.server.PacketPlayOutSpawnEntityLiving");
			Reflection.class_EntityLiving = fixBukkitClass("net.minecraft.server.EntityLiving");
			Reflection.class_PlayerConnection = fixBukkitClass("net.minecraft.server.PlayerConnection");
			Reflection.class_Packet = fixBukkitClass("net.minecraft.server.Packet");
			Reflection.class_PacketPlayOutEntityDestroy = fixBukkitClass(
					"net.minecraft.server.PacketPlayOutEntityDestroy");
			Reflection.class_World = fixBukkitClass("net.minecraft.server.World");
			Reflection.class_CraftWorld = fixBukkitClass("org.bukkit.craftbukkit.CraftWorld");
			Reflection.class_Entity = fixBukkitClass("net.minecraft.server.Entity");
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	public static void loadMethods() {
		try {
			Reflection.method_asNMSCopy = getClass_CraftItemStack().getMethod("asNMSCopy", ItemStack.class);
			Reflection.method_SaveItem = getClass_ItemStack().getMethod("save", getClass_NBTTagCompound());
			Reflection.method_Add = getClass_NBTTagList().getMethod("add", getClass_NBTBase());
			Reflection.method_Save = getClass_NBTCompressedStreamTools().getMethod("a", getClass_NBTTagCompound(),
					DataOutput.class);
			Reflection.method_A = getClass_NBTCompressedStreamTools().getMethod("a", DataInputStream.class);
			Reflection.method_CreateStack = getClass_ItemStack().getMethod("createStack", getClass_NBTTagCompound());
			Reflection.method_AsBukkitCopy = getClass_CraftItemStack().getMethod("asBukkitCopy", getClass_ItemStack());
			Reflection.method_SendPacket = getClass_PlayerConnection().getMethod("sendPacket", getClass_Packet());
			Reflection.method_GetId = getClass_Entity().getMethod("getId", (Class<?>[]) new Class[0]);
			Reflection.method_Die = getClass_Entity().getMethod("die", (Class<?>[]) new Class[0]);
			Reflection.method_SetCustomName = getClass_EntityArmorStand().getMethod("setCustomName", String.class);
			Reflection.method_SetCustomNameVisible = getClass_EntityArmorStand().getMethod("setCustomNameVisible",
					Boolean.TYPE);
			Reflection.method_SetInvisible = getClass_EntityArmorStand().getMethod("setInvisible", Boolean.TYPE);
			Reflection.method_SetGravity = getClass_EntityArmorStand().getMethod("setGravity", Boolean.TYPE);
			Reflection.method_GetHandle = getClass_CraftWorld().getMethod("getHandle", (Class<?>[]) new Class[0]);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	public static void loadConstructors() {
		try {
			Reflection.constructor_PacketPlayOutSpawnEntityLiving = Reflection.class_PacketPlayOutSpawnEntityLiving
					.getConstructor(Reflection.class_EntityLiving);
			Reflection.constructor_PacketPlayOutEntityDestroy = Reflection.class_PacketPlayOutEntityDestroy
					.getConstructor(int[].class);
			Reflection.constructor_EntityArmorStand = Reflection.class_EntityArmorStand
					.getConstructor(Reflection.class_World, Double.TYPE, Double.TYPE, Double.TYPE);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	private static Class<?> fixBukkitClass(String className) {
		className = className.replace("org.bukkit.craftbukkit.", "org.bukkit.craftbukkit." + Reflection.versionPrefix);
		className = className.replace("net.minecraft.server.", "net.minecraft.server." + Reflection.versionPrefix);
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object getConnection(final Player p) {
		Object toReturn = null;
		try {
			final Method method_GetHandle = p.getClass().getMethod("getHandle", (Class<?>[]) new Class[0]);
			final Object nmsPlayer = method_GetHandle.invoke(p, new Object[0]);
			final Field field_PlayerConnection = nmsPlayer.getClass().getField("playerConnection");
			toReturn = field_PlayerConnection.get(nmsPlayer);
		} catch (Exception ex) {
		}
		return toReturn;
	}

	private static Class<?> getClass_Entity() {
		return Reflection.class_Entity;
	}

	public static Method getMethod_GetHandle() {
		return Reflection.method_GetHandle;
	}

	private static Class<?> getClass_CraftWorld() {
		return Reflection.class_CraftWorld;
	}

	public static Method getMethod_SetCustomName() {
		return Reflection.method_SetCustomName;
	}

	public static Method getMethod_SetCustomNameVisible() {
		return Reflection.method_SetCustomNameVisible;
	}

	public static Method getMethod_SetInvisible() {
		return Reflection.method_SetInvisible;
	}

	public static Method getMethod_SetGravity() {
		return Reflection.method_SetGravity;
	}

	public static Constructor<?> getConstructor_EntityArmorStand() {
		return Reflection.constructor_EntityArmorStand;
	}

	public static Method getMethod_Die() {
		return Reflection.method_Die;
	}

	public static Method getMethod_GetId() {
		return Reflection.method_GetId;
	}

	public static Constructor<?> getConstructor_PacketPlayOutEntityDestroy() {
		return Reflection.constructor_PacketPlayOutEntityDestroy;
	}

	private static Class<?> getClass_Packet() {
		return Reflection.class_Packet;
	}

	public static Method getMethod_SendPacket() {
		return Reflection.method_SendPacket;
	}

	private static Class<?> getClass_PlayerConnection() {
		return Reflection.class_PlayerConnection;
	}

	public static Constructor<?> getConstructor_PacketPlayOutSpawnEntityLiving() {
		return Reflection.constructor_PacketPlayOutSpawnEntityLiving;
	}

	static Method getMethod_AsBukkitCopy() {
		return Reflection.method_AsBukkitCopy;
	}

	static Method getMethod_CreateStack() {
		return Reflection.method_CreateStack;
	}

	static Method getMethod_A() {
		return Reflection.method_A;
	}

	static Method getMethod_asNMSCopy() {
		return Reflection.method_asNMSCopy;
	}

	static Method getMethod_SaveItem() {
		return Reflection.method_SaveItem;
	}

	static Method getMethod_Add() {
		return Reflection.method_Add;
	}

	static Method getMethod_Save() {
		return Reflection.method_Save;
	}

	private static Class<?> getClass_EntityArmorStand() {
		return Reflection.class_EntityArmorStand;
	}

	private static Class<?> getClass_ItemStack() {
		return Reflection.class_ItemStack;
	}

	private static Class<?> getClass_NBTBase() {
		return Reflection.class_NBTBase;
	}

	static Class<?> getClass_NBTTagCompound() {
		return Reflection.class_NBTTagCompound;
	}

	static Class<?> getClass_NBTTagList() {
		return Reflection.class_NBTTagList;
	}

	private static Class<?> getClass_CraftItemStack() {
		return Reflection.class_CraftItemStack;
	}

	private static Class<?> getClass_NBTCompressedStreamTools() {
		return Reflection.class_NBTCompressedStreamTools;
	}
}
