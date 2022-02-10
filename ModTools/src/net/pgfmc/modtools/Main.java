package net.pgfmc.modtools;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.playerdataAPI.PlayerDataManager;
import net.pgfmc.modtools.fake.FakeJoin;
import net.pgfmc.modtools.fake.FakeLeave;
import net.pgfmc.modtools.mute.Mute;
import net.pgfmc.modtools.mute.Unmute;
import net.pgfmc.modtools.rollback.PlayerInventorySaver;
import net.pgfmc.modtools.rollback.commands.Rollback;
import net.pgfmc.modtools.tag.AddTag;
import net.pgfmc.modtools.tag.HasTag;
import net.pgfmc.modtools.toggle.DimToggle;
import net.pgfmc.modtools.toggle.Fly;
import net.pgfmc.modtools.toggle.God;
import net.pgfmc.modtools.toggle.Vanish;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		PlayerDataManager.setInit(pd -> new PlayerInventorySaver(pd));
		
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
		
		getCommand("rollback").setExecutor(new Rollback());
		getCommand("rollback").setExecutor(new Rollback());
		
		getServer().getPluginManager().registerEvents(new Fly(), this);
		getServer().getPluginManager().registerEvents(new God(), this);
		getServer().getPluginManager().registerEvents(new Vanish(), this);
		
	}

}
