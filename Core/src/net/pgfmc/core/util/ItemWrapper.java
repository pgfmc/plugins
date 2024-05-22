package net.pgfmc.core.util;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
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
		imeta.setDisplayName(ChatColor.RESET + name);
		item.setItemMeta(imeta);
		return this;
	}
	
	public ItemWrapper l(String lore) {
		return l(Arrays.asList(lore.split("\n")));
	}
	
	public ItemWrapper l(List<String> lore)
	{
		for (int i = 0; i < lore.size(); i++)
		{
			if (i > 0)
			{
				lore.set(i, ChatColor.RESET + ChatColor.getLastColors(lore.get(i - 1)) + lore.get(i));
				
			} else
			{
				lore.set(i, ChatColor.RESET + lore.get(i));
				
			}
			
		}
		
		ItemMeta imeta = item.getItemMeta();
		imeta.setLore(null); // clear lore first
		imeta.setLore(lore);
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
