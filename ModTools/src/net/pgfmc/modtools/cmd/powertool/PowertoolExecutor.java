package net.pgfmc.modtools.cmd.powertool;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.Main;

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
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerSwitchToItem(PlayerItemHeldEvent e)
	{
		if (!e.getPlayer().hasPermission("pgf.admin.powertool")) return;
		
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.from(p);
		
		Material tool = p.getInventory().getItemInMainHand().getType();
		Map<Material, String> tools = (Map<Material, String>) Optional.ofNullable(pd.getData("powertools")).orElse(new HashMap<>());
		
		if (tools.isEmpty() || tool == Material.AIR) return;
		if (!tools.containsKey(tool)) return;
		
		final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		final PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SET_ACTION_BAR_TEXT);
		final String message = ChatColor.LIGHT_PURPLE + "Active powertool: /" + tools.get(tool);
		
		packet.getChatComponents().write(0, WrappedChatComponent.fromText(message));
		protocolManager.sendServerPacket(p, packet);
		
		Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
			@Override
			public void run() {
				if (!p.isOnline()) return;
				
				PacketContainer packetReset = protocolManager.createPacket(PacketType.Play.Server.SET_ACTION_BAR_TEXT);
				packetReset.getChatComponents().write(0, WrappedChatComponent.fromText(""));
				protocolManager.sendServerPacket(p, packetReset);
				
			}
			
		}, 5);
		
	}

}
