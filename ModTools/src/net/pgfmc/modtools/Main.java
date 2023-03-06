package net.pgfmc.modtools;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.cmd.Broadcast;
import net.pgfmc.modtools.cmd.Debug;
import net.pgfmc.modtools.cmd.Gamemode;
import net.pgfmc.modtools.cmd.Heal;
import net.pgfmc.modtools.cmd.Invsee;
import net.pgfmc.modtools.cmd.Sudo;
import net.pgfmc.modtools.cmd.powertool.Powertool;
import net.pgfmc.modtools.cmd.powertool.PowertoolExecutor;
import net.pgfmc.modtools.cmd.toggle.Fly;
import net.pgfmc.modtools.cmd.toggle.God;
import net.pgfmc.modtools.cmd.toggle.Vanish;
import net.pgfmc.modtools.inventory.InventoryBackup;
import net.pgfmc.modtools.inventory.InventoryBackupScheduler;
import net.pgfmc.modtools.inventory.cmd.Inventory;

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
		
		getCommand("inventory").setExecutor(new Inventory());
		
		getCommand("powertool").setExecutor(new Powertool());
		
		getServer().getPluginManager().registerEvents(new Fly(), this);
		getServer().getPluginManager().registerEvents(new God(), this);
		getServer().getPluginManager().registerEvents(new Vanish(), this);
		getServer().getPluginManager().registerEvents(new InventoryBackupScheduler(), this);
		getServer().getPluginManager().registerEvents(new PowertoolExecutor(), this);
		
		
	}
	
	@Override
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTask(InventoryBackupScheduler.INVENTORY_ROLLBACK_TASK_ID);
		
		PlayerData.getPlayerDataSet().stream().forEach(pd -> {
			@SuppressWarnings("unchecked")
			List<InventoryBackup> inventories = (List<InventoryBackup>) Optional.ofNullable(pd.getData("inventories")).orElse(new ArrayList<InventoryBackup>());
			
			inventories.stream().forEach(inventory -> {
				Bukkit.getScheduler().cancelTask(inventory.getTaskId());
				// TODO file save and stuff
				
			});
		});
	}

}
