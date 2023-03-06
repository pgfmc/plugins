package net.pgfmc.modtools.cmd.powertool;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.playerdata.PlayerData;

public class PowertoolExecutor implements Listener {
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPowertool(PlayerInteractEvent e)
	{
		if (!e.getPlayer().hasPermission("pgf.admin.powertool")) return;
		if (!(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR)) return;
		
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.from(p);
		
		Material tool = p.getInventory().getItemInMainHand().getType();
		Map<Material, String> tools = (Map<Material, String>) Optional.ofNullable(pd.getData("powertools")).orElse(new HashMap<>());
		
		if (tools.isEmpty() || tool == Material.AIR) return;
		if (!tools.containsKey(tool)) return;
		
		p.performCommand(tools.get(tool));
		e.setCancelled(true);
		
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		if (!e.getPlayer().hasPermission("pgf.admin.powertool")) return;
		
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.from(p);
		
		Map<Material, String> tools = (Map<Material, String>) Optional.ofNullable(pd.getData("powertools")).orElse(new HashMap<>());
		
		if (tools.isEmpty()) return;
		
		p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "WARNING!"
				+ "\n"
				+ ChatColor.RESET + ChatColor.RED + "Active powertools:"
				+ "\n"
				+ tools.toString());
		
	}

}
