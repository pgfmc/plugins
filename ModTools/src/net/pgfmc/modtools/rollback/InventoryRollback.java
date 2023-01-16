package net.pgfmc.modtools.rollback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.Main;
import net.pgfmc.modtools.rollback.inv.RollbackInventory;

public class InventoryRollback implements Listener {
	
	public static int INVENTORY_ROLLBACK_TASK_ID;
	public static DateFormat INVENTORY_DATE_FORMAT = new SimpleDateFormat("MMM dd, YYYY @ kkmm");
	
	public InventoryRollback()
	{
		INVENTORY_ROLLBACK_TASK_ID = Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable() {
			@Override
			public void run() {
				PlayerData.getPlayerDataSet(pd -> pd.isOnline()).stream()
													.forEach(pd -> new RollbackInventory(pd));
			}
		}, 20 * 60, 20 * 60 * 5).getTaskId(); // Start after 1 minute, repeat every 5 minutes
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		Player p = e.getEntity();
		
		new RollbackInventory(PlayerData.from(p)); // XXX I do not know if the saved inventory
												   // XXX will be before or after death
		
	}
	
	

}
