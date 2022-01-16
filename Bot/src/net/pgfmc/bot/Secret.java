package net.pgfmc.bot;

public class Secret {
	
	public static String getKey()
	{
		return Main.plugin.getConfig().getString("token");
	}
}