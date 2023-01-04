package net.pgfmc.core.cmd.admin;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.ServerPlayerAtPlayerCommand;

public class Skull extends ServerPlayerAtPlayerCommand {

	
	public Skull(String name) {
		super(name);
	}
	
	@Override
	public boolean execute(PlayerData pd, String alias, PlayerData arg) {
		pd.getPlayer().getInventory().addItem(getHead(arg.getUniqueId(), null));
		return true;

	}
	
	@Override
	public boolean playerPredicate(PlayerData arg, PlayerData sender) {
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
