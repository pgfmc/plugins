package net.pgfmc.core.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.file.Configify;
import net.pgfmc.core.file.Mixins;

public class ProfanityFilter extends Configify {
	
	public ProfanityFilter() {
		super(Mixins.getFile(CoreMain.plugin.getDataFolder() + File.separator + "profanity.yml"));
		setDefaultValue("profantity", new ArrayList<String>(Arrays.asList("test", "test1")));
	}

	private static List<String> nword = new ArrayList<String>();
	
	public static List<String> getFilter()
	{
		return nword;
	}
	
	/**
	 * Checks to see if a message has blacklisted words
	 * Doesn't stop everything though
	 * 
	 * @param message Message to be checked
	 * @return true if has profanity
	 */
	public static boolean hasProfanity(String message)
	{
		return !Collections.disjoint(Arrays.asList(message.split(" ")), nword);
	}
	
	@Override
	public void reload() {
		nword = getConfig().getStringList("profantity");
		Bukkit.getLogger().warning("New profanity list!");
		
		if (nword.toString().equals(Arrays.asList("test", "test1").toString()))
		{
			Bukkit.getLogger().warning("Profanity filter list is default! Please change.");
		}
	}


	@Override
	public void enable() {
		reload();
	}


	@Override
	public void disable() {}

}
