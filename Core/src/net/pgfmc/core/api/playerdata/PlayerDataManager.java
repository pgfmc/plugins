package net.pgfmc.core.api.playerdata;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.core.CoreMain;

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
		
		Bukkit.getLogger().warning("PlayerData init function created!");
		
	}
	
	/**
	 * Sets a function to be ran after all PlayerDatas have loaded; should be used to initialize features that require PlayerData support.
	 * Should only be used in JavaPlugin.onEnable().
	 * @param consoom The function to be ran.
	 */
	public static void setPostLoad(Consumer<Void> consoom) {
		postLoad.add(consoom);
		
		Bukkit.getLogger().warning("PlayerData post-load function created!");
		
	}
	
	/**
	 * Initializes PlayerData. 
	 * Creates a new PlayerData for each Player who has ever played, and loads their data, set by PlayerDataManager.setInit(Consumer<PlayerData>).
	 * After PD is initialized, the queue is initialized, and postLoad functions are loaded, set by PlayerDataManager.setPostLoad(Consumer<?>).
	 */
	public static void initializePlayerData() {
		
		for (final OfflinePlayer p : Bukkit.getOfflinePlayers())
		{
			new PlayerData(p);
			
		}
		
		Bukkit.getLogger().warning("PlayerData init functions ran!");
		Bukkit.getLogger().warning("PlayerData tags added!");
		
		postLoad.stream().forEach(consoomer -> consoomer.accept(null));
		
		Bukkit.getLogger().warning("PlayerData post-load functions ran!");
		
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
		
		for (PlayerData pd : PlayerData.getPlayerDataSet())
		{
			if (pd.queue.isEmpty()) continue;
			
			pd.queue.stream().forEach(key -> pd.saveToFile(key, pd.getData(key)));
			pd.queue.clear();
			
			Bukkit.getLogger().info("Queue saved for " + pd.getName() + "!");
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST) // Will run first before the rest!
	public void onJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.from(p);
		
		if (pd == null) {
			pd = new PlayerData(p);
		}
		
		CoreMain.updatePlayerNameplate(pd);
		
	}

}
