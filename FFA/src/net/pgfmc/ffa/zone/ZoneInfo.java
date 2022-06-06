package net.pgfmc.ffa.zone;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.file.Configify;
import net.pgfmc.core.file.Mixins;
import net.pgfmc.core.inventoryAPI.extra.ItemWrapper;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.ffa.Main;
import net.pgfmc.ffa.zone.zones.Combat;
import net.pgfmc.ffa.zone.zones.Safe;

public class ZoneInfo extends Configify {
	
	public enum Zone {
		
		SAFE(new Safe()),
		COMBAT(new Combat());
		
		ItemStack[] contents;
		ZoneDo zoneDoClass;
		
		private Zone(ZoneDo zoneDoClass)
		{
			this.zoneDoClass = zoneDoClass;
		}
		
		public void zoneDo(EntityDamageByEntityEvent e)
		{
			zoneDoClass.zoneDo(e);
		}
		
		public void setContents(ItemStack[] contents)
		{
			
			this.contents = contents;
		}
		
		public ItemStack[] getContents()
		{
			return contents;
		}
		
		public void switchZone(Player player)
		{
			PlayerData.setData(player, "zone", this);
			
			if (contents == null)
			{
				Bukkit.getLogger().warning("Inventory contents is null in FFA#ZoneInfo! Cannot change zone inventory.");
				return;
			}
			
			player.getInventory().setContents(contents);
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
		super(Mixins.getFile(Main.plugin.getDataFolder() + File.separator + "zone-inventory-contents.yml"));
		
		ItemStack[] emptyInventory = Bukkit.createInventory(null, InventoryType.PLAYER).getContents();
		emptyInventory[0] = new ItemWrapper(Material.APPLE).a(1).gi();
		
		setDefaultValue("safe", emptyInventory);
		setDefaultValue("combat", emptyInventory);
	}
	
	public static Zone getZoneFromLocation(Location loc)
	{
		int x = Math.abs(loc.getBlockX());
		int z = Math.abs(loc.getBlockZ());
		
		if (x >= 10 || z >= 10) return Zone.COMBAT;
		
		return Zone.SAFE;
		
	}
	
	@Override
	public void reload() {}

	@Override
	public void enable() {
		FileConfiguration config = getConfig();
		
		ItemStack[] emptyInventory = Bukkit.createInventory(null, InventoryType.PLAYER).getContents();
		ItemStack[] templateInventory = new ItemStack[emptyInventory.length - 1];
		
		List<?> safeList = config.getList("safe");
		List<?> combatList = config.getList("combat");
		
		for (int i = 0; i < emptyInventory.length - 1; i++)
		{
			templateInventory[i] = (ItemStack) safeList.get(i);
		}
		
		Zone.SAFE.setContents(templateInventory.clone());
		
		for (int i = 0; i < emptyInventory.length - 1; i++)
		{
			templateInventory[i] = (ItemStack) combatList.get(i);
		}
		
		Zone.COMBAT.setContents(templateInventory.clone());
		
		Bukkit.getLogger().info("Configify loaded for ZoneInfo in FFA!");
	}

	@Override
	public void disable() {
		FileConfiguration config = getConfig();
		
		
		
		config.set("safe", Zone.SAFE.getContents());
		config.set("combat", Zone.COMBAT.getContents());
		
		save(config);
		
		Bukkit.getLogger().info("Configify saved for ZoneInfo in FFA!");
	}
}
