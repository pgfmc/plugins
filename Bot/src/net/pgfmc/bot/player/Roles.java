package net.pgfmc.bot.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.pgfmc.bot.Discord;
import net.pgfmc.core.permissions.Role;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Roles {
	
	private static List<Role> getDefault() {
		List<Role> r = new ArrayList<>(1);
		r.add(Role.MEMBER);
		return r;
	}
	
	public static void recalculateRoles(PlayerData pd) {
		
		String discordo = (String) pd.loadFromFile("Discord");
		
		if (discordo != null) {
			pd.setData("Discord", discordo);
			
			List<Role> list = Role.getRoles(Discord.JDA.getGuildById("579055447437475851").getMemberById(discordo).getRoles().stream().map(x -> x.getId()).collect(Collectors.toList()));
			pd.setData("Roles", list);
			
		} else {
			pd.setData("Roles", getDefault());
		}
	}
}
