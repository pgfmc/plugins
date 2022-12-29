package net.pgfmc.bot.util;

import java.awt.Color;

public enum Colors {
	GREEN(0, 255, 0), // Join
	RED(255, 0, 0), // Leave
	GOLD(255, 215, 0), // Advancement
	BLACK(0, 0, 0); // Death
	
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
