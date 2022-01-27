package net.pgfmc.core.cmd.donator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.permissions.Role;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Nick implements CommandExecutor {
	
	/*
	 * Color codes and format codes
	 */
	public static final List<String> colors = new ArrayList<>(Arrays.asList("&0"
			, "&2", "&4", "&6", "&8"
			, "&a", "&c", "&e", "&1"
			, "&3", "&5", "&7", "&9"
			, "&b", "&d", "&d", "&f"));
	public static final List<String> formats = new ArrayList<>(Arrays.asList("&k"
			, "&m", "&o", "&l"
			, "&n", "&r", "&k"));

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		if (args.length == 0)
		{
			return false;
		}
		
		setNick((Player) sender, args);
		
		return true;
	}
	
	
	/**
	 * Removes the color codes and formatting codes
	 * from the String
	 * 
	 * @param nick String to remove codes from
	 * @return a "raw" form of "nick", no codes
	 */
	public static String removeCodes(String nick)
	{
		String nickname = nick.replace("§", "&");
		
		for (String code : colors)
		{
			nickname = nickname.replace(code, "");
		}
		
		for (String code : formats)
		{
			nickname = nickname.replace(code, "");
		}
		
		return nickname;
	}
	/**
	 * This prevents a player from
	 * having the same name/nickname as another player
	 * 
	 * @param p The sus player
	 * @return The name to use with color codes
	 */
	public static void removeImpostors(PlayerData pd)
	{
		// The nickname without color codes
		String raw = removeCodes((String) Optional.ofNullable(pd.getData("nick")).orElse(pd.getName())).toLowerCase();
		// If their raw nickname is just their player name, ignore
		if (raw.equals(pd.getName().toLowerCase())) { return; }
		
		// Loop through every player
		for (OfflinePlayer op : Bukkit.getOfflinePlayers())
		{
			// Skip the player who's nickname we're checking for impostors
			if (op.getUniqueId().equals(pd.getUniqueId())) { continue; }
			
			// If player1's name matches player2's name OR their raw nicknames match
			if (op.getName().toLowerCase().equals(raw) || removeCodes(
					((String) Optional.ofNullable(PlayerData.getPlayerData(op).getData("nick")).orElse(""))).toLowerCase()
					.equals(raw))
			{
				// Remove the impostor's nickname
				pd.setData("nick", null).queue();
				return;
			}
		}
	}
	
	/**
	 * Set a nickname to a player
	 * @param p Player
	 * @param nick Nickname
	 */
	public static void setNick(Player p, String nick)
	{
		PlayerData pd = PlayerData.getPlayerData(p);
		
		Role role = Role.getDominant(pd.getData("Roles"));
		
		if (role.getDominance() < Role.VETERAN.getDominance())
		{
			p.sendMessage("§cYou do not have permission to use this command.");
			return;
		}
		
		nick = nick.replaceAll("[^A-Za-z0-9&]", "").replace("&k", "");
		String raw = removeCodes(nick);
		
		/*
		 * A raw length of 0 means the nickname had no content, just color codes (lmao)
		 */
		if (raw.length() <= 0)
		{
			p.sendMessage("§cThe nickname must be more than just color codes!");
			return;
		}
		
		/*
		 * The nickname without color codes must be less than 20 characters
		 */
		if (raw.length() > 20)
		{
			p.sendMessage("§cThe max nickname length is 20!");
			return;
		}
		
		/*
		 * If the raw nickname is "off" or "reset" or the player's name
		 * then it will reset the nickname to Player.getName()
		 */
		if (raw.equals("off") || raw.equals("reset") || nick.equals(p.getName()))
		{
			pd.setData("nick", null).queue();
			p.sendMessage("§6Nickname changed to " + pd.getRankedName() + "§6!");
			
			return;
		}
		
		/*
		 * No impostors, check removeImpostors() for comments
		 */
		for (OfflinePlayer op2 : Bukkit.getOfflinePlayers())
		{
			String raw2 = raw.toLowerCase();
			if (raw2.equals(pd.getName().toLowerCase())) { break; }
			
			if (op2.getUniqueId().equals(pd.getUniqueId())) { continue; }
			
			if (op2.getName().toLowerCase().equals(raw2) || removeCodes(
					((String) Optional.ofNullable(PlayerData.getPlayerData(op2).getData("nick")).orElse(""))).toLowerCase()
					.equals(raw2))
			{
				p.sendMessage("§cYou cannot have the same name as another player!");
				return;
			}
		}
		
		pd.setData("nick", nick.replace("&", "§") + "§r").queue();
		p.sendMessage("§6Nickname changed to " + pd.getRankedName() + "§6!");
	}
	
	public static void setNick(Player p, String[] nick)
	{
		setNick(p, String.join("", nick));
	}

}
