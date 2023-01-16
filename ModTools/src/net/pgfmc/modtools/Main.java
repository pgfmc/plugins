package net.pgfmc.modtools;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.cmd.Broadcast;
import net.pgfmc.modtools.cmd.Debug;
import net.pgfmc.modtools.cmd.Gamemode;
import net.pgfmc.modtools.cmd.Heal;
import net.pgfmc.modtools.cmd.Invsee;
import net.pgfmc.modtools.cmd.Sudo;
import net.pgfmc.modtools.cmd.toggle.Fly;
import net.pgfmc.modtools.cmd.toggle.God;
import net.pgfmc.modtools.cmd.toggle.Vanish;
import net.pgfmc.modtools.rollback.InventoryRollback;
import net.pgfmc.modtools.rollback.cmd.Rollback;
import net.pgfmc.modtools.rollback.inv.RollbackInventory;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		getCommand("gmc").setExecutor(new Gamemode());
		getCommand("gms").setExecutor(new Gamemode());
		getCommand("gma").setExecutor(new Gamemode());
		getCommand("gmsp").setExecutor(new Gamemode());
		
		getCommand("vanish").setExecutor(new Vanish());

		getCommand("fly").setExecutor(new Fly());
		getCommand("god").setExecutor(new God());
		getCommand("sudo").setExecutor(new Sudo());
		getCommand("heal").setExecutor(new Heal());
		
		getCommand("debug").setExecutor(new Debug());
		
		getCommand("invsee").setExecutor(new Invsee());
		
		getCommand("broadcast").setExecutor(new Broadcast());
		
		getCommand("rollback").setExecutor(new Rollback());
		
		getServer().getPluginManager().registerEvents(new Fly(), this);
		getServer().getPluginManager().registerEvents(new God(), this);
		getServer().getPluginManager().registerEvents(new Vanish(), this);
		getServer().getPluginManager().registerEvents(new InventoryRollback(), this);
		
		
	}
	
	@Override
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTask(InventoryRollback.INVENTORY_ROLLBACK_TASK_ID);
		
		PlayerData.getPlayerDataSet().stream().forEach(pd -> {
			List<RollbackInventory> inventories = pd.getData("inventories");
			
			inventories.stream().forEach(inventory -> {
				Bukkit.getScheduler().cancelTask(inventory.getTaskId());
				// TODO file save and stuff
				
			});
		});
	}

}
