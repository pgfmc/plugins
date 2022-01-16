package net.pgfmc.core.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.Mixins;
import net.pgfmc.core.configify.Configify;

public class ProfanityFilter extends Configify {
	
	public ProfanityFilter() {
		super(Mixins.getFile(CoreMain.plugin.getDataFolder() + File.separator + "profanity.yml"));
		setDefaultValue("profantity", new ArrayList<String>(Arrays.asList("test", "test1")));
	}

	private static List<String> nword = new ArrayList<String>();
	
	@Override
	public void reload() {
		nword = getConfig().getStringList("profantity");
		System.out.println("New profanity list: " + nword);
	}
	
	
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

}
