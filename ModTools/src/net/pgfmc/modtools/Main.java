package net.pgfmc.modtools;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.modtools.tools.Broadcast;
import net.pgfmc.modtools.tools.Day;
import net.pgfmc.modtools.tools.Debug;
import net.pgfmc.modtools.tools.DimToggle;
import net.pgfmc.modtools.tools.Fly;
import net.pgfmc.modtools.tools.Gamemode;
import net.pgfmc.modtools.tools.God;
import net.pgfmc.modtools.tools.Heal;
import net.pgfmc.modtools.tools.Invsee;
import net.pgfmc.modtools.tools.Sudo;
import net.pgfmc.modtools.tools.Vanish;
import net.pgfmc.modtools.tools.fake.FakeJoin;
import net.pgfmc.modtools.tools.fake.FakeLeave;
import net.pgfmc.modtools.tools.mute.Mute;
import net.pgfmc.modtools.tools.mute.Unmute;
import net.pgfmc.modtools.tools.tag.AddTag;
import net.pgfmc.modtools.tools.tag.HasTag;

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
		
		getCommand("day").setExecutor(new Day());
		
		getCommand("dimtoggle").setExecutor(new DimToggle());
		
		getCommand("invsee").setExecutor(new Invsee());
		
		getCommand("mute").setExecutor(new Mute());
		getCommand("unmute").setExecutor(new Unmute());
		
		getCommand("tag").setExecutor(new AddTag());
		getCommand("hastag").setExecutor(new HasTag());
		
		getCommand("broadcast").setExecutor(new Broadcast());
		
		getCommand("fakeleave").setExecutor(new FakeLeave());
		getCommand("fakejoin").setExecutor(new FakeJoin());
		
		getServer().getPluginManager().registerEvents(new Fly(), this);
		getServer().getPluginManager().registerEvents(new God(), this);
		getServer().getPluginManager().registerEvents(new Vanish(), this);
		
	}

}
