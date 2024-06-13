package net.pgfmc.claims;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.vector4.Vector4;

public class ActionBarStuff extends BukkitRunnable {
	
	@Override
	public void run() {
	
		for (Player player : Main.plugin.getServer().getOnlinePlayers()) {
			
			Block block = player.getTargetBlock(null, 4);
            PlayerData playerData = PlayerData.from(player);

			
			String ting = " ";

            int enterTimer = (int) Optional.ofNullable(playerData.getData("enterTimer")).orElse(0);
            PlayerData claimOwner = playerData.getData("claimOwner");

            if (enterTimer > 0 && claimOwner != null) {
                playerData.setData("enterTimer", enterTimer - 1);
                ting = ChatColor.GOLD + "Entering " + claimOwner.getRankedName() + ChatColor.GOLD + "'s Claim!";
                
            } else if (block != null && block.getType() == Material.LODESTONE) {
				Claim claim = ClaimsTable.getOwnable(new Vector4(block));
				if (claim != null) {
					
					if (claim.getPlayer() == null) {
						ting = ChatColor.LIGHT_PURPLE + "Creative Claim";
					} else {
						ting = ChatColor.GRAY + "Claimed by " + claim.getPlayer().getRankedName();
					}
				}
				
			} else if (player.getInventory().getItemInMainHand().getType() == Material.LODESTONE) {
				
				Vector4 calcPos = (block != null && block.getType() != Material.AIR) ?
						new Vector4(block):
						new Vector4(player.getLocation());
				
				Claim merger = ClaimsTable.getClosestClaim(calcPos, Range.MERGE);
				
				if (merger == null) {
					Claim foreigner = ClaimsTable.getClosestClaim(calcPos, Range.FOREIGN);
					
					if (foreigner == null) {
						ting = ChatColor.GREEN + "Claims a 61x61 block area";
					} else {
						
						Security access = foreigner.getAccess(playerData);
						if (foreigner != null && (access == Security.MEMBER || access == Security.ADMIN)) {
							ting = ChatColor.GREEN + "Claims a 61x61 block area";
						} else {
							ting = ChatColor.RED + "Cannot place claim here";
						}
					}
				} else {
					Security access = merger.getAccess(playerData);
					
					if ((access == Security.MEMBER )) {
						ting = ChatColor.GOLD + "Merge with " + merger.getPlayer().getRankedName() + "'s " + ChatColor.GOLD + "claim" ;
					} else if (access == Security.ADMIN) {
						ting = ChatColor.GOLD + "Merge with nearby claim";
					} else {
						ting = ChatColor.RED + "Cannot place claim here";
					}
				}
			}

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ting));
		}
	}

    private static int getEnterTimer(PlayerData pd) {
        return pd.getData("enterTimer");
    }

    private static PlayerData getClaimInsideOwner(PlayerData pd) {
        return pd.getData("claimOwner");
    }
}
