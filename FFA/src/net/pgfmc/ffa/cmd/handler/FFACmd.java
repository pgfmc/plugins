package net.pgfmc.ffa.cmd.handler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface FFACmd {
	
	void CmdDo(CommandSender sender, Command cmd, String label, String[] args);

}
