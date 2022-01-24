package net.pgfmc.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Listing {
	
	private transient static ArrayList<Listing> instances = new ArrayList<Listing>();
	
	public final ItemStack product;
	public final ItemStack cost;
	public final UUID playerUuid;
	
	
	// ------------------------------------------------------------------------ Constructors
	
	public Listing(OfflinePlayer seller, ItemStack itemBeingSold, ItemStack tradeItem) {
		this.product = itemBeingSold;
		
		this.playerUuid = seller.getUniqueId();
		
		if (tradeItem == null) {
			tradeItem = new ItemStack(Material.DIAMOND, 1);
		}
		this.cost = tradeItem;
		
		ItemMeta itemMeta = this.product.getItemMeta();
		itemMeta.setLore(new ArrayList<String>());
		this.product.setItemMeta(itemMeta);
		
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
	
	// ------------------------------------------------------------------------ Get Price
	
	public String getPrice() { // returns a string representation of the cost of this listing
		return Main.makePlural(cost);
	}
	
	// ------------------------------------------------------------------------ Confirm / Buy
	
	public void deleteListing() {
		instances.remove(this);
		saveListings();
		loadListings();
	}
}