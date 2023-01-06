package net.pgfmc.core.cmd.donator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Profanity;

public class Nick implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		setNick((Player) sender, args);
		
		return true;
	}
	
	/**
	 * Set a nickname to a player
	 * @param p Player
	 * @param nick Nickname
	 */
	public static void setNick(Player p, String nick)
	{
		PlayerData pd = PlayerData.from(p);
		
		if (!pd.hasPermission("pgf.cmd.donator.nick"))
		{
			pd.sendMessage("§cYou do not have permission to use this command.");
			return;
		}
		
		nick = nick.replaceAll("[^A-Za-z0-9&]", "")
				.replace("&k", "")
				.replace("&m", "")
				.replace("&o", "")
				.replace("&n", "")
				.replace("&l", "")
				.replace("&r", "");
		String raw = ChatColor.stripColor(nick.replace('&', '§'));
		
		if (Profanity.hasProfanity(raw))
		{
			p.sendMessage(ChatColor.RED + "Please do not include profanity!");
			return;
		}
		
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
		if (raw.equals("off") || raw.equals("reset") || nick.equals(p.getName()) || nick.equals(""))
		{
			pd.setData("nick", null).queue();
			p.sendMessage("§6Nickname changed to " + pd.getRankedName() + "§6!");
			
			return;
		}
		
		pd.setData("nick", nick.replace("&", "§")).queue();
		p.sendMessage("§6Nickname changed to " + pd.getRankedName() + "§6!");
		
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
		if (nick.length <= 0) setNick(p, "");
		
		setNick(p, String.join("", nick));
	}
	
	public static String getNick(OfflinePlayer p)
	{
		if (p.getPlayer() == null
				|| !p.isOnline()
				|| !p.getPlayer().hasPermission("pgf.cmd.donator.nick")
				|| PlayerData.getData(p, "nick") == null) return p.getName();
		
		return "~" + ((String) PlayerData.getData(p, "nick"));
			
	}

}
