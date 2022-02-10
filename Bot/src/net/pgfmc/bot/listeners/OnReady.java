package net.pgfmc.bot.listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgfmc.bot.Discord;
import net.pgfmc.bot.Main;

public class OnReady implements EventListener {

	@Override
	public void onEvent(GenericEvent e) {		
		if (!(e instanceof ReadyEvent)) { return; }
		Bukkit.getLogger().warning("Discord Bot Initialized!");
		
		// Don't run the code below if Machine isn't MAIN !!!!!!!!
		if (!Main.plugin.getConfig().getBoolean("enable-command")) return;
		
		Guild guild = Discord.getGuildPGF();
		Role memberRole = guild.getRoleById("579062298526875648");
		
		List<Member> noMemberRole = guild.getMembers().stream().filter(member -> !member.getRoles().contains(memberRole)).collect(Collectors.toList());
		
		for (Member m : noMemberRole)
		{
			guild.addRoleToMember(m, memberRole);
		}
		
		// guild.upsertCommand(new CommandData("list", "Show who's online.")).queue();
		
	}

}
