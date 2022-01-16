package net.pgfmc.bot.functions;

import java.util.LinkedList;
import java.util.Random;

import net.dv8tion.jda.api.entities.User;
import net.pgfmc.bot.player.Roles;
import net.pgfmc.core.permissions.Permissions;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class AccountLinking {
	
	/**
	 * Link a Discord account to a Minecraft account
	 * 
	 * @param code The sent code from the user
	 * @param user The user who sent the code
	 */
	public static boolean linkAsk(String code, User user)
	{
		// when a Direct Message is sent to the bot.
		// 0000 because a shorter message will give error
		code = (code.strip() + "0000").substring(0, 4);
		System.out.println("Account linking: Input code is " + code);
		
		PlayerData codeMatch = null; // the playerdata that has the code match.
		boolean taken = false; // wether or not the discord account has been taken.
		
		for (PlayerData pd : PlayerData.getPlayerDataSet()) { // sets variables.
			
			if (pd.getData("linkCode") != null && pd.getData("linkCode").equals(code)) {
				pd.setData("linkCode", null);
				codeMatch = pd;
				System.out.println("Account linking: Found a match");
			}
			
			if (!taken) {
				taken = (pd.getData("Discord") != null && pd.getData("Discord").equals(user.getId()));
			}
		}
		
		// Link the account
		if (codeMatch != null && !taken) {
			System.out.println("Account linking: Successfully linked (Minecraft)" + codeMatch.getName() + " and (Discord)" + user.getName());
			link(codeMatch, user.getId());
			codeMatch.sendMessage("§aYour roles have been updated!");
			return true;
		}
		
		return false;
	}
	
	/**
	 * Manually link a player to a Discord
	 * 
	 * @param p Minecraft Player to link
	 * @param id Discord user ID to link
	 */
	public static void link(PlayerData pd, String id)
	{
		pd.setData("Discord", id).save();
		Roles.recalculateRoles(pd);
		Permissions.recalcPerms(pd);
	}
	
	/**
	 * Get a randomly generated 4 digit code that isn't taken
	 * This should only be accessed through a thread!
	 * @return The 4 digit unique code
	 */
	public static String generateCode()
	{
		String code = String.valueOf(new Random().nextInt(9999 - 1000) + 1000);
		System.out.println("Generating code: Code is " + code);
		// range is 1000 to 9999
		LinkedList<String> takenCodes = new LinkedList<>();
		
		for (PlayerData pd : PlayerData.getPlayerDataSet())
		{
			String tempCode = pd.getData("linkCode");
			if (tempCode == null || tempCode == "") continue;
			takenCodes.add(tempCode);
		}
		System.out.println("Generating code: Taken codes are " + takenCodes);
		
		while (takenCodes.contains(code))
		{
			System.out.println("Generating code: Code taken, generating new code");
			code = String.valueOf(new Random().nextInt(9999 - 1000) + 1000);
			System.out.println("Generating code: Code is " + code);
		}
		
		return code;
	}

}
