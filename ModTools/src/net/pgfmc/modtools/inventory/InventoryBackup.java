package net.pgfmc.modtools.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.Main;

public class InventoryBackup {
	
	private UUID uuid;
	private Inventory inventory;
	private float exp;
	
	private int taskId;
	private InventoryBackupCause cause;
	
	private final Date timeOf = new Date();
	
	@SuppressWarnings("unchecked")
	public InventoryBackup(PlayerData pd, InventoryBackupCause cause)
	{
		if (pd.getPlayer() == null) return;
		
		Player p = pd.getPlayer();
		
		this.uuid = p.getUniqueId();
		this.inventory = p.getInventory();
		this.exp = p.getExp();
		
		List<InventoryBackup> inventories = (List<InventoryBackup>) Optional.ofNullable(((List<InventoryBackup>) pd.getData("inventories")))
					.orElse(new ArrayList<InventoryBackup>());
		
		inventories.add(this);
		
		pd.setData("inventories", inventories);
		
		this.taskId = setFutureDelete(); // 1 hour
		
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
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public float getExp()
	{
		return exp;
	}
	
	public InventoryBackupCause getCause()
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
		
		new InventoryBackup(PlayerData.from(p), InventoryBackupCause.ROLLBACK);
		
		p.getInventory().setContents(inventory.getContents());
		p.setExp(exp);
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public int setFutureDelete()
	{
		return Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {

			@Override
			public void run() {
				// TODO remove this from instances and save to file
				List<InventoryBackup> inventories = (List<InventoryBackup>) Optional.ofNullable(((List<InventoryBackup>) PlayerData.from(getPlayer()).getData("inventories")))
						.orElse(new ArrayList<InventoryBackup>());
				
				InventoryBackup inventory = inventories.get(0);
				
				inventories.remove(0);
				
				PlayerData.from(getPlayer()).setData("inventories", inventories);
				
				inventory.saveToFile(); // TODO				
				
			}
			
		}, 20 * 60 * 60).getTaskId();
		
	}

}
