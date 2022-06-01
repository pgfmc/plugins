package net.pgfmc.ffa.zone;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import net.pgfmc.core.file.Configify;
import net.pgfmc.core.file.Mixins;
import net.pgfmc.ffa.Main;

public class ZoneInfo extends Configify {
	
	public enum Zone {
		Safe,
		Combat;
		
		Inventory inventory;
		
		public void setInventoryItems(Inventory inventory)
		{
			this.inventory = inventory;
		}
		
		public Inventory getInventory()
		{
			return inventory;
		}
		
		public void switchZone(Player player)
		{
			player.getInventory().setContents(inventory.getContents());
		}
		
		public static Zone switchZoneUknown(Player player) 
		{
			Zone zone = getZoneFromLocation(player.getLocation());
			
			zone.switchZone(player);
			return zone;
		}

	}
	
	public ZoneInfo()
	{
		super(Mixins.getFile(Main.plugin.getDataFolder() + File.separator + "zone-items.yml"));
		
		setDefaultValue("safe", Bukkit.createInventory(null, InventoryType.PLAYER));
		setDefaultValue("combat", Bukkit.createInventory(null, InventoryType.PLAYER));
	}
	
	public static Zone getZoneFromLocation(Location loc)
	{
		int x = Math.abs(loc.getBlockX());
		int z = Math.abs(loc.getBlockZ());
		
		if (x >= 10 || z >= 10) return Zone.Combat;
		
		return Zone.Safe;
		
	}
	
	
	
	@Override
	public void reload() {
		FileConfiguration config = getConfig();
		
		Zone.Safe.setInventoryItems((Inventory) config.get("safe"));
		Zone.Combat.setInventoryItems((Inventory) config.get("combat"));
		
	}

	@Override
	public void enable() {
		reload();
	}

	@Override
	public void disable() {
		FileConfiguration config = getConfig();
		
		config.set("safe", Zone.Safe.getInventory());
		config.set("combat", Zone.Combat.getInventory());
		
		save(config);
	}
}
