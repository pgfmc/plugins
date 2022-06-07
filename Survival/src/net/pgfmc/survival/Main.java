package net.pgfmc.survival;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.survival.cmd.PvpToggle;
import net.pgfmc.survival.cmd.donator.Craft;
import net.pgfmc.survival.cmd.donator.Echest;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		getCommand("afk").setExecutor(new Afk());
		getCommand("echest").setExecutor(new Echest());
		getCommand("craft").setExecutor(new Craft());
		new PvpToggle();
		
		
		getServer().getPluginManager().registerEvents(new Afk(), this);
		getServer().getPluginManager().registerEvents(new ItemProtect(), this);
		getServer().getPluginManager().registerEvents(new PvPEvent(), this);
	}
	
	@Override
	public void onDisable()
	{
		
	}	
}
