package net.pgfmc.masterbook;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.masterbook.chat.HomeInput;
import net.pgfmc.masterbook.chat.NicknameInput;
import net.pgfmc.masterbook.masterbook.HelpCommand;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		new HelpCommand("commands");
		
		getServer().getPluginManager().registerEvents(new HomeInput(), this);
		getServer().getPluginManager().registerEvents(new NicknameInput(), this);
	}
}
