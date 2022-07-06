package net.pgfmc.bot.listeners.minecraft;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.pgfmc.bot.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class AssignRolesOnJoin implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		String discordId = pd.getData("Discord");
		
		Bukkit.getLogger().warning("Setting roles for player " + pd.getName());
		
		LuckPerms lp = LuckPermsProvider.get();
		UserManager userManager = lp.getUserManager();
		
		// Reset roles
		userManager.modifyUser(pd.getUniqueId(), user -> {
	        user.data().clear(NodeType.INHERITANCE::matches);
		});
		
		// Use default role (member) if account isn't linked
		if (discordId == null || discordId.isEmpty()) return;
		
		User user = Discord.JDA.getUserById(discordId);
		Member member = Discord.getGuildPGF().getMember(user);
		
		// Add role from Discord to LuckPerms
		userManager.modifyUser(pd.getUniqueId(), lpUser -> {
			
			// member's discord roles
			List<Role> roles = member.getRoles();
			
			// Find the highest supported role
			for (Role role : roles)
			{
				Group group = lp.getGroupManager().getGroup(role.getName().toLowerCase());
				
				if (group != null)
				{
					// Create a node to add to the player.
		            Node node = InheritanceNode.builder(group).build();

		            // Add the node to the user.
		            lpUser.data().add(node);
					return;
					
				}
				
			}
            
		});
			
	}

}
