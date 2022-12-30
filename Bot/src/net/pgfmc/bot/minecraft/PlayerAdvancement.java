package net.pgfmc.bot.minecraft;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.pgfmc.bot.Main;
import net.pgfmc.bot.discord.Discord;
import net.pgfmc.bot.util.Colors;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class PlayerAdvancement {
	
	public void checkPacketForAdvancement() {
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Main.plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ADVANCEMENTS) {
			@Override
			public void onPacketSending(PacketEvent event)
			{
				PlayerData pd = PlayerData.from(event.getPlayer());
				String advancement = event.getPacket().getStrings().read(0);
				
				advancement = advancement.replace(pd.getName() + " ", "");
				
				EmbedBuilder eb = Discord.simplePlayerEmbed(pd.getOfflinePlayer(), advancement, Colors.GOLD);
				
				Discord.sendEmbed(eb.build()).queue();
			}
		});
		
	}

}
