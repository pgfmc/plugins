package net.pgfmc.core.util;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

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
	
	public ItemWrapper amount(int a) {
		item.setAmount(a);
		return this;
	}
	
	public ItemWrapper name(Component name) {
		ItemMeta imeta = item.getItemMeta();
		imeta.displayName(name);
		item.setItemMeta(imeta);
		return this;
	}
	
	//public ItemWrapper l(String lore) {
	//	return l(Arrays.asList(lore.split("\n")));
	//}
	
	public ItemWrapper lore(List<Component> lore) {
		ItemMeta imeta = item.getItemMeta();
		imeta.lore(null); // clear lore first
		imeta.lore(lore);
		item.setItemMeta(imeta);
		return this;
	}

    public ItemWrapper lore(Component... arguments) {
        this.lore(Arrays.asList(arguments));
        return this;
    }
	
	public ItemWrapper material(Material mat) {

        int amount = amount();
        Component name = name();
        List<Component> lore = lore();

        this.item = new ItemStack(mat);

        amount(amount);
        name(name);
        lore(lore);

		return this;
	}
	
	public ItemStack item() {
		return item;
	}
	
	public int amount() {
		return item.getAmount();
	}
	
	public Component name() {
		return item.getItemMeta().displayName();
	}
	
	public List<Component> lore() {
		return item.getItemMeta().lore();
	}
	
	public Material material() {
		return item.getType();
	}
}
