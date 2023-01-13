package net.pgfmc.core.util.roles;

import org.bukkit.ChatColor;

public enum PGFRole {
	FOUNDER("b"),
	ADMIN("c"),
	DEVELOPER("2"),
	MODERATOR("d"),
	TRAINEE("5"),
	STAFF("6"),
	DOOKIE("6"),
	DONATOR("e"),
	VETERAN("9"),
	MEMBER("6");
	
	private String color;
	public static String STAFF_DIAMOND = new String(Character.toChars(0x2726));
	
	PGFRole(String color)
	{
		this.color = color;
		
	}
	
	public String getName()
	{
		return name().toLowerCase();
		
	}
	
	public String getColor(boolean hasStaffDiamond) {
		// 0x2726 is the staff star Unicode
		
		if (!hasStaffDiamond || compareTo(STAFF) > 0) return ChatColor.COLOR_CHAR + color;
		
		return ChatColor.COLOR_CHAR + color + STAFF_DIAMOND;
		
	}
	
	/**
	 * Gets a PGFRole from an anycase String, null if not found
	 * @param role
	 * @return
	 */
	public static PGFRole get(String role)
	{
		for (PGFRole r : PGFRole.values())
		{
			if (r.getName().equals(role.toLowerCase())) return r;
			
		}
		
		return null;
	}
	
}
