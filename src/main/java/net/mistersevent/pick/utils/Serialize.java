package net.mistersevent.pick.utils;

import org.bukkit.inventory.*;
import java.math.*;
import java.io.*;
import org.bukkit.*;
import org.yaml.snakeyaml.external.biz.base64Coder.*;
import org.bukkit.util.io.*;
import java.util.*;

public class Serialize {
	public static String toBase64(final ItemStack itemStack) {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final DataOutputStream dataOutput = new DataOutputStream(outputStream);
		try {
			final Object nbtTagListItems = Reflection.getClass_NBTTagList().newInstance();
			final Object nbtTagCompoundItem = Reflection.getClass_NBTTagCompound().newInstance();
			final Object nms = Reflection.getMethod_asNMSCopy().invoke(null, itemStack);
			Reflection.getMethod_SaveItem().invoke(nms, nbtTagCompoundItem);
			Reflection.getMethod_Add().invoke(nbtTagListItems, nbtTagCompoundItem);
			Reflection.getMethod_Save().invoke(null, nbtTagCompoundItem, dataOutput);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return new BigInteger(1, outputStream.toByteArray()).toString(32);
	}

	public static ItemStack fromBase64(final String data) {
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());
		Object nmsItem = null;
		Object toReturn = null;
		try {
			final Object nbtTagCompoundRoot = Reflection.getMethod_A().invoke(null, new DataInputStream(inputStream));
			if (nbtTagCompoundRoot != null) {
				nmsItem = Reflection.getMethod_CreateStack().invoke(null, nbtTagCompoundRoot);
			}
			toReturn = Reflection.getMethod_AsBukkitCopy().invoke(null, nmsItem);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return (ItemStack) toReturn;
	}

	public static String toBase64List(final List<ItemStack> items) {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream((OutputStream) outputStream);
			dataOutput.writeInt(items.size());
			int index = 0;
			for (final ItemStack is : items) {
				if (is != null && is.getType() != Material.AIR) {
					dataOutput.writeObject((Object) toBase64(is));
				} else {
					dataOutput.writeObject((Object) null);
				}
				dataOutput.writeInt(index);
				++index;
			}
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public static List<ItemStack> fromBase64List(final String items) {
		try {
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(items));
			final BukkitObjectInputStream dataInput = new BukkitObjectInputStream((InputStream) inputStream);
			final int size = dataInput.readInt();
			final List<ItemStack> list = new ArrayList<ItemStack>();
			for (int i = 0; i < size; ++i) {
				final Object utf = dataInput.readObject();
				dataInput.readInt();
				if (utf != null) {
					list.add(fromBase64((String) utf));
				}
			}
			dataInput.close();
			return list;
		} catch (Exception e) {
			throw new IllegalStateException("Unable to load item stacks.", e);
		}
	}
}