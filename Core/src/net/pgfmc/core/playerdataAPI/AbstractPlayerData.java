package net.pgfmc.core.playerdataAPI;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class AbstractPlayerData {
	
	protected OfflinePlayer player;
	protected Player online;
	
	protected AbstractPlayerData(OfflinePlayer p) {
		player = p;
	}
	
	/**
	 * Returns the player, cast to OfflinePlayer.
	 * @return the player, cast to OfflinePlayer.
	 * 
	 */
	public OfflinePlayer getOfflinePlayer() {
		return player;
	}
	
	public Player getPlayer() {
		if (player.isOnline()) {
			return player.getPlayer();
		}
		return null;
	}
	
	/**
	 * Returns the Player's UUID.
	 * @return the Player's UUID.
	 */
	public UUID getUniqueId() {
		return player.getUniqueId();
	}
	
	
	/**
	 * Returns the player's name.
	 * @return
	 */
	public String getName() {
		return getOfflinePlayer().getName();
	}
	
	public void sendMessage(String message) {
		if (getOfflinePlayer().getPlayer() != null) {
			getOfflinePlayer().getPlayer().sendMessage(message);
		}
	}
	
	public boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}
	
	public void teleport(Entity o) {
		if (isOnline()) {
			getPlayer().teleport(o);
		}
	}
	
	public void teleport(Location o) {
		if (isOnline()) {
			getPlayer().teleport(o);
		}
	}
	
	public void teleport(PlayerData o) {
		if (isOnline()) {
			getPlayer().teleport(o.getPlayer());
		}
		
	}
	
	public void playSound(Location location, Sound sound, float volume, float pitch) {
		if (isOnline()) {
			getPlayer().playSound(location, sound, volume, pitch);
		}
	}
	
	public void playSound(Sound sound) {
		if (isOnline()) {
			getPlayer().playSound(getPlayer().getLocation(), sound, 1f, 1f);
		}
	}
	
	
	
	
}
