package net.pgfmc.masterbook;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.masterbook.chat.HomeInput;
import net.pgfmc.masterbook.masterbook.FirstJoin;
import net.pgfmc.masterbook.masterbook.HelpCommand;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		getCommand("commands").setExecutor(new HelpCommand());
		
		getServer().getPluginManager().registerEvents(new FirstJoin(), this);
		getServer().getPluginManager().registerEvents(new HomeInput(), this);
	}

}
