package net.pgfmc.ffa.zone;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import net.pgfmc.core.file.Configify;
import net.pgfmc.core.file.Mixins;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.ffa.Main;
import net.pgfmc.ffa.zone.zones.Combat;
import net.pgfmc.ffa.zone.zones.Safe;

public class ZoneInfo extends Configify {
	
	public enum Zone {
		
		SAFE(new Safe()),
		COMBAT(new Combat());
		
		Inventory inventory;
		ZoneDo zoneDoClass;
		
		private Zone(ZoneDo zoneDoClass)
		{
			this.zoneDoClass = zoneDoClass;
		}
		
		public void zoneDo(EntityDamageByEntityEvent e)
		{
			zoneDoClass.zoneDo(e);
		}
		
		public void setInventoryItems(Inventory inventory)
		{
			// Not sure if this is completely necessary, but I
			// like the idea of a blank inventory template
			// instead of copying the admin's inventory
			Inventory tempInventory = Bukkit.createInventory(null, InventoryType.PLAYER);
			tempInventory.setContents(inventory.getContents());
			
			this.inventory = tempInventory;
		}
		
		public Inventory getInventory()
		{
			return inventory;
		}
		
		public void switchZone(Player player)
		{
			PlayerData.setData(player, "zone", this);
			
			if (inventory.getContents() == null)
			{
				Bukkit.getLogger().warning("Inventory contents is null in FFA#ZoneInfo! Cannot change zone inventory.");
				return;
			}
			
			player.getInventory().setContents(inventory.getContents());
		}
		
		public static Zone switchZoneUnknown(Player player) 
		{
			Zone zone = getZoneFromLocation(player.getLocation());
			
			if (PlayerData.getData(player, "zone") == zone) return null;
			
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
		
		if (x >= 10 || z >= 10) return Zone.COMBAT;
		
		return Zone.SAFE;
		
	}
	
	
	
	@Override
	public void reload() {
		FileConfiguration config = getConfig();
		
		Zone.SAFE.setInventoryItems((Inventory) config.get("safe"));
		Zone.COMBAT.setInventoryItems((Inventory) config.get("combat"));
		
		Bukkit.getLogger().fine("Configify loaded for ZoneInfo in FFA!");
	}

	@Override
	public void enable() {
		reload();
	}

	@Override
	public void disable() {
		FileConfiguration config = getConfig();
		
		config.set("safe", Zone.SAFE.getInventory());
		config.set("combat", Zone.COMBAT.getInventory());
		
		save(config);
	}
}
