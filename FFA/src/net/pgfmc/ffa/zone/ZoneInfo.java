package net.pgfmc.ffa.zone;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
		
		List<Map<String, Object>> zoneItemsMapList = new ArrayList<Map<String, Object>>();
		zoneItemsMapList.add(new ItemWrapper(Material.APPLE).a(16).gi().serialize());
		
		
		setDefaultValue("safe", zoneItemsMapList);
		setDefaultValue("combat", zoneItemsMapList);
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

	@SuppressWarnings("unchecked")
	@Override
	public void enable() {
		FileConfiguration config = getConfig();
		
		List<Map<?, ?>> safeZoneItemsMapList = config.getMapList("safe");
		List<Map<?, ?>> combatZoneItemsMapList = config.getMapList("combat");
		
		List<ItemStack> safeZoneItemsList = new ArrayList<ItemStack>();
		safeZoneItemsMapList.stream()
			.filter(map -> map != null)
			.forEach(map -> safeZoneItemsList
								.add(safeZoneItemsMapList.indexOf(map), ItemStack.deserialize((Map<String, Object>) map)));
		
		List<ItemStack> combatZoneItemsList = new ArrayList<ItemStack>();
		combatZoneItemsMapList.stream()
			.filter(map -> map != null)
			.forEach(map -> combatZoneItemsList
								.add(safeZoneItemsMapList.indexOf(map), ItemStack.deserialize((Map<String, Object>) map)));
		
		Zone.SAFE.setContents((ItemStack[]) safeZoneItemsList.toArray(new ItemStack[safeZoneItemsList.size() - 1]));
		Zone.COMBAT.setContents((ItemStack[]) combatZoneItemsList.toArray(new ItemStack[combatZoneItemsList.size() - 1]));
		
		Bukkit.getLogger().info("Configify loaded for ZoneInfo in FFA!");
	}

	@Override
	public void disable() {
		FileConfiguration config = getConfig();
		
		List<Map<String, Object>> safeZoneItemsMapList = new ArrayList<Map<String, Object>>();
		Arrays.asList(Zone.SAFE.getContents())
			.forEach(item -> safeZoneItemsMapList
								.add(Arrays.asList(Zone.SAFE.getContents())
										.indexOf(item), item.serialize()));
		
		List<Map<String, Object>> combatZoneItemsMapList = new ArrayList<Map<String, Object>>();
		Arrays.asList(Zone.COMBAT.getContents())
			.forEach(item -> combatZoneItemsMapList
								.add(Arrays.asList(Zone.COMBAT.getContents())
										.indexOf(item), item.serialize()));
		
		config.set("safe", safeZoneItemsMapList);
		config.set("combat", combatZoneItemsMapList);
		
		save(config);
		
		Bukkit.getLogger().info("Configify saved for ZoneInfo in FFA!");
	}
}
