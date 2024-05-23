package net.pgfmc.proxycore.util;

import net.pgfmc.proxycore.Main;

public interface Logger {
	
	public static final org.slf4j.Logger VELOCITY_LOGGER = Main.plugin.logger;
	public static final boolean DEBUG = true; // manually toggle this,,, ;/
											   // org.slf4j.Logger seems complicated
	
	public static void debug(final String message)
	{
		if (!DEBUG) return;
		
		VELOCITY_LOGGER.info(message);
	}
	
	public static void log(final String message)
	{
		VELOCITY_LOGGER.info(message);
	}
	
	public static void warn(final String message)
	{
		VELOCITY_LOGGER.warn(message);
	}
	
	public static void error(final String message)
	{
		VELOCITY_LOGGER.error(message);
	}

}
