package net.pgfmc.modtools.cmd.powertool;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.playerdata.PlayerData;

public class Powertool implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can exectute this command.");
			return true;
		}
		
		if (args.length == 0)
		{
			sender.sendMessage(ChatColor.RED + "Usage: /powertool <command without slash>");
			return true;
		}
		
		Player p = (Player) sender;
		PlayerData pd = PlayerData.from(p);
		
		String power = String.join(" ", args);
		ItemStack tool = p.getInventory().getItemInMainHand();
		Map<Material, String> tools = pd.getData("powertools");
		
		if (args[0].equals("remove"))
		{
			if (tool == null)
			{
				p.sendMessage(ChatColor.RED + "Invalid tool.");
				return true;
			}
			
			if (!tools.containsKey(tool.getType())) return true;
			
			tools.remove(tool.getType());
			pd.setData("powertools", tools);
			
			p.sendMessage(ChatColor.GREEN + "Removed powertool (/" + tools.get(tool.getType()) + ")!");
			
			return true;
		}
		
		if (tool == null)
		{
			p.sendMessage(ChatColor.RED + "Invalid tool.");
			return true;
		}
		
		if (power.equals(null))
		{
			p.sendMessage(ChatColor.RED + "Invalid command.");
			return true;
		}
		
		if (tools.containsKey(tool.getType()))
		{
			p.sendMessage(ChatColor.RED + "Overwriting existing powertool. (/" + tools.get(tool.getType()) + ")");
			
		}
		
		tools.put(tool.getType(), power);
		pd.setData("powertools", tools);
		
		p.sendMessage(ChatColor.GREEN + "Powertool applied! Use \"/powertool remove\" to remove it.");
		
		return true;
	}

}
