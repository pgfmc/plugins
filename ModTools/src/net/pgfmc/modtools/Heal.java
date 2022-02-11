package net.pgfmc.modtools;

import org.bukkit.Bukkit;
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
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		Player p;
		
		if (args.length >= 1)
		{
			p = Bukkit.getPlayer(args[0]);
			if (p == null)
			{
				sender.sendMessage("§cCould not find player §6§n" + args[0] + "§r§c.");
				return true;
			}
		} else { p = (Player) sender; }
		
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		p.setFoodLevel(20);
		p.setSaturation(20);
		
		p.getActivePotionEffects().stream()
		.forEach(effect -> p.removePotionEffect(
				effect.getType()
				));
		
		p.sendMessage("§aHealed!");
		
		return true;
	}

}
