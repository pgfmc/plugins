package net.pgfmc.proxycore.serverselector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Lang;
import net.pgfmc.proxycore.Main;

/**
 * ConnectCommand is used to connect to another server.
 * 
 * It opens a inventory containing available servers to the player.
 * 
 * For a server to display in the inventory, the player needs permission
 * to connect to it.
 */
public final class ConnectCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
		{
			sender.sendMessage(Lang.COMMAND_PLAYER_REQUIRED.getLang());
			return true;
		}
		
		final Player player = (Player) sender;
		
		
		if (!player.hasPermission("net.pgfmc.proxycore.connect"))
		{
			player.sendMessage(Lang.PERMISSION_DENIED.getLang());
			return true;
		}
		
		if (args.length == 0)
		{
			player.sendMessage(ChatColor.RED + "Please specify a server to connect to.");
			
			if (Main.registeredServers == null || Main.registeredServers.isEmpty())
			{
				player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Available servers: " + ChatColor.LIGHT_PURPLE + "[test, past, survival]");
				
				return true;
			}
			
			/**
			 * Discards any registered servers if the player doesn't have permission to connect to it.
			 * Adds a marker to the server the player is connected to (if it exists)
			 */
			final List<String> servers = Main.registeredServers.keySet()
					.stream()
					.filter(server -> {
						if (!player.hasPermission("net.pgfmc.proxycore.connect." + server)) return false; // no permission, remove from list
						
						if (!Main.registeredServers.get(server)) // Server is offline
						{
							server = server + " (Offline)";
							
							return true;
						}
						
						if (!server.equals(Main.thisServerName)) return true; // Online, but not this server
						
						// this server
						final String blackStarIcon = new String(Character.toChars(0x2605));
						server = blackStarIcon + " " + server + " (Connected)";
						
						return true;
					})
					.collect(Collectors.toList());
			
			player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Available servers: " + ChatColor.LIGHT_PURPLE + servers.toString());
			
			return true;
		}
		
		final PlayerData playerdata = PlayerData.from(player);
		final String server = String.join("_", args).toLowerCase();
		final String serverNamePronounified = server.substring(0, 1).toUpperCase() + server.substring(1);
		
		if (!playerdata.hasPermission("net.pgfmc.proxycore.connect." + server)) // no permission
		{
			playerdata.sendMessage(Lang.PERMISSION_DENIED.getLang());
			
			return true;
		}
		
		if (Main.registeredServers != null && !Main.registeredServers.isEmpty() && !Main.registeredServers.containsKey(server))
		{
			playerdata.sendMessage(ChatColor.RED + "This server does not exist. To see available servers, use: /connect");
			playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			
			return true;
		}
		
		if (Main.registeredServers != null && !Main.registeredServers.isEmpty() && !Main.registeredServers.get(server))
		{
			playerdata.sendMessage(ChatColor.RED + "This server is offline.");
			playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			
			return true;
		}
		
		if (Main.registeredServers != null && !Main.registeredServers.isEmpty() && server.equals(Main.thisServerName))
		{
			playerdata.sendMessage(ChatColor.RED + "You are already connected to " + serverNamePronounified);
			playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			
			return true;
		}
		
		player.sendMessage(ChatColor.GREEN + "Attempting to connect to " + serverNamePronounified + ".");
		playerdata.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 2F);
		
		Main.plugin.getLogger().info("Attempting to connect " + playerdata.getName() + " to " + serverNamePronounified + ".");
		
		final ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendPluginMessage(Main.plugin, "pgf:main", out.toByteArray()); // send connection request to proxy
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if (Main.registeredServers == null || Main.registeredServers.isEmpty())
		{
			return Arrays.asList("test", "past", "survival");
		}
		
		final List<String> servers = Main.registeredServers.keySet()
				.stream()
				.filter(server -> sender.hasPermission("net.pgfmc.proxycore.connect." + server))
				.collect(Collectors.toList());
		
		return servers;
	}
	
}
