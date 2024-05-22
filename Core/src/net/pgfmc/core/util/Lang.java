package net.pgfmc.core.util;

import org.bukkit.ChatColor;

public enum Lang {
	COMMAND_PLAYER_REQUIRED(ChatColor.RED + "Only players can execute this command." + ChatColor.RESET),
	PERMISSION_DENIED(ChatColor.RED + "You do not have permission to use this command." + ChatColor.RESET);
	
	private final String content;
	
	private Lang(String content)
	{
		this.content = content;
	}
	
	public String getLang()
	{
		return content;
	}

}
