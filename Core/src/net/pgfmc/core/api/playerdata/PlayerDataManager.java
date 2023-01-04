package net.pgfmc.core.api.playerdata;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.util.files.Configify;

public class PlayerDataManager extends Configify implements Listener {
	
	private static int task = -1;

	private static List<Consumer<Void>> postLoad = new ArrayList<>();
	protected static List<Consumer<PlayerData>> pdInit = new ArrayList<>();
	
	/**
	 * Sets an initialization function to be ran when playerData loads.
	 * Should only be used in JavaPlugin.onEnable().
	 * @param consoom The function to load playerdata
	 */
	public static void setInit(Consumer<PlayerData> consoom) {
		pdInit.add(consoom);
		Bukkit.getLogger().warning("PD Initialization Function added!");
	}
	
	/**
	 * Sets a function to be ran after all PlayerDatas have loaded; should be used to initialize features that require PlayerData support.
	 * Should only be used in JavaPlugin.onEnable().
	 * @param consoom The function to be ran.
	 */
	public static void setPostLoad(Consumer<Void> consoom) {
		postLoad.add(consoom);
		Bukkit.getLogger().warning("Post PD function Init!");
	}
	
	/**
	 * Initializes PlayerData. 
	 * Creates a new PlayerData for each Player who has ever played, and loads their data, set by PlayerDataManager.setInit(Consumer<PlayerData>).
	 * After PD is initialized, the queue is initialized, and postLoad functions are loaded, set by PlayerDataManager.setPostLoad(Consumer<?>).
	 */
	public static void initializePD() {
		for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			PlayerData pd = new PlayerData(p);
			
			ConfigurationSection cfs = pd.loadFile();
			
			for (String s : cfs.getStringList("tags")) {
				pd.addTag(s);
			}
		}
		
		initializeQ();
		for (Consumer<?> c : postLoad) {
			c.accept(null);
			Bukkit.getLogger().warning("PD Post methods ran!");
		}
	}
	
	
	/**
	 * Begins the queue saving loop.
	 */
	private static void initializeQ() {
		task = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CoreMain.plugin, new Runnable() {
			
			@Override
			public void run() {
				saveQ();
			}
			
		}, 20 * 60);
	}
	
	/**
	 * Saves all queued values to file.
	 */
	public static void saveQ() {
		for (PlayerData pd : PlayerData.getPlayerDataSet()) {
			for (String key : pd.queue) {
				pd.saveToFile(key, pd.getData(key));
			}
			pd.queue.clear();
			
			List<String> list = new LinkedList<>();
			for (String s : pd.getTags()) {
				list.add(s);
			}
			
			pd.saveToFile("tags", list);
		}
		
		Bukkit.getLogger().info("Queue has been saved.");
	}
	
	@EventHandler
	public void onJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.from(p);
		
		if (pd == null) {
			pd = new PlayerData(p);
		}
		
		// p.setDisplayName(pd.getRankedName());
		p.setPlayerListName(pd.getRankedName());
		p.setCustomName(pd.getRankedName());
		p.setCustomNameVisible(true);
		
	}

	@Override
	public void reload() {
		saveQ();
		if (task == -1) return;
		Bukkit.getServer().getScheduler().cancelTask(task);
		initializeQ();
	}
	
	
	@Override
	public void enable() {}

	@Override
	public void disable() {}
}
