package net.pgfmc.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Listing {
	
	private transient static ArrayList<Listing> instances = new ArrayList<Listing>();
	
	ItemStack itemBeingSold;
	ItemStack tradeItem;
	UUID playerUuid;
	
	
	// ------------------------------------------------------------------------ Constructors
	
	public Listing(OfflinePlayer seller, ItemStack itemBeingSold, ItemStack tradeItem) {
		this.itemBeingSold = itemBeingSold;
		
		this.playerUuid = seller.getUniqueId();
		
		if (tradeItem == null) {
			tradeItem = new ItemStack(Material.DIAMOND, 1);
		}
		this.tradeItem = tradeItem;
		
		ItemMeta itemMeta = this.itemBeingSold.getItemMeta();
		itemMeta.setLore(new ArrayList<String>());
		this.itemBeingSold.setItemMeta(itemMeta);
		
		instances.add(this);
	}
	
	// ------------------------------------------------------------------------ Save and Load
	
	@Deprecated
	public static void saveListings() {
		//Database.save(instances);
	}
	
	@Deprecated
	public static void loadListings() {
		//instances = new ArrayList<Listing>();
		//Database.load();
	}
	
	// ------------------------------------------------------------------------ Get Listings (instances)
	
	public static List<Listing> getListings() {
		List<Listing> list = new ArrayList<Listing>();
		for (Listing listing : instances) {
			list.add(listing);
		}
		return list;
	}

	// ------------------------------------------------------------------------ Get itemBeingSold / icon
	
	public ItemStack getItem() {
		return itemBeingSold;
	}
	
	// ------------------------------------------------------------------------ Get Seller
	
	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(playerUuid);
	}
	
	// ------------------------------------------------------------------------ Get Price
	
	public String getPrice() { // returns a string representation of the cost of this listing
		return Main.makePlural(tradeItem);
	}
	
	// ------------------------------------------------------------------------ Get TradeItem
	
	public ItemStack getTrade() {
		return tradeItem;
	}
	
	// ------------------------------------------------------------------------ Confirm / Buy
	
	public void deleteListing() {
		instances.remove(this);
		saveListings();
		loadListings();
	}
}