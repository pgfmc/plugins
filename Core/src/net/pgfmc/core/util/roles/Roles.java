package net.pgfmc.core.util.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.bot.discord.Discord;

public class Roles implements Listener {
	
	/**
	 * Set and apply roles to player (update roles)
	 * 
	 * @param pd The player to update roles
	 * @param role The role to apply
	 */
	public static void setRole(PlayerData pd, PGFRole role)
	{
		Bukkit.getLogger().warning("Recalculating role for player " + pd.getName());
		
		LuckPerms lp = LuckPermsProvider.get();
		UserManager userManager = lp.getUserManager();
		
		// Remove groups from user, save changes
		userManager.modifyUser(pd.getUniqueId(), user -> {
	        user.data().clear(NodeType.INHERITANCE::matches);
	        
		});
		
		// Add group to the user, save changes
		userManager.modifyUser(pd.getUniqueId(), user -> {
			
			if (role.getName().equals("member"))
			{
				Node node = InheritanceNode.builder("default").value(true).build();
				user.data().add(node);
				
			} else
			{
	            Node node = InheritanceNode.builder(role.getName()).value(true).build();
	            user.data().add(node);
	            
			}
            
		});
		
		pd.setData("role", role);
		
	}
	/**
	 * Used if you don't have a list of the player's roles
	 * This will get them for you
	 * 
	 * @param pd The player to update roles
	 */
	public static void setRole(PlayerData pd)
	{
		// Get roles, get top role
		List<PGFRole> playerRoles = getPlayerRoles(pd);
		PGFRole role = getTop(playerRoles);
		
		setRole(pd, role);
		
	}	
	
	/**
	 * Returns a list of PGFRole from a list of Discord role names
	 * 
	 * @param discordRoles List of Discord role names from PGF-Bot
	 * @return List of PGFRole
	 */
	public static List<PGFRole> getPlayerRoles(PlayerData pd)
	{
		List<String> rolesAsString = Discord.getMemberRoles(pd.getData("Discord"));
		if (rolesAsString == null || rolesAsString.isEmpty()) return new ArrayList<PGFRole>(Arrays.asList(PGFRole.MEMBER));
		
		// Takes a list of string names and gets PGFRole enums and potential null values
		// Then removes the null values
		return rolesAsString.stream()
				.map(r -> PGFRole.get(r))
				.collect(Collectors.toList()).stream()
				.filter(r -> r != null)
				.collect(Collectors.toList());
		
	}
	
	public static PGFRole getTop(Collection<PGFRole> roles)
	{
		// Return MEMBER Role if null or empty
		// Return the only Role if only 1 Role in list
		if (roles == null || roles.isEmpty()) return PGFRole.MEMBER;
		if (roles.size() == 1) return roles.stream().collect(Collectors.toList()).get(0);
		
		// Quick sort against the order of the enums to find the top Role
		return roles.stream()
				.sorted((r1, r2) -> r1.compareTo(r2))
				.collect(Collectors.toList())
				.get(0);
		
	}
	public static PGFRole getTop(PlayerData pd)
	{
		return getTop(getPlayerRoles(pd));
	}
	
	@EventHandler
	public void assignPlayerRoleOnLogin(PlayerLoginEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		setRole(pd);
	}
	
}
