package net.pgfmc.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringDate {
	
	/**
	 * Get a date and time in String format
	 * @return
	 */
	public static String date()
	{
		return new SimpleDateFormat("MMM dd, YYYY @ kkmm").format(new Date()); // Jan, 01, 2022 @ 0330
	}

}
