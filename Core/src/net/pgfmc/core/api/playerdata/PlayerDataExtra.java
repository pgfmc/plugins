package net.pgfmc.core.api.playerdata;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.luckperms.api.node.matcher.NodeMatcher;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.util.roles.PGFRole;

/**
 * Exists literally only so there is less code in PlayerData.
 * @author CrimsonDart
 *
 */
abstract class PlayerDataExtra {
	
	protected OfflinePlayer player;
	
	protected PlayerDataExtra(OfflinePlayer p) {
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
		return player.getPlayer(); // Can be null
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
		if (getPlayer() == null) return;
		
		getPlayer().sendMessage(message);
	}
	
	public boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}
	
	public boolean teleport(Entity o) {
		if (!isOnline()) return false;
		
		return getPlayer().teleport(o);
	}
	
	public boolean teleport(Location o) {
		if (!isOnline()) return false;
		
		return getPlayer().teleport(o);
	}
	
	public void teleport(PlayerData o) {
		if (!isOnline()) return;
		
		getPlayer().teleport(o.getPlayer());
	}
	
	public void playSound(Location location, Sound sound, float volume, float pitch) {
		if (!isOnline()) return;
		
		getPlayer().playSound(location, sound, volume, pitch);
	}
	
	public void playSound(Sound sound) {
		if (!isOnline()) return;
		
		getPlayer().playSound(getPlayer().getLocation(), sound, 1f, 1f);
	}
	
	public boolean hasPermission(String permission) {
		
		if (getPlayer() != null) return getPlayer().hasPermission(permission);
		
		try {
			List<String> nodes = new ArrayList<String>(CoreMain.luckPermsAPI.getGroupManager().searchAll(NodeMatcher.key(permission)).get().keySet());
			
			if (nodes == null || nodes.isEmpty()) throw new NullPointerException("Permission does not exist in LuckPerms for the OfflinePlayer.");
			
			nodes.forEach(node -> Bukkit.getLogger().warning("Node found: " + node));
			
			List<PGFRole> roles = nodes.stream().map(node -> PGFRole.get(node)).collect(Collectors.toList());
			
			PGFRole parentRole = roles.stream().sorted((r1, r2) -> r1.compareTo(r2))
						  .collect(Collectors.toList())
						  .get(roles.size() - 1); // Lowest role with the permission
			
			Bukkit.getLogger().warning("Node to role: " + parentRole.getName());
			Bukkit.getLogger().warning("OfflinePlayer's role: " + PlayerData.from(getOfflinePlayer()).getRole().getName());
			Bukkit.getLogger().warning("Comparison: " + PlayerData.from(getOfflinePlayer()).getRole().compareTo(parentRole));
			
			return (PlayerData.from(getOfflinePlayer()).getRole().compareTo(parentRole) <= 0); // If the OfflinePlayer's role is equal or above the parent role
			
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		throw new NullPointerException("Permission does not exist in LuckPerms for the OfflinePlayer.");
		
	}
	
	public boolean hasPermission(Permission permission) {
		return hasPermission(permission.getName());
	}
	
}
