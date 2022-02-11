package net.pgfmc.core.cmd.base;

import org.bukkit.command.CommandSender;

import net.pgfmc.core.playerdataAPI.PlayerData;

public abstract class PlayerArg1Cmd extends CmdBase {

	public PlayerArg1Cmd(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		
		if (args.length < 1) {
			sender.sendMessage("�cPlease input a player's name!");
			return true;
		}
		
		PlayerData pdarg = PlayerData.from(args[0]);
		
		if (pdarg == null) {
			sender.sendMessage("�cPlease input a player's name!");
			return true;
		}
		
		return execute(sender, alias, pdarg);
	}
	
	public abstract boolean execute(CommandSender sender, String alias, PlayerData arg);
}
