package net.pgfmc.core;

import org.bukkit.ChatColor;

public enum PGFRole {
	FOUNDER(ChatColor.AQUA),
	ADMIN(ChatColor.RED),
	DEVELOPER(ChatColor.DARK_GREEN),
	MODERATOR(ChatColor.LIGHT_PURPLE),
	TRAINEE(ChatColor.DARK_PURPLE),
	STAFF(ChatColor.GOLD),
	DOOKIE(ChatColor.GOLD),
	DONATOR(ChatColor.YELLOW),
	VETERAN(ChatColor.BLUE),
	MEMBER(ChatColor.GOLD);
	
	private ChatColor color;
	
	public static String STAFF_DIAMOND = new String(Character.toChars(0x2726)); // 0x2726 is the staff star Unicode
	
	PGFRole(ChatColor color)
	{
		this.color = color;
		
	}
	
	@Override
	public String toString()
	{
		return name().substring(0,1) + name().substring(1).toLowerCase();
	}
	
	public ChatColor getColor() {
		return color;
		
	}
	
	/**
	 * Gets a PGFRole from an anycase String, null if not found
	 * @param role
	 * @return
	 */
	public static PGFRole get(final String name)
	{
		for (final PGFRole r : PGFRole.values())
		{
			if (r.name().equalsIgnoreCase(name)) return r;
			
		}
		
		return MEMBER;
	}
	
}
