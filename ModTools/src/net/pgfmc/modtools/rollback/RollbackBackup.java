package net.pgfmc.modtools.rollback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.Main;

public class RollbackBackup {
	
	UUID uuid;
	ItemStack[] inventoryContents;
	float exp;
	
	int taskId;
	BackupCause cause;
	
	Date timeOf = new Date();

	@SuppressWarnings("unchecked")
	public RollbackBackup(PlayerData pd, BackupCause cause)
	{
		//super(InventoryType.CHEST.getDefaultSize(), InventoryRollback.INVENTORY_DATE_FORMAT.format(new Date()));
		
		if (pd.getPlayer() == null) return;
		
		Player p = pd.getPlayer();
		
		uuid = p.getUniqueId();
		inventoryContents = p.getInventory().getContents();
		exp = p.getExp();
		
		List<RollbackBackup> inventories = (List<RollbackBackup>) Optional.ofNullable(((List<RollbackBackup>) pd.getData("inventories")))
					.orElse(new ArrayList<RollbackBackup>());
		
		inventories.add(this);
		
		pd.setData("inventories", inventories);
		
		taskId = Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {

			@Override
			public void run() {
				// TODO remove this from instances and save to file
				List<RollbackBackup> inventories = (List<RollbackBackup>) Optional.ofNullable(((List<RollbackBackup>) pd.getData("inventories")))
						.orElse(new ArrayList<RollbackBackup>());
				
				RollbackBackup inventory = inventories.get(0);
				
				inventories.remove(0);
				
				pd.setData("inventories", inventories);
				
				inventory.saveToFile(); // TODO
				
				
				
			}
			
		}, 20 * 60 * 60).getTaskId(); // 1 hour
		
		this.cause = cause;
		
	}
	
	public int getTaskId()
	{
		return taskId;
	}
	
	public Player getPlayer()
	{
		return Bukkit.getPlayer(uuid);
	}
	
	public Map<String, Integer> getInventoryContents()
	{
		List<ItemStack> contents = new ArrayList<>(Arrays.asList(inventoryContents));
		Map<String, Integer> readableInventoryContents = new HashMap<>();
		
		contents.stream().forEach(item -> {
			readableInventoryContents.put(item.getType().name(), item.getAmount());
		});
		
		return readableInventoryContents;
	}
	
	public float getExp()
	{
		return exp;
	}
	
	public BackupCause getCause()
	{
		return cause;
	}
	
	public Date getDate()
	{
		return timeOf;
	}
	
	public void saveToFile()
	{
		// TODO
	}
	
	public boolean restore()
	{
		Player p = Bukkit.getPlayer(uuid);
		
		if (p == null || !p.isOnline()) return false;
		
		new RollbackBackup(PlayerData.from(p), BackupCause.ROLLBACK);
		
		p.getInventory().setContents(inventoryContents);
		p.setExp(exp);
		
		return true;
	}

}
