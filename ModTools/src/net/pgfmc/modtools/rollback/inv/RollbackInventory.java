package net.pgfmc.modtools.rollback.inv;

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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.Main;
import net.pgfmc.modtools.rollback.InventoryRollback;

public class RollbackInventory extends BaseInventory {
	
	UUID uuid;
	ItemStack[] inventoryContents;
	ItemStack[] echestInventoryContents;
	float exp;
	int taskId;
	
	Date timeOf = new Date();

	@SuppressWarnings("unchecked")
	public RollbackInventory(PlayerData pd)
	{
		super(InventoryType.CHEST.getDefaultSize(), InventoryRollback.INVENTORY_DATE_FORMAT.format(new Date()));
		
		if (pd.getPlayer() == null) return;
		
		Player p = pd.getPlayer();
		
		uuid = p.getUniqueId();
		inventoryContents = p.getInventory().getStorageContents();
		echestInventoryContents = p.getEnderChest().getStorageContents();
		exp = p.getExp();
		
		List<RollbackInventory> inventories = (List<RollbackInventory>) Optional.ofNullable(((List<RollbackInventory>) pd.getData("inventories")))
					.orElse(new ArrayList<RollbackInventory>());
		
		inventories.add(this);
		
		pd.setData("inventories", inventories);
		
		taskId = Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {

			@Override
			public void run() {
				// TODO remove this from instances and save to file
				List<RollbackInventory> inventories = (List<RollbackInventory>) Optional.ofNullable(((List<RollbackInventory>) pd.getData("inventories")))
						.orElse(new ArrayList<RollbackInventory>());
				
				RollbackInventory inventory = inventories.get(0);
				
				inventories.remove(0);
				
				pd.setData("inventories", inventories);
				
				inventory.saveToFile(); // TODO
				
				
				
			}
			
		}, 20 * 60 * 60).getTaskId(); // 1 hour
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
	
	public Map<String, Integer> getEchestInventoryContents()
	{
		List<ItemStack> echestContents = new ArrayList<>(Arrays.asList(echestInventoryContents));
		Map<String, Integer> readableEchestInventoryContents = new HashMap<>();
		
		echestContents.stream().forEach(item -> {
			readableEchestInventoryContents.put(item.getType().name(), item.getAmount());
		});
		
		return readableEchestInventoryContents;
	}
	
	public float getExp()
	{
		return exp;
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
		
		p.getInventory().setStorageContents(inventoryContents);
		p.getEnderChest().setStorageContents(echestInventoryContents);
		p.setExp(exp);
		
		return true;
	}

}
