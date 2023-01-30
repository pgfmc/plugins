package net.pgfmc.modtools.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		Player p;
		
		if (args.length >= 1)
		{
			p = Bukkit.getPlayer(args[0]);
			if (p == null)
			{
				sender.sendMessage(ChatColor.RED + "Could not find player " + ChatColor.GOLD + ChatColor.UNDERLINE + args[0] + ChatColor.RESET + ChatColor.RED + ".");
				return true;
			}
		} else {
			p = (Player) sender;
		}
		
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		p.setFoodLevel(20);
		p.setSaturation(20);
		
		p.getActivePotionEffects().stream()
				.forEach(effect -> p.removePotionEffect(effect.getType()));
		
		p.sendMessage(ChatColor.GREEN + "Healed!");
		
		return true;
	}

}
