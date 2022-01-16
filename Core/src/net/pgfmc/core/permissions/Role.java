package net.pgfmc.core.permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Roles Enum
 * @author CrimsonDart
 *
 */
public enum Role {
	FOUNDER("§b✦", 7, Permissions.adminPerms),
	ADMIN("§c✦", 6, Permissions.adminPerms),
	DEVELOPER("§2✦", 5, Permissions.devPerms),
	MODERATOR("§d✦", 4, Permissions.modPerms),
	TRAINEE("§5✦", 3, Permissions.traineePerms),
	DONATOR("§e", 2, Permissions.donatorPerms),
	VETERAN("§9", 1, Permissions.veteranPerms),
	MEMBER("§6", 0, Permissions.defaultPerms);
	
	private String colorCode;
	private int dominance;
	private String[] permissions;
	private String id;
	
	Role(String cc, int d, String[] pps) {
		colorCode = cc;
		dominance = d;
		permissions = pps;
	}
	
	public String getColorCode() {
		return colorCode;
	}
	
	public int getDominance() {
		return dominance;
	}
	
	public String getID() {
		return id;
	}
	
	public static Role getRole(String id) {
		
		if (id.equals("579062298526875648")) { return MEMBER;} else
		if (id.equals("779928656658432010")) { return VETERAN;} else
		if (id.equals("899932873921003540") || id.equals("645442029756874753")) { return DONATOR;} else
		if (id.equals("814184657674305546")) { return DEVELOPER;} else
		if (id.equals("802671617804730379")) { return TRAINEE;} else
		if (id.equals("595560680023654401")) { return MODERATOR;} else
		if (id.equals("594015606626320417")) { return ADMIN;} else
		if (id.equals("579061127921664000")) { return FOUNDER;}
		
		return null;
	}
	
	public static List<Role> getRoles(List<String> ids) {
		
		return ids.stream()
				.map(x -> getRole(x))
				.filter(x -> (x != null))
				.collect(Collectors.toList());
		
	}
	
	public static List<String> getPermissions(List<Role> roles)
	{
		List<String> permissions = new ArrayList<String>();
		
		for (Role r : roles)
		{
			permissions.addAll(Arrays.asList(r.permissions));
		}
		return permissions;
	}
	
	public static String[] getPermissions(Role r)
	{
		return r.permissions;
	}
	
	public static Role getDominantOf(List<String> ids) {
		return getDominant(getRoles(ids));
	}
	
	public static Role getDominant(List<Role> list) {
		
		// System.out.println(list);
		
		if (list == null || list.size() == 0) {
			// System.out.println("out 1");
			return MEMBER;
		}
		
		return list.stream()
				.reduce((h, r) -> {
			if (h == null) {
				return r;
			} 
			return (r.dominance > h.dominance) ? r : h;
		}).get();
	}
}
