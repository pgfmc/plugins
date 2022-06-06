package net.pgfmc.ffa.cmd.zones;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.pgfmc.ffa.cmd.handler.FFACmd;
import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class List implements FFACmd {

	@Override
	public void CmdDo(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(ChatColor.RED + "" + ChatColor.UNDERLINE + "Zones for PGF-FFA" + ChatColor.RESET
				+ "\n" + ChatColor.RED + Arrays.asList(Zone.values()).toString());
		
	}

}
