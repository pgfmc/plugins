package net.pgfmc.claims;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.claims.ownable.OwnableFile;
import net.pgfmc.claims.ownable.block.events.BBEvent;
import net.pgfmc.claims.ownable.block.events.BExEvent;
import net.pgfmc.claims.ownable.block.events.BPE;
import net.pgfmc.claims.ownable.block.events.BlockInteractEvent;
import net.pgfmc.claims.ownable.block.events.BucketEvent;
import net.pgfmc.claims.ownable.block.events.EntityEvents;
import net.pgfmc.claims.ownable.block.events.EntityInteractEvent;
import net.pgfmc.claims.ownable.block.events.HarvestEvent;
import net.pgfmc.claims.ownable.block.events.ItemFrameBreak;
import net.pgfmc.claims.ownable.block.events.PhysicsEvent;
import net.pgfmc.claims.ownable.entities.TameEvent;
import net.pgfmc.claims.ownable.inspector.ClaimTPCommand;
import net.pgfmc.claims.ownable.inspector.EditOwnableCommand;
import net.pgfmc.claims.ownable.inspector.InspectCommand;
import net.pgfmc.core.api.playerdata.PlayerDataManager;
import net.pgfmc.core.util.files.Mixins;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable() {
		
		plugin = this;
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		
		// loads files.
		Mixins.getFile(getDataFolder() + File.separator + "BlockContainers.yml");
		
		// new EscapeCommand();
		
		PlayerDataManager.setPostLoad((x) -> OwnableFile.loadContainers());
		
		// initializes all Event Listeners and Command Executors.
		getServer().getPluginManager().registerEvents(new BlockInteractEvent(), this);
		getServer().getPluginManager().registerEvents(new BBEvent(), this);
		getServer().getPluginManager().registerEvents(new BPE(), this);
		getServer().getPluginManager().registerEvents(new TameEvent(), this);
		getServer().getPluginManager().registerEvents(new BExEvent(), this);
		getServer().getPluginManager().registerEvents(new EntityInteractEvent(), this);
		getServer().getPluginManager().registerEvents(new BucketEvent(), this);
		getServer().getPluginManager().registerEvents(new HarvestEvent(), this);
		getServer().getPluginManager().registerEvents(new PhysicsEvent(), this);
		getServer().getPluginManager().registerEvents(new EntityEvents(), this);
		getServer().getPluginManager().registerEvents(new ItemFrameBreak(), this);
		
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
