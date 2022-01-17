package net.pgfmc.backup;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
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
	
	public CoreProtectAPI getCoreProtect() {
        Plugin p = getServer().getPluginManager().getPlugin("CoreProtect");

        // Check that CoreProtect is loaded
        if (p == null || !(p instanceof CoreProtect)) {
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect) p).getAPI();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 7) {
            return null;
        }

        return CoreProtect;
	}

}
