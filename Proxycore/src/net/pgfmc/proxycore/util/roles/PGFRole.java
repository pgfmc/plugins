package net.pgfmc.proxycore.util.roles;

import net.kyori.adventure.text.format.NamedTextColor;

public enum PGFRole {
	FOUNDER(NamedTextColor.AQUA),
	ADMIN(NamedTextColor.RED),
	DEVELOPER(NamedTextColor.DARK_GREEN),
	MODERATOR(NamedTextColor.LIGHT_PURPLE),
	TRAINEE(NamedTextColor.DARK_PURPLE),
	STAFF(NamedTextColor.GOLD),
	DOOKIE(NamedTextColor.GOLD),
	DONATOR(NamedTextColor.YELLOW),
	VETERAN(NamedTextColor.BLUE),
	MEMBER(NamedTextColor.GOLD);
	
	private final NamedTextColor color;
	public static final String STAFF_DIAMOND = new String(Character.toChars(0x2726)); // 0x2726 is the staff star Unicode
	
	PGFRole(final NamedTextColor color)
	{
		this.color = color;
		
	}
	
	@Override
	public String toString()
	{
		return name().substring(0,1) + name().substring(1).toLowerCase();
	}
	
	public NamedTextColor getColor() {
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
		
		return null;
	}
	
}
