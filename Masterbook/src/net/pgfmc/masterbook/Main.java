package net.pgfmc.masterbook;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.masterbook.chat.NickHomeInput;
import net.pgfmc.masterbook.masterbook.BookClick;
import net.pgfmc.masterbook.masterbook.HelpCommand;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		new HelpCommand("commands");
		new GiveLodestoneCommand("getClaim");
		
		getServer().getPluginManager().registerEvents(new NickHomeInput(), this);
		getServer().getPluginManager().registerEvents(new BookClick(), this);
	}
}
