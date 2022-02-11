package net.pgfmc.core.cmd.donator;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.chat.ProfanityFilter;
import net.pgfmc.core.permissions.Permissions;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Nick implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("�cOnly players can execute this command.");
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
		return ChatColor.stripColor(nick.replace('&', '�'));
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
		String nick = pd.getData("nick");
		if (nick == null) return;
		
		String raw = removeCodes(nick).toLowerCase();
		
		// If their raw nickname is just their player name, ignore
		if (raw.equals(pd.getName().toLowerCase())) return;
		
		// If op isn't pd, if op's name is pd's nickname with or without color codes
		if (Arrays.asList(Bukkit.getOfflinePlayers()).stream()
				.filter(op -> !op.getUniqueId().equals(pd.getUniqueId()) && (
						op.getName().toLowerCase().equals(raw)
							|| removeCodes(((String) Optional.ofNullable(PlayerData.getData(op, "nick"))
									.orElse(op.getName()))).toLowerCase().equals(raw)
						)).collect(Collectors.toList()).size() == 0) return; // If list is empty (no impostors)
		
		// At least 1 impostor, remove nickname
		pd.setData("nick", null).queue();
	}
	
	/**
	 * Set a nickname to a player
	 * @param p Player
	 * @param nick Nickname
	 */
	public static void setNick(Player p, String nick)
	{
		PlayerData pd = PlayerData.from(p);
		
		if (!p.hasPermission("pgf.cmd.donator.nick"))
		{
			p.sendMessage("�cYou do not have permission to use this command.");
			return;
		}
		nick = nick.replaceAll("[^A-Za-z0-9&]", "")
				.replace("&k", "")
				.replace("&m", "")
				.replace("&o", "")
				.replace("&n", "")
				.replace("&l", "")
				.replace("&r", "");
		String raw = removeCodes(nick);
		
		if (ProfanityFilter.hasProfanity(raw))
		{
			p.sendMessage(ChatColor.RED + "Please do not include profanity!");
			return;
		}
		
		/*
		 * A raw length of 0 means the nickname had no content, just color codes (lmao)
		 */
		if (raw.length() <= 0)
		{
			p.sendMessage("�cThe nickname must be more than just color codes!");
			return;
		}
		
		/*
		 * The nickname without color codes must be less than 20 characters
		 */
		if (raw.length() > 20)
		{
			p.sendMessage("�cThe max nickname length is 20!");
			return;
		}
		
		/*
		 * If the raw nickname is "off" or "reset" or the player's name
		 * then it will reset the nickname to Player.getName()
		 */
		if (raw.equals("off") || raw.equals("reset") || nick.equals(p.getName()))
		{
			pd.setData("nick", null).queue();
			p.sendMessage("�6Nickname changed to " + pd.getRankedName() + "�6!");
			
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
					((String) Optional.ofNullable(PlayerData.getData(op2, "nick")).orElse(""))).toLowerCase()
					.equals(raw2))
			{
				p.sendMessage("�cYou cannot have the same name as another player!");
				return;
			}
		}
		
		pd.setData("nick", nick.replace("&", "�")).queue();
		p.sendMessage("�6Nickname changed to " + pd.getRankedName() + "�6!");
		
		// p.setDisplayName(pd.getRankedName());
		p.setPlayerListName(pd.getRankedName());
		p.setCustomName(pd.getRankedName());
		p.setCustomNameVisible(true);
		
		for (Player playerP : Bukkit.getOnlinePlayers()) {
			if (playerP == p) continue;
			p.hidePlayer(CoreMain.plugin, playerP);
			p.showPlayer(CoreMain.plugin, playerP);
		}
		
		p.hidePlayer(CoreMain.plugin, p);
		p.showPlayer(CoreMain.plugin, p);
	}
	
	public static void setNick(Player p, String[] nick)
	{
		setNick(p, String.join("", nick));
	}
	
	public static String getNick(OfflinePlayer p)
	{
		if (Permissions.has(p, "pgf.cmd.donator.nick")) 
		{
			String nick = PlayerData.getData(p, "nick");
			
			if (nick != null) return "~" + nick;
			
		}
		
		return p.getName();
	}

}
