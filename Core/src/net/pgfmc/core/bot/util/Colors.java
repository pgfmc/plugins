package net.pgfmc.core.bot.util;

import java.awt.Color;

public enum Colors {
	DARK_RED(170, 0, 0),
	RED(255, 85, 85),
	GOLD(255, 170, 0),
	YELLOW(255, 255, 85),
	DARK_GREEN(0, 170, 0),
	GREEN(85, 255, 85),
	AQUA(85, 255, 255),
	DARK_AQUA(0, 170, 170),
	DARK_BLUE(0, 0, 170),
	BLUE(85, 85, 255),
	LIGHT_PURPLE(255, 85, 255),
	DARK_PURPLE(170, 0, 170),
	WHITE(255, 255, 255),
	GRAY(170, 170, 170),
	DARK_GRAY(85, 85, 85),
	BLACK(0, 0, 0);

	
	private int r;
	private int g;
	private int b;
	
	Colors(int r, int g, int b)
	{
		this.r = r;
		this.g = g;
		this.b = g;
	}
	
	public Color getColor()
	{
		return new Color(r, g, b);
	}

}
