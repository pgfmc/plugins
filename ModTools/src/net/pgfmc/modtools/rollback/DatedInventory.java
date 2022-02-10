package net.pgfmc.modtools.rollback;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.file.Configify;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.modtools.Main;

public class DatedInventory extends Configify {
	
	private PlayerData pd;
	private long date;
	private ItemStack[] items;
	private ItemStack[] undoItems;
	
	private int taskId;
	
	public DatedInventory(PlayerData pd)
	{
		super(new File(Main.plugin.getDataFolder() + File.separator + "DatedInventory" + File.separator + pd.getUniqueId().toString() + ".yml"));
		
		this.pd = pd;
		date = new Date().getTime();
		items = pd.getPlayer().getInventory().getContents();
		
		taskId = startTimer();
	}
	
	/**
	 * Restore the player's inventory with the contents of this DatedInventory
	 * 
	 * @return if the player's inventory was successfully restored
	 */
	public boolean restore()
	{
		if (pd == null || !pd.isOnline() || items == null)
		{
			Bukkit.getLogger().warning(pd.getRankedName() + "'s inventory could not be restored.");
			return false;
		}
		
		undoItems = pd.getPlayer().getInventory().getContents();
		pd.getPlayer().getInventory().setContents(items);
		
		Bukkit.getLogger().warning(pd.getRankedName() + "'s inventory successfully restored!");
		return true;
	}
	
	public boolean undo()
	{
		if (undoItems == null) return false;
		
		pd.getPlayer().getInventory().setContents(undoItems);
		undoItems = null;
		
		Bukkit.getLogger().warning(pd.getRankedName() + "'s inventory successfully undid!");
		return true;
	}
	
	public PlayerData getPlayerData()
	{
		return pd;
	}
	
	public long getDate()
	{
		return date;
	}
	
	public int getMinutesAgo()
	{
		return (int) ((new Date().getTime() - date) / 60000);
	}
	
	public static int getMinutesAgo(Long date)
	{
		return (int) ((new Date().getTime() - date) / 60000);
	}
	
	public ItemStack[] getItems()
	{
		return items;
	}
	
	private int startTimer()
	{
		return Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				LinkedList<DatedInventory> inventories = (LinkedList<DatedInventory>) Optional.ofNullable(pd.getData("dated-inventories")).orElse(new LinkedList<DatedInventory>());
				DatedInventory that = inventories.remove(0);
				FileConfiguration file = getConfig();
				
				file.set("dated-inventories." + String.valueOf(date), that);
				save(file);
			}
		}, 30 * 60 * 20, 60 * 20); // 30 minutes
	}

	@Override
	public void reload() {}

	@Override
	public void enable() {}

	@Override
	public void disable() {
		Bukkit.getScheduler().cancelTask(taskId);
		
	}

}
