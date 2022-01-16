package net.pgfmc.backup;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.backup.backup.Backup;
import net.pgfmc.backup.backup.Backupconfirm;
import net.pgfmc.backup.backup.Restore;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		getCommand("backup").setExecutor(new Backup());
		getCommand("backupconfirm").setExecutor(new Backupconfirm());
		getCommand("restore").setExecutor(new Restore());
		
		new Restart().init(); // Starts auto restart
	}
	
	@Override
	public void onDisable()
	{
		
	}

}
