package net.pgfmc.core.cmd.serverselector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
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
		
		if (CoreMain.getThisServerName() == null || !CoreMain.getThisServerName().contains("test")) // if the main proxy
		{
			/**
			 * The Survival server
			 */
			if (playerdata.hasPermission("net.pgfmc.proxycore.connect.survival"))
			{
				if (CoreMain.getThisServerName() != null && CoreMain.getThisServerName().equals("survival")) // Connected
				{
					setItem(i, Material.GRASS_BLOCK)
					.n(ChatColor.GREEN + blackStarIcon + " Survival (Connected)")
					.l(Arrays.asList(ChatColor.GRAY + "You are connected to Survival.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Claims, homes, tpa, and more."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.RED + "You are already connected to Survival.");
						
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
					});
					
				} else if (servers.get("survival") == null || servers.get("survival") == true) // Online
				{
					setItem(i, Material.GRASS_BLOCK)
					.n(ChatColor.GREEN + "Survival")
					.l(Arrays.asList(ChatColor.GRAY + "Click to join Survival.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Claims, homes, tpa, and more."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.GREEN + "Attempting to connect to Survival.");
						
						CoreMain.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to Survival.");
						
						PluginMessageType.CONNECT.send(player, "survival");
						
						playerdata.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
						
					});
					
				} else // Offline
				{
					setItem(i, Material.RED_STAINED_GLASS)
					.n(ChatColor.DARK_RED + "Survival - Offline")
					.l(Arrays.asList(ChatColor.GRAY + "Click to join Survival.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Claims, homes, tpa, and more."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.RED + "Survival is offline.");
						
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
					});
					
				}
				
				servers.remove("survival");
				i--;
				
			}
			
			/**
			 * The Past seasons server
			 */
			if (playerdata.hasPermission("net.pgfmc.proxycore.connect.past"))
			{
				if (CoreMain.getThisServerName() != null && CoreMain.getThisServerName().equals("past")) // Connected
				{
					setItem(i, Material.DIRT_PATH)
					.n(ChatColor.GREEN + blackStarIcon + " Past Seasons (Connected)")
					.l(Arrays.asList(ChatColor.GRAY + "You are connected to Past Seasons.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Celebrate PGF's 5th anniversary",
							 blackRightPointingPointerIcon + " " +"by exploring past seasons."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.RED + "You are already connected to Survival.");
						
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
					});
					
				} else if (servers.get("past") == null || servers.get("past") == true) // Online
				{
					setItem(i, Material.DIRT_PATH)
					.n(ChatColor.GREEN + "Past Seasons")
					.l(Arrays.asList(ChatColor.GRAY + "Click to join Past Seasons.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Celebrate PGF's 5th anniversary",
							 blackRightPointingPointerIcon + " " +"by exploring past seasons."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.GREEN + "Attempting to connect to Past Seasons.");
						
						CoreMain.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to Past Seasons.");
						
						PluginMessageType.CONNECT.send(player, "past");
						
						playerdata.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
						
					});
					
				} else // Offline
				{
					setItem(i, Material.RED_STAINED_GLASS)
					.n(ChatColor.DARK_RED + "Past Seasons - Offline")
					.l(Arrays.asList(ChatColor.GRAY + "Click to join Past Seasons.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Celebrate PGF's 5th anniversary",
							 blackRightPointingPointerIcon + " " + "by exploring past seasons."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.RED + "Past Seasons is offline.");
						
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
					});
					
				}
				
				servers.remove("past");
				i--;
				
			}
			
		} else // test server
		{
			/**
			 * The Test1 server
			 * 
			 * Staff only
			 */
			if (playerdata.hasPermission("net.pgfmc.proxycore.connect.test1"))
			{
				if (CoreMain.getThisServerName() != null && CoreMain.getThisServerName().equals("test1")) // Connected
				{
					setItem(i, Material.COMMAND_BLOCK)
					.n(ChatColor.GREEN + blackStarIcon + " Test1 (Connected)")
					.l(Arrays.asList(ChatColor.GRAY + "You are connected to Test1.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Test1 server for developers."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.RED + "You are already connected to Test1.");
						
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
					});
					
				} else if (servers.get("test1") == null || servers.get("test1") == true) // Online
				{
					setItem(i, Material.COMMAND_BLOCK)
					.n(ChatColor.GREEN + "Test1")
					.l(Arrays.asList(ChatColor.GRAY + "Click to join Test1.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Test1 server for developers."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.GREEN + "Attempting to connect to Test1.");
						
						CoreMain.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to Test1.");
						
						PluginMessageType.CONNECT.send(player, "test1");
						
						playerdata.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
						
					});
					
				} else // Offline
				{
					setItem(i, Material.RED_STAINED_GLASS)
					.n(ChatColor.DARK_RED + "Test1 - Offline")
					.l(Arrays.asList(ChatColor.GRAY + "Click to join Test1.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Test1 server for developers."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.RED + "Test1 is offline.");
						
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
					});
					
				}
				
				servers.remove("test1");
				i--;
				
			}
			
			/**
			 * The Test2 server
			 * 
			 * Staff only
			 */
			if (playerdata.hasPermission("net.pgfmc.proxycore.connect.test2"))
			{
				if (CoreMain.getThisServerName() != null && CoreMain.getThisServerName().equals("test2")) // Connected
				{
					setItem(i, Material.COMMAND_BLOCK)
					.n(ChatColor.GREEN + blackStarIcon + " Test2 (Connected)")
					.l(Arrays.asList(ChatColor.GRAY + "You are connected to Test2.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Test2 server for developers."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.RED + "You are already connected to Test2.");
						
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
					});
					
				} else if (servers.get("test2") == null || servers.get("test2") == true) // Online
				{
					setItem(i, Material.COMMAND_BLOCK)
					.n(ChatColor.GREEN + "Test2")
					.l(Arrays.asList(ChatColor.GRAY + "Click to join Test2.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Test2 server for developers."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.GREEN + "Attempting to connect to Test2.");
						
						CoreMain.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to Test2.");
						
						PluginMessageType.CONNECT.send(player, "test2");
						
						playerdata.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
						
					});
					
				} else // Offline
				{
					setItem(i, Material.RED_STAINED_GLASS)
					.n(ChatColor.DARK_RED + "Test2 - Offline")
					.l(Arrays.asList(ChatColor.GRAY + "Click to join Test2.",
							ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Test2 server for developers."));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(ChatColor.RED + "Test2 is offline.");
						
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
					});
					
				}
				
				servers.remove("test2");
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
					
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					
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
					
					playerdata.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
					
				});
				
			} else // Offline
			{
				setItem(i, Material.GRAVEL)
				.n(ChatColor.DARK_RED + serverNamePronounified + " - Offline")
				.l(ChatColor.GRAY + "Click to join " + serverNamePronounified + ".");
				
				setAction(i, (player, event) -> {
					if (playerdata.getPlayer() == null) return;
					
					player.sendMessage(ChatColor.RED + serverNamePronounified + " is offline.");
					
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					
				});
				
			}
			
			i--;
			
		}
		
	}

}
