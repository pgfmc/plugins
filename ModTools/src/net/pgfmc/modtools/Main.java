package net.pgfmc.modtools;

import org.bukkit.plugin.java.JavaPlugin;

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

		
		getCommand("powertool").setExecutor(new Powertool());
		
		getServer().getPluginManager().registerEvents(new Fly(), this);
		getServer().getPluginManager().registerEvents(new God(), this);
		getServer().getPluginManager().registerEvents(new Vanish(), this);
		
		getServer().getPluginManager().registerEvents(new PowertoolExecutor(), this);
		
	}
	
	@Override
	public void onDisable() {}

}
