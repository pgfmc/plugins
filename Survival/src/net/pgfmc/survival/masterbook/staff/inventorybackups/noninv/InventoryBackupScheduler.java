package net.pgfmc.survival.masterbook.staff.inventorybackups.noninv;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.Main;

public class InventoryBackupScheduler implements Listener {
	
	public static int INVENTORY_ROLLBACK_TASK_ID;
	public static DateFormat INVENTORY_DATE_FORMAT = new SimpleDateFormat("MMM dd, YYYY @ kkmm");
	
	public InventoryBackupScheduler()
	{
		INVENTORY_ROLLBACK_TASK_ID = Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable() {
			@Override
			public void run() {
				PlayerData.getPlayerDataSet(pd -> pd.isOnline()).stream()
													.forEach(pd -> new InventoryBackup(pd, InventoryBackupCause.SCHEDULED));
			}
		}, 20 * 60, 20 * 60 * 5).getTaskId(); // Start after 1 minute, repeat every 5 minutes
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		Player p = e.getEntity();
		
		new InventoryBackup(PlayerData.from(p), InventoryBackupCause.DEATH);
		
	}

}
