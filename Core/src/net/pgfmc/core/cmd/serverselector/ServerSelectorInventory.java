package net.pgfmc.core.cmd.serverselector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.SoundEffect;
import net.pgfmc.core.util.proxy.PluginMessageType;

/**
 * This is the server selector inventory for ConnectCommand.
 * 
 * The inventory contains available servers to the player.
 * 
 * For a server to display in the inventory, the player needs permission
 * to connect to it.
 * 
 * ---------------------------
 * 
 * [Connect]
 * 
 * The Connect subchannel is used for connecting this player to the
 * specified server.
 * 
 * PLUGIN MESSAGE FORM (pgf:CoreMain): Connect, <server name>
 */
public final class ServerSelectorInventory extends BaseInventory {

	public ServerSelectorInventory(final PlayerData playerdata) {
		super(InventoryType.HOPPER, "Server Selector");
		
		/**
		 * The hopper is filled in right to left.
		 *
		 * This integer is used to keep track of where
		 * to place the next button.
		 */
		int i = 4;
		
		/**
		 * Registered servers. May be online or offline.
		 * 
		 * Any server not handled explicitly will have a default itemstack icon.
		 * 
		 * The proxy can only communicate with a server if there is a player on that server using the proxy.
		 * Because of this, the Registered Servers Map will be EMPTY/NOT UPDATED if there are no players
		 * on the server until after a player joins the server.
		 * 
		 * It takes about 10 seconds to update registered servers after a player joins. Explicitly
		 * defined servers (below) will appear online in the server selector until the proxy is
		 * able to check if they are online/offline for real.
		 * 
		 * In short,,, without the null check for defined servers below, the server selector would be blank
		 * for a few seconds.
		 */
		final Map<String, Boolean> servers = new HashMap<>(CoreMain.getRegisteredServersMap()); // gets a copy of the map
		
		final String blackRightPointingPointerIcon = new String(Character.toChars(0x25BA));
		final String blackStarIcon = new String(Character.toChars(0x2605));
		
		if (CoreMain.getThisServerName() == null || !CoreMain.getThisServerName().contains("est")) // if not the test server
		{
			/**
			 * The Survival server
			 */
			if (playerdata.hasPermission("net.pgfmc.proxycore.connect.survival"))
			{
				if (CoreMain.getThisServerName() != null && CoreMain.getThisServerName().equals("season13")) // Connected
				{
					setItem(i, Material.GRASS_BLOCK)
					.n(ChatColor.GREEN + blackStarIcon + " Season13 (Connected)")
					.l(Arrays.asList(ChatColor.GRAY + "You are connected to Season13.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Claims, homes, tpa, and more."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.RED + "You are already connected to Season13.");
						
						SoundEffect.ERROR.play(playerdata);
						
					});
					
				} else if (servers.get("season13") == null || servers.get("season13") == true) // Online
				{
					setItem(i, Material.GRASS_BLOCK)
					.n(ChatColor.GREEN + "Season13")
					.l(Arrays.asList(ChatColor.GRAY + "Click to join Season13.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Claims, homes, tpa, and more."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.GREEN + "Attempting to connect to Season13.");
						
						CoreMain.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to Season13.");
						
						PluginMessageType.CONNECT.send(player, "season13");
						
						SoundEffect.WORKING.play(playerdata);
						
					});
					
				} else // Offline
				{
					setItem(i, Material.RED_STAINED_GLASS)
					.n(ChatColor.DARK_RED + "Season13 - Offline")
					.l(Arrays.asList(ChatColor.GRAY + "Click to join Season13.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Claims, homes, tpa, and more."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.RED + "Season13 is offline.");
						
						SoundEffect.ERROR.play(playerdata);
						
					});
					
				}
				
				servers.remove("season13");
				i--;
				
			}
			
		}
		
		/**
		 * Default itemstack icons / catch case
		 */
		for (final String server : servers.keySet())
		{
			if (i == 1) break; // max 3 servers
			if (!playerdata.hasPermission("net.pgfmc.proxycore.connect." + server)) continue;
			
			final String serverNamePronounified = server.substring(0, 1).toUpperCase() + server.substring(1);
			
			if (CoreMain.getThisServerName() != null && CoreMain.getThisServerName().equals(server)) // Connected
			{
				setItem(i, Material.SAND)
				.n(ChatColor.GREEN + blackStarIcon + " " + serverNamePronounified + " (Connected)")
				.l(ChatColor.GRAY + "You are connected to " + serverNamePronounified + ".");
				
				setAction(i, (player, event) -> {
					if (playerdata.getPlayer() == null) return;
					
					player.sendMessage(ChatColor.RED + "You are already connected to " + serverNamePronounified + ".");
					
					SoundEffect.ERROR.play(playerdata);
					
				});
				
			} else if (servers.get(server)) // Online
			{
				setItem(i, Material.SAND)
				.n(ChatColor.GREEN + serverNamePronounified)
				.l(ChatColor.GRAY + "Click to join " + serverNamePronounified + ".");
				
				setAction(i, (player, event) -> {
					if (playerdata.getPlayer() == null) return;
					
					player.sendMessage(ChatColor.GREEN + "Attempting to connect to " + serverNamePronounified + ".");
					
					CoreMain.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to " + serverNamePronounified + ".");
					
					PluginMessageType.CONNECT.send(player, server);
					
					SoundEffect.WORKING.play(playerdata);
					
				});
				
			} else // Offline
			{
				setItem(i, Material.GRAVEL)
				.n(ChatColor.DARK_RED + serverNamePronounified + " - Offline")
				.l(ChatColor.GRAY + "Click to join " + serverNamePronounified + ".");
				
				setAction(i, (player, event) -> {
					if (playerdata.getPlayer() == null) return;
					
					player.sendMessage(ChatColor.RED + serverNamePronounified + " is offline.");
					
					SoundEffect.ERROR.play(playerdata);
					
				});
				
			}
			
			i--;
			
		}
		
	}

}
