package net.pgfmc.core.cmd.serverselector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
		super(InventoryType.HOPPER, Component.text("Server Selector"));
		
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
					.name(Component.text(blackStarIcon + " Season13 (Connected)", NamedTextColor.GREEN))
					.lore(
                        Arrays.asList((Component.text("You are connected to Season13.", NamedTextColor.GRAY)),
                            Component.text(blackRightPointingPointerIcon + "Claims, Homes, tpa, and more.", NamedTextColor.GRAY, TextDecoration.ITALIC)));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(NamedTextColor.RED + "You are already connected to Season13.");
						
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
					});
					
				} else if (servers.get("season13") == null || servers.get("season13") == true) // Online
				{
					setItem(i, Material.GRASS_BLOCK)
					.name(Component.text("Season13", NamedTextColor.GREEN))
					.lore(
                        Arrays.asList((Component.text("Click to join Season13.", NamedTextColor.GRAY)),
                            Component.text(blackRightPointingPointerIcon + "Claims, Homes, tpa, and more.", NamedTextColor.GRAY, TextDecoration.ITALIC)));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(NamedTextColor.GREEN + "Attempting to connect to Season13.");
						
						CoreMain.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to Season13.");
						
						PluginMessageType.CONNECT.send(player, "season13");
						
						playerdata.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
						
					});
					
				} else // Offline
				{
					setItem(i, Material.RED_STAINED_GLASS)
					.name(Component.text("Season13 - Offline", NamedTextColor.DARK_RED))
					.lore(
                        Arrays.asList((Component.text("Click to join Season13.", NamedTextColor.GRAY)),
                            Component.text(blackRightPointingPointerIcon + "Claims, Homes, tpa, and more.", NamedTextColor.GRAY, TextDecoration.ITALIC)));
					
					setAction(i, (player, event) -> {
						if (playerdata.getPlayer() == null) return;
						
						player.sendMessage(NamedTextColor.RED + "Season13 is offline.");
						
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
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
				.name(Component.text(blackStarIcon + " " + serverNamePronounified + " (Connected)", NamedTextColor.GREEN))
				.lore(Arrays.asList(Component.text("You are connectd to " + serverNamePronounified + ".", NamedTextColor.GRAY)));
				
				setAction(i, (player, event) -> {
					if (playerdata.getPlayer() == null) return;
					
					player.sendMessage(NamedTextColor.RED + "You are already connected to " + serverNamePronounified + ".");
					
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					
				});
				
			} else if (servers.get(server)) // Online
			{
				setItem(i, Material.SAND)
				.name(Component.text(serverNamePronounified, NamedTextColor.GREEN))
				.lore(Arrays.asList(Component.text("Click to join " + serverNamePronounified + ".", NamedTextColor.GRAY)));
				
				setAction(i, (player, event) -> {
					if (playerdata.getPlayer() == null) return;
					
					player.sendMessage(NamedTextColor.GREEN + "Attempting to connect to " + serverNamePronounified + ".");
					
					CoreMain.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to " + serverNamePronounified + ".");
					
					PluginMessageType.CONNECT.send(player, server);
					
					playerdata.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
					
				});
				
			} else // Offline
			{
				setItem(i, Material.GRAVEL)
				.name(Component.text(serverNamePronounified + " - Offline", NamedTextColor.DARK_RED))
				.lore(Arrays.asList(Component.text("Click to join " + serverNamePronounified + ".", NamedTextColor.GRAY)));
				
				setAction(i, (player, event) -> {
					if (playerdata.getPlayer() == null) return;
					
					player.sendMessage(NamedTextColor.RED + serverNamePronounified + " is offline.");
					
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					
				});
			}
			i--;
		}
	}
}
