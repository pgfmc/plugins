package net.pgfmc.core.chat;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.file.Mixins;

public class Profanity {
	
	private static final List<String> BAD_WORDS_LIST = Mixins.getDatabase(CoreMain.plugin.getDataFolder() + File.separator + "profanity.yml").getStringList("profanity");

	public static List<String> getBadWords()
	{
		return BAD_WORDS_LIST;
	}
	
	public static boolean hasProfanity(String context)
	{
		return !Collections.disjoint(Arrays.asList(context.split(" ")), BAD_WORDS_LIST);
	}

}
