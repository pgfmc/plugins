package net.pgfmc.survival.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

public class GiveLodestoneCommand extends PlayerCommand {

	public GiveLodestoneCommand(String name) {
		super(name);
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		if (pd.hasTag("loded"))
		{
			pd.sendMessage(ChatColor.RED + "You already got your free Lodestone!");
			
			return true;
		} else {
			pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
			pd.getPlayer().getInventory().addItem(new ItemStack(Material.LODESTONE));
			pd.addTag("loded");
			
		}
		
		return true;
	}
	
}
