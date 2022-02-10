package net.pgfmc.modtools.rollback;

import java.io.File;
import java.util.LinkedList;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import net.pgfmc.core.file.Configify;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.modtools.Main;

public class PlayerInventorySaver extends Configify {
	
	private PlayerData pd;
	private int taskId;
	
	public PlayerInventorySaver(PlayerData pd)
	{
		super(new File(Main.plugin.getDataFolder() + File.separator + "DatedInventory" + File.separator + pd.getUniqueId().toString() + ".yml"));
		
		this.pd = pd;
		taskId = startTimer();
	}
	

	private int startTimer()
	{
		return Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
				@Override
				public void run() {
					if (!pd.isOnline()) return;
					@SuppressWarnings("unchecked")
					LinkedList<DatedInventory> inventories = (LinkedList<DatedInventory>) Optional.ofNullable(pd.getData("dated-inventories"))
																							.orElse(new LinkedList<DatedInventory>());
					inventories.add(new DatedInventory(pd));
					
					pd.setData("dated-inventories", inventories);
				}
		}, 0, 60 * 20);
	}
	
	/**
	 * Restores a player's inventory
	 * 
	 * @param targetInMinutes the time at which the inventory will be replaced with
	 * @return successful or not
	 */
	public static boolean restore(PlayerData pd, Integer targetInMinutes)
	{
		if (targetInMinutes == null)
		{
			Bukkit.getLogger().warning("Could not restore inventory! Invalid time.");
			return false;
		}
		
		@SuppressWarnings("unchecked")
		LinkedList<DatedInventory> inventories = (LinkedList<DatedInventory>) Optional.ofNullable(pd.getData("dated-inventories"))
																				.orElse(new LinkedList<DatedInventory>());
		
		if (inventories.size() == 0)
		{			
			Bukkit.getLogger().warning("Could not restore inventory! No saved inventories.");
			return false;
		}
		
		for (DatedInventory i : inventories)
		{
			Bukkit.getLogger().warning("DatedInventory for " + pd.getRankedName() + ": " + i.getMinutesAgo());
			if (i.getMinutesAgo() != targetInMinutes) continue;
			
			pd.setData("undo-dated-inventory", i);
			
			return i.restore();
		}
		
		Bukkit.getLogger().warning("Could not restore inventory! No saved inventories " + (targetInMinutes + 1) + " minutes ago.");
		return false;
	}
	
	/**
	 * Undos the last restore
	 */
	public static boolean undo(PlayerData pd)
	{
		DatedInventory di = pd.getData("undo-dated-inventory");
		if (di == null) return false;
		
		return di.undo();
	}
	
	@Override
	public void reload() {
		Bukkit.getScheduler().cancelTask(taskId);
		taskId = startTimer();
	}
	
	@Override
	public void enable() {
		Bukkit.getLogger().warning("PlayerInventorySaver activated!");
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void disable() {
		Bukkit.getScheduler().cancelTask(taskId);
		
		LinkedList<DatedInventory> inventories = (LinkedList<DatedInventory>) Optional.ofNullable(pd.getData("dated-inventories")).orElse(new LinkedList<DatedInventory>());
		Bukkit.getLogger().warning("All saved inventories for " + pd.getRankedName() + ": " + inventories);
		FileConfiguration file = getConfig();
		
		inventories.stream().forEach(di -> file.set("dated-inventories." + String.valueOf(di.getDate()), di));
		
		//save(file);
	}
	
}