package net.pgfmc.core.playerdataAPI;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.cmd.donator.Nick;
import net.pgfmc.core.permissions.Role;
import net.pgfmc.core.util.Mixins;

/**
 * stores dynamic, temporary and non-temporary data for each player.
 * @author CrimsonDart
 * @since 2.0.0
 * @version 4.0.2
 */
public class PlayerData extends AbstractPlayerData {
	
	// fields
	
	/**
	 * Hashmap to contain all instances of PlayerData, so they can be accesed.
	 */
	private static Set<PlayerData> instances = new HashSet<PlayerData>();
	private static Set<PlayerData> debug = new HashSet<PlayerData>();
	private static List<PlayerData> onlinePlayers = new LinkedList<>();
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	protected List<String> queue = new LinkedList<String>();
	
	/**
	 * Creates a new PlayerData for anyone who joins the server for the first time.
	 * @param p Player who joined.
	 */
	PlayerData(OfflinePlayer p) {
		super(p);
		
		PlayerData pd = getPlayerData(p);
		if (pd == null) {
			
			
			for (Consumer<PlayerData> consoomer : PlayerDataManager.pdInit) {
				
				consoomer.accept(this);
			}
			
			instances.add(this);
			System.out.println(p.getName() + " has been loaded!");
		}
	}
	
	// find PlayerData functions ------------------------------------------
	
	/**
	 * Gets a player's associated PlayerData.
	 * @param p The player.
	 * @return The Player's PlayerData class.
	 */
	public static PlayerData getPlayerData(OfflinePlayer p) { // gets a player's playerdata.
		return getPlayerData(p.getUniqueId());
	}
	
	/**
	 * Gets a player's PlayerData class.
	 * @param id The player's UUID.
	 * @return The player's PlayerData.
	 */
	public static PlayerData getPlayerData(UUID id) {
		Objects.requireNonNull(id);
		for (PlayerData uid : instances) {
			if (id.toString().equals(uid.getUniqueId().toString())) {
				return uid;
			}
		}
		return null;
	}
	
	/**
	 * Gets a player's PlayerData class.
	 * @param name The player's Minecraft Username, or Nickname.
	 * @return The player's PlayerData.
	 */
	public static PlayerData getPlayerData(String name) {
		for (PlayerData uid : instances) {
			if (uid.getName().equals(name) || uid.getNicknameRaw().equals(name)) {
				return uid;
			}
		}
		return null;
	}
	
	public static PlayerData getPlayerDataById(String discordUserId)
	{
		for (PlayerData uid : instances)
		{
			if (discordUserId.equals(uid.getData("Discord"))) return uid;
		}
		return null;
		
	}
	
	// getters and setters
	
	public String getRankedName()
	{
		Nick.removeImpostors(this);
		
		Role role = Role.getDominant(getData("Roles"));
		String name = getName();
		
		// If role is Donator or higher
		if (role.getDominance() >= Role.VETERAN.getDominance())
		{
			return role.getColorCode() + (String) Optional.ofNullable(getData("nick")).orElse(name);
		}
		return role.getColorCode() + name;
	}
	
