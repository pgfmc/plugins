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
		
		Player target;
		
		if (args.length >= 1)
		{
			target = Bukkit.getPlayer(args[0]);
			
			if (target == null || !target.isOnline())
			{
				sender.sendMessage(ChatColor.RED + "Could not find player " + ChatColor.GOLD + ChatColor.UNDERLINE + args[0] + ChatColor.RESET + ChatColor.RED + ".");
				return true;
			}
			
		} else {
			target = (Player) sender;
			
		}
		
		target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		target.setFoodLevel(20);
		target.setSaturation(20);
		
		target.getActivePotionEffects().stream()
				.forEach(effect -> target.removePotionEffect(effect.getType()));
		
		target.sendMessage(ChatColor.GREEN + "Healed!");
		
		return true;
	}

}
