package net.pgfmc.survival.masterbook.staff.inventorybackups.noninv;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.Main;

public class InventoryBackup {
	
	private String uuid;
	private ItemStack[] inventoryContents;
	private float exp;
	
	private int taskId;
	private InventoryBackupCause cause;
	
	private final Date timeOf = new Date();
	
	@SuppressWarnings("unchecked")
	public InventoryBackup(PlayerData pd, InventoryBackupCause cause)
	{
		if (pd.getPlayer() == null) return;
		
		Player p = pd.getPlayer();
		
		this.uuid = p.getUniqueId().toString();
		this.inventoryContents = p.getInventory().getContents();
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
	
	public String getPlayerUUID()
	{
		return uuid;
	}
	
	public OfflinePlayer getOfflinePlayer()
	{
		return Bukkit.getOfflinePlayer(UUID.fromString(uuid));
	}
	
	public ItemStack[] getInventoryContents()
	{
		return inventoryContents;
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
	
	public boolean restore()
	{
		
		if (!getOfflinePlayer().isOnline()) return false;
		
		Player p = getOfflinePlayer().getPlayer();
		
		new InventoryBackup(PlayerData.from(p), InventoryBackupCause.ROLLBACK);
		
		p.getInventory().setContents(getInventoryContents());
		p.setExp(exp);
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public int setFutureDelete()
	{
		return Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {

			@Override
			public void run() {
				List<InventoryBackup> inventories = (List<InventoryBackup>) Optional.ofNullable(((List<InventoryBackup>) PlayerData.from(getOfflinePlayer()).getData("inventories")))
						.orElse(new ArrayList<InventoryBackup>());
				
				inventories.remove(0);
				
				PlayerData.from(getOfflinePlayer()).setData("inventories", inventories);
				
			}
			
		}, 20 * 60 * 60).getTaskId();
		
	}

}
