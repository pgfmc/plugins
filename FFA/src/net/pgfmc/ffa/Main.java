package net.pgfmc.ffa;

import java.util.Optional;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.playerdataAPI.PlayerDataManager;
import net.pgfmc.ffa.cmd.handler.FFACmdHandler;
import net.pgfmc.ffa.zone.ZoneDetector;
import net.pgfmc.ffa.zone.ZoneInfo;
import net.pgfmc.ffa.zone.zones.Combat;

public class Main extends JavaPlugin implements Listener {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		PlayerDataManager.setInit(pd -> {
			
			FileConfiguration db = pd.loadFile();
			
			double kills = Optional.ofNullable(db.getDouble("kills")).orElse(0.0);
			double deaths = Optional.ofNullable(db.getDouble("deaths")).orElse(0.0);
			
			pd.setData("kills", kills).queue();
			pd.setData("deaths", deaths).queue();
			
		});
		
		getCommand("ffa").setExecutor(new FFACmdHandler());
		getServer().getPluginManager().registerEvents(new ZoneDetector(), this);
		getServer().getPluginManager().registerEvents(new Combat(), this);
		
		new ZoneInfo(); // Needed for Configify to work apparently ?
	}
	
	@EventHandler
	public void onInteractMinecart(VehicleDamageEvent e)
	{
		e.setCancelled(true);
	}

}