	public String getNicknameRaw()
	{
		return Nick.removeCodes(getRankedName());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof OfflinePlayer) {
			return (((OfflinePlayer) o).getUniqueId().toString().equals(getUniqueId().toString()));
		} else if (o instanceof PlayerData) {
			return (((PlayerData) o).getUniqueId().toString().equals(getUniqueId().toString()));
		}
		return false;
	}
	
	public boolean equals(PlayerData o) {
		return o.getUniqueId().toString().equals(getUniqueId().toString());
	}
	
	
	
	
	
	/**
	 * Gets the Player's Role prefix.
	 * @return The player's role prefix.
	 */
	public String getRankColor() {
		return Role.getDominant(getData("Roles")).getColorCode();
	}
	
	public void setDebug(boolean d) {
		if (d) {
			debug.add(this);
		} else {
			debug.remove(this);
		}
	}
	
	public boolean isDebug() {
		return (debug.contains(this));
	}
	
	public void toggleDebug() {
		setDebug(!isDebug());
	}
	
	public static void sendDebug(String message) {
		for (PlayerData pd : debug) {
			pd.sendMessage(message);
		}
	}
	
	/**
	 * Sets a player's data, and saves it in RAM.
	 * Returns a Queueable, which can be <.queue>ed to save to file.
	 * @param p The player
	 * @param n Name of the data; also the name used to call the data
	 * @param d The data itself
	 */
	public static Queueable setData(OfflinePlayer p, String n, Object d) { // static function that passes to non-static setData
		if (p == null) {
			return null;
		}
		return getPlayerData(p).setData(n, d);
	}
	
	/**
	 * Sets a player's data, and saves it in RAM.
	 * Returns a Queueable, which can be <.queue>ed to save to file.
	 * @param n Name of the data; also the name used to call the data
	 * @param d The data itself
	 */
	public Queueable setData(String n, Object d) { // sets a playerData point
		data.put(n, d);
		return new Queueable(n);
	}
	
	/**
	 * Queueable class
	 * 
	 * @author CrimsonDart
	 *
	 */
	public class Queueable {
		
		private String data;
		
		Queueable(String n) {
			data = n;
		}
		
		/**
		 * adds this data to the queue
		 */
		public void queue() {
			queue.add(data);
		}
		
		public void save() {
			saveToFile(data, getData(data));
		}
	}
	
	/**
	 * adds a data point to the queue.
	 * @param data The data point to be added to the queue.
	 * @return wether or not the addition was successful.
	 */
	public boolean addQ(String data) {
		if (data.contains(data)) {
			queue.add(data);
			return true;
		}
		return false;
	}
	
	/**
	 * removes a data point from the queue.
	 * @param data The data point to remove.
	 * @return Wether or not the removal was successful.
	 */
	public boolean removeQ(String data) {
		return queue.remove(data);
	}
	
	/**
	 * Gets a player's data. must be Cast to the Correct Class to be used, however.
	 * @param p The player
	 * @param n Name of the data.
	 * @return The data called by "n". Must be Cast to be used.
	 */
	public static <T> T getData(OfflinePlayer p, String n) { // static function that passes to non-static getData
		return getPlayerData(p).getData(n);
	}
	
	/**
	 * Gets a player's data. must be Cast to the Correct Class to be used, however.
	 * @param n Name of the data.
	 * @return The data called by "n". Must be Cast to be used.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData(String n) { // gets a playerData point
		return (T) data.get(n);
	}
	
	/**
	 * @author bk
	 * @return a player's associated FileConfiguration from database.yml.
	 */
	public Object loadFromFile(String path) {
		return Mixins.getDatabase(CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml").get(path);
	}
	
	public FileConfiguration loadFile() {
		return Mixins.getDatabase(CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml");
	}
	
	/**
	 * Saves A Player's data.
	 * @author bk
	 * @param path Name of the data saved.
	 * @param payload The data saved.
	 */
	public void saveToFile(String path, Object payload) {

		FileConfiguration database = Mixins.getDatabase(CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml");
		database.set(path, payload);
		//queue.forEach((s, o) -> database.set(s, o));
		//queue.clear();
		System.out.println("Queue saved to system!");
		
		Mixins.saveDatabase(database, CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml");
	}
	
	/**
	 * switches this player to online;
	 */
	protected void setOnline(Player p) {
		online = p;
		onlinePlayers.add(this);
	}
	
	/**
	 * turns this player offline :(
	 */
	protected void setOffline() {
		online = null;
		onlinePlayers.remove(this);
	}
	
	/**
	 * Returns a set containing all PlayerData.
	 * @return A set containing all PlayerData.
	 */
	public static Set<PlayerData> getPlayerDataSet() {
		return instances;
	}
	
	/**
	 * Returns the amount of PlayerDatas currently loaded.
	 * @return the amount of playerdatas.
	 */
	public static int size() {
		return instances.size();
	}
	
	/**
	 * Returns a stream of each PlayerData.
	 */
	public static Stream<PlayerData> stream() {
		return StreamSupport.stream(getPlayerDataSet().spliterator(), false);
	}
	
	/**
	 * returns all online player's PlayerData.
	 * @return
	 */
	public static List<PlayerData> getOnlinePlayerData() {
		return onlinePlayers;
	}
}