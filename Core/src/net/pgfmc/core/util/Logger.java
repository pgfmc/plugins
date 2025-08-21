package net.pgfmc.core.util;

import net.pgfmc.core.CoreMain;

public interface Logger {
	
	public static final java.util.logging.Logger BUKKIT_LOGGER = CoreMain.plugin.getLogger();
	public static final boolean DEBUG = false; // manually toggle this,,, ;/

	public static void debug(final String message)
	{
		if (!DEBUG) return;
		
		BUKKIT_LOGGER.info(message);
	}
	
	public static void log(final String message)
	{
		BUKKIT_LOGGER.info(message);
	}
	
	public static void warn(final String message)
	{
		BUKKIT_LOGGER.warning(message);
	}
	
	public static void error(final String message)
	{
		BUKKIT_LOGGER.severe(message);
	}

}
