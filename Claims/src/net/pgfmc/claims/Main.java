package net.pgfmc.claims;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.claims.ownable.OwnableFile;
import net.pgfmc.claims.ownable.block.events.BBEvent;
import net.pgfmc.claims.ownable.block.events.BExEvent;
import net.pgfmc.claims.ownable.block.events.BPE;
import net.pgfmc.claims.ownable.block.events.BlockInteractEvent;
import net.pgfmc.claims.ownable.entities.TameEvent;
import net.pgfmc.claims.ownable.inspector.ClaimTPCommand;
import net.pgfmc.claims.ownable.inspector.EditOwnableCommand;
import net.pgfmc.claims.ownable.inspector.InspectCommand;
import net.pgfmc.core.file.Mixins;
import net.pgfmc.core.playerdataAPI.PlayerDataManager;

public class Main extends JavaPlugin {
	
	// all relevant file paths.
	public static final String BlockContainersPath = "plugins" + File.separator + "PGF-Claims" + File.separator + "BlockContainers.yml";
	
	public static Main plugin;
	
	@Override
	public void onEnable() {
		
		plugin = this;
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		
		// loads files.
		Mixins.getFile(BlockContainersPath);
		
		// new EscapeCommand();
		
		PlayerDataManager.setPostLoad((x) -> OwnableFile.loadContainers());
		
		// initializes all Event Listeners and Command Executors.
		getServer().getPluginManager().registerEvents(new BlockInteractEvent(), this);
		getServer().getPluginManager().registerEvents(new BBEvent(), this);
		getServer().getPluginManager().registerEvents(new BPE(), this);
		getServer().getPluginManager().registerEvents(new TameEvent(), this);
		getServer().getPluginManager().registerEvents(new BExEvent(), this);
		
		
		new InspectCommand("inspector");
		new EditOwnableCommand("editownable");
		new ClaimTPCommand("claimtp");
		
		plugin.getLogger().info("Claims Loaded!");
		
		new ActionBarStuff().runTaskTimer(this, 100, 3);
	}
	
	@Override
	public void onDisable() {
		OwnableFile.saveContainers();
	}
	
	public static JavaPlugin getPlugin() {
		return plugin;
	}
}
