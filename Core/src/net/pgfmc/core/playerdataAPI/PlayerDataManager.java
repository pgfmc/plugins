package net.pgfmc.core.playerdataAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.permissions.Permissions;

public class PlayerDataManager implements Listener {
	
	private static List<Consumer<Void>> postLoad = new ArrayList<>();
	protected static List<Consumer<PlayerData>> pdInit = new ArrayList<>();
	
	/**
	 * Sets an initialization function to be ran when playerData loads.
	 * Should only be used in JavaPlugin.onEnable().
	 * @param consoom The function to load playerdata
	 */
	public static void setInit(Consumer<PlayerData> consoom) {
		pdInit.add(consoom);
		System.out.println("PD Initialization Function added!");
	}
	
	/**
	 * Sets a function to be ran after all PlayerDatas have loaded; should be used to initialize features that require PlayerData support.
	 * Should only be used in JavaPlugin.onEnable().
	 * @param consoom The function to be ran.
	 */
	public static void setPostLoad(Consumer<Void> consoom) {
		postLoad.add(consoom);
		System.out.println("Post PD function Init!");
	}
	
	/**
	 * Initializes PlayerData. 
	 * Creates a new PlayerData for each Player who has ever played, and loads their data, set by PlayerDataManager.setInit(Consumer<PlayerData>).
	 * After PD is initialized, the queue is initialized, and postLoad functions are loaded, set by PlayerDataManager.setPostLoad(Consumer<?>).
	 */
	public static void InitializePD() {
		
		for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			new PlayerData(p);
			
		}
		initializeQ();
		for (Consumer<?> c : postLoad) {
			c.accept(null);
			System.out.println("PD Post methods ran!");
		}
	}
	
	/**
	 * Begins the queue saving loop.
	 */
	private static void initializeQ() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CoreMain.plugin, new Runnable() {
			
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
		}
		System.out.println("Queue has been saved.");
	}
	
	@EventHandler
	public void onJoinEvent(PlayerJoinEvent e) {
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		if (pd == null) {
			pd = new PlayerData(e.getPlayer());
		}
		
		pd.setOnline(e.getPlayer());
		Permissions.recalcPerms(pd);
	}
	
	@EventHandler
	public void onQuitEvent(PlayerQuitEvent e) {
		PlayerData.getPlayerData(e.getPlayer()).setOffline();
	}
}
