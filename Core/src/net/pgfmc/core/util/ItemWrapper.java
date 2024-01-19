package net.pgfmc.core.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemWrapper {
	
	protected ItemStack item;
	
	public ItemWrapper(ItemStack item) {
		if (item == null) {
			this.item = new ItemStack(Material.AIR);
			return;
		}
		
		this.item = item;
	}
	
	public ItemWrapper(Material mat) {
		if (mat == null) {
			mat = Material.AIR;
		}
		
		this.item = new ItemStack(mat);
	}
	
	public ItemWrapper a(int a) {
		item.setAmount(a);
		return this;
	}
	
	public ItemWrapper n(String name) {
		ItemMeta imeta = item.getItemMeta();
		imeta.setDisplayName(name);
		item.setItemMeta(imeta);
		return this;
	}
	
	public ItemWrapper l(String lore) {
		ItemMeta imeta = item.getItemMeta();
		
		String[] lorelist = lore.split("\n");
		ArrayList<String> liszt = new ArrayList<>(lorelist.length);
		for (String s : lorelist) {
			liszt.add(s);
		}
		imeta.setLore(liszt);
		item.setItemMeta(imeta);
		return this;
	}
	
	public ItemWrapper m(Material mat) {
		item.setType(mat);
		return this;
	}
	
	public ItemStack gi() {
		return item;
	}
	
	public int ga() {
		return item.getAmount();
	}
	
	public String gn() {
		return item.getItemMeta().getDisplayName();
	}
	
	public List<String> gl() {
		return item.getItemMeta().getLore();
	}
	
	public Material gm() {
		return item.getType();
	}
}
