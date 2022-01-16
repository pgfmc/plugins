package net.pgfmc.core.cmd.admin;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Skull implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (args.length > 0)
		{
			Player player = Bukkit.getPlayer(args[0]);
			
			if (Bukkit.getPlayer(args[0]) == null)
			{
				p.sendMessage("§cCould not find player §6§n" + args[0]);
				return true;
			}
			
			String lore = null;
			if (args.length >= 2)
			{
				lore = String.join(" ", args).replace(args[0], "").replace("&", "§").strip();
			}
			
			p.getInventory().addItem(getHead(player.getUniqueId(), lore));
			
			return true;
		}
		
		p.getInventory().addItem(getHead(p.getUniqueId(), null));
		
		return true;
	}
	
	public static ItemStack getHead(UUID player, String lore)
	{
		// Credit: https://www.spigotmc.org/threads/skullmeta-with-custom-skin-in-gui-1-14-4.403014/#post-3598618
		// Edited by me lol
		
		ItemStack item = new ItemStack(Material.PLAYER_HEAD); // Create a new ItemStack of the Player Head type.
		SkullMeta meta = (SkullMeta) item.getItemMeta(); // Get the created item's ItemMeta and cast it to SkullMeta so we can access the skull properties
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(player)); // Set the skull's owner so it will adapt the skin of the provided username (case sensitive).
		
		if (lore != null)
		{
			lore = lore.replace(";", "\n");
			meta.setLore(Arrays.asList(lore));
		}
		
		item.setItemMeta(meta); // Apply the modified meta to the initial created item
		
		return item;
	}

}
