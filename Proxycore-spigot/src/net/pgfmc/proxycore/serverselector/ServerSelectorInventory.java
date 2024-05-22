package net.pgfmc.proxycore.serverselector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.proxycore.Main;

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
 * PLUGIN MESSAGE FORM (pgf:main): Connect, <server name>
 */
public class ServerSelectorInventory extends BaseInventory {

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
		final Map<String, Boolean> servers = new HashMap<>(Main.registeredServers);
		
		final String blackRightPointingPointerIcon = new String(Character.toChars(0x25BA));
		final String blackStarIcon = new String(Character.toChars(0x2605));
		
		/**
		 * The Survival server
		 */
		if (playerdata.hasPermission("net.pgfmc.proxycore.connect.survival"))
		{
			if (Main.thisServerName != null && Main.thisServerName.equals("survival")) // Connected
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
					
					Main.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to Survival.");
					
					final ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("Connect");
					out.writeUTF("survival");
					player.sendPluginMessage(Main.plugin, "pgf:main", out.toByteArray());
					
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
		 * The Test server
		 * 
		 * Staff only
		 */
		if (playerdata.hasPermission("net.pgfmc.proxycore.connect.test"))
		{
			if (Main.thisServerName != null && Main.thisServerName.equals("test")) // Connected
			{
				setItem(i, Material.COMMAND_BLOCK)
				.n(ChatColor.GREEN + blackStarIcon + " Test (Connected)")
				.l(Arrays.asList(ChatColor.GRAY + "You are connected to Test.",
						ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Test server for developers."));
				
				setAction(i, (player, event) -> {
					if (playerdata.getPlayer() == null) return;
					
					player.sendMessage(ChatColor.RED + "You are already connected to Test.");
					
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					
				});
				
			} else if (servers.get("test") == null || servers.get("test") == true) // Online
			{
				setItem(i, Material.COMMAND_BLOCK)
				.n(ChatColor.GREEN + "Test")
				.l(Arrays.asList(ChatColor.GRAY + "Click to join Test.",
						ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Test server for developers."));
				
				setAction(i, (player, event) -> {
					if (playerdata.getPlayer() == null) return;
					
					player.sendMessage(ChatColor.GREEN + "Attempting to connect to Test.");
					
					Main.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to Test.");
					
					final ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("Connect");
					out.writeUTF("test");
					player.sendPluginMessage(Main.plugin, "pgf:main", out.toByteArray());
					
					playerdata.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
					
				});
				
			} else // Offline
			{
				setItem(i, Material.RED_STAINED_GLASS)
				.n(ChatColor.DARK_RED + "Test - Offline")
				.l(Arrays.asList(ChatColor.GRAY + "Click to join Test.",
						ChatColor.GRAY + blackRightPointingPointerIcon + " " + ChatColor.ITALIC + "Test server for developers."));
				
				setAction(i, (player, event) -> {
					if (playerdata.getPlayer() == null) return;
					
					player.sendMessage(ChatColor.RED + "Test is offline.");
					
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					
				});
				
			}
			
			servers.remove("test");
			i--;
			
		}
		
		/**
		 * The Past seasons server
		 */
		if (playerdata.hasPermission("net.pgfmc.proxycore.connect.past"))
		{
			if (Main.thisServerName != null && Main.thisServerName.equals("past")) // Connected
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
					
					Main.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to Past Seasons.");
					
					final ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("Connect");
					out.writeUTF("past");
					player.sendPluginMessage(Main.plugin, "pgf:main", out.toByteArray());
					
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
		
		/**
		 * Default itemstack icons / catch case
		 */
		for (final String server : servers.keySet())
		{
			if (i == 1) break; // max 3 servers
			if (!playerdata.hasPermission("net.pgfmc.proxycore.connect." + server)) continue;
			
			final String serverNamePronounified = server.substring(0, 1).toUpperCase() + server.substring(1);
			
			if (Main.thisServerName != null && Main.thisServerName.equals(server)) // Connected
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
					
					Main.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to " + serverNamePronounified + ".");
					
					final ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("Connect");
					out.writeUTF(server);
					player.sendPluginMessage(Main.plugin, "pgf:main", out.toByteArray());
					
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
