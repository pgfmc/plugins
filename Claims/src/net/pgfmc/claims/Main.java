package net.pgfmc.claims;

import java.awt.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.pgfmc.claims.ownable.OwnableFile;
import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.events.BBEvent;
import net.pgfmc.claims.ownable.block.events.BExEvent;
import net.pgfmc.claims.ownable.block.events.BPE;
import net.pgfmc.claims.ownable.block.events.BlockInteractEvent;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.claims.ownable.entities.TameEvent;
import net.pgfmc.claims.ownable.inspector.ClaimTPCommand;
import net.pgfmc.claims.ownable.inspector.EditOwnableCommand;
import net.pgfmc.claims.ownable.inspector.InspectCommand;
import net.pgfmc.core.file.Mixins;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.playerdataAPI.PlayerDataManager;
import net.pgfmc.core.util.Vector4;

public class Main extends JavaPlugin {
	
	// all relevant file paths.
	public static final String BlockContainersPath = "plugins\\PGF-Claims\\BlockContainers.yml";
	
	public static Main plugin;
	
	BukkitRunnable task = new BukkitRunnable() {
		
		@Override
		public void run() {
			for (PlayerData pd : PlayerData.getPlayerDataSet(x -> x.isOnline())) {
				
				Player player = pd.getPlayer();
				Block block = player.getTargetBlock(null, 4);
				
				if (block != null && block.getType() == Material.LODESTONE) {
					
					Claim claim = ClaimsTable.getOwnable(new Vector4(block));
					player.sendTitle(" ", "Claimed by " + claim.getPlayer().getRankedName(), 0, 4, 0);
					player.spigot().sendMessage(
							  ChatMessageType.ACTION_BAR,
							  new TextComponent("This message will be in the Action Bar"));
				}
			}
		}
		
	};
	
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
		
		getCommand("inspector").setExecutor(new InspectCommand());
		getCommand("editownable").setExecutor(new EditOwnableCommand());
		getCommand("claimtp").setExecutor(new ClaimTPCommand());
		
		v = Bukkit.getServer().getClass().getPackage().getName();
        v = v.substring(v.lastIndexOf(".") + 1);
		
		plugin.getLogger().info("Claims Loaded!");
		task.runTaskTimer(this, 100, 3);
	}
	
	@Override
	public void onDisable() {
		OwnableFile.saveContainers();
	}
	
	public static JavaPlugin getPlugin() {
		return plugin;
	}
}
