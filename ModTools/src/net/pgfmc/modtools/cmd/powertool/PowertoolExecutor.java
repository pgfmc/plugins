package net.pgfmc.modtools.cmd.powertool;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.playerdata.PlayerData;

public class PowertoolExecutor implements Listener {
	
	@EventHandler
	public void onPowertool(PlayerInteractEvent e)
	{
		if (!e.getPlayer().hasPermission("pgf.admin.powertool")) return;
		if (e.getAction() == Action.PHYSICAL) return;
		
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.from(p);
		
		ItemStack tool = p.getInventory().getItemInMainHand();
		Map<Material, String> tools = pd.getData("powertools");
		
		if (tools.isEmpty() || tool == null) return;
		if (!tools.containsKey(tool.getType())) return;
		
		p.performCommand(tools.get(tool.getType()));
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		if (!e.getPlayer().hasPermission("pgf.admin.powertool")) return;
		
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.from(p);
		
		Map<Material, String> tools = pd.getData("powertools");
		
		if (tools.isEmpty()) return;
		
		p.sendMessage(ChatColor.RED + "" + ChatColor.UNDERLINE + "WARNING!"
				+ "\n"
				+ ChatColor.RESET + ChatColor.RED + "Active powertools:"
				+ "\n"
				+ tools.toString());
		
	}

}
