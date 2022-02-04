package net.pgfmc.core.punish;

import java.util.Optional;

import net.pgfmc.core.playerdataAPI.PlayerData;

public interface Punish {
	
	/**
	 * Sets "mute" in the PlayerData
	 * Does not mute/unmute the player.
	 * 
	 * @param pd the PlayerData
	 * @param isMute what "mute" would be set to
	 * @return the PlayerData
	 */
	public static PlayerData setMute(PlayerData pd, boolean isMute)
	{
		pd.setData("punishment.mute", isMute);//.queue();
		return pd;
	}
	
	/**
	 * Checks if "mute" is true in the PlayerData
	 * 
	 * @param pd the PlayerData
	 * @return If "mute" is true
	 */
	public static boolean isMute(PlayerData pd)
	{
		return (boolean) Optional.ofNullable(pd.getData("punishment.mute")).orElse(false);
	}

}
