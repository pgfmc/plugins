package net.pgfmc.hardcore;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.util.files.Mixins;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		// Creates rewards.yml for the player rewards
		// If it doesn't exist
		Mixins.getDatabase(getDataFolder() + File.separator + "advancement_gifts.yml");
	}
	
	@Override
	public void onDisable()
	{
	}
}
