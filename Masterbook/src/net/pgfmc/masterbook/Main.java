package net.pgfmc.masterbook;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.survival.cmd.GiveLodestoneCommand;
import net.pgfmc.survival.inventories.masterbook.BookClick;
import net.pgfmc.survival.inventories.masterbook.HelpCommand;
import net.pgfmc.survival.inventories.masterbook.NickHomeInput;

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
