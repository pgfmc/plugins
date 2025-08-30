package net.pgfmc.core.cmd.test.inventory;

import java.util.List;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.SoundEffect;
import net.pgfmc.core.util.commands.PlayerCommand;

public class TestInventorySizeCommand extends PlayerCommand {

	public TestInventorySizeCommand(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		final Player player = pd.getPlayer();
		
		if (player == null || !player.isOnline()) return true;
		
		pd.sendMessage(ChatColor.GREEN + "Opening inventory for testing the sizes.");
		SoundEffect.PING.play(pd);
		
		player.openInventory(new TestInventorySizeInventory(27, "Size: 27").getInventory());
		
		return true;
	}

}
