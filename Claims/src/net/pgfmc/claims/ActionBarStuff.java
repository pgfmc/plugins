package net.pgfmc.claims;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

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
			
			ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
			PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SET_ACTION_BAR_TEXT);
			
			String ting = " ";

            int enterTimer = playerData.getData("enterTimer");

            if (enterTimer > 0) {
                playerData.setData("enterTimer", enterTimer - 1);
                PlayerData claimOwner = playerData.getData("claimOwner");
                ting = ChatColor.GOLD + "Entering " + claimOwner.getRankedName() + ChatColor.RESET + ChatColor.GOLD + "'s Claim!";
            } 

            else if (block != null && block.getType() == Material.LODESTONE) {
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
			
			packet.getChatComponents().write(0, WrappedChatComponent.fromText(ting));
			protocolManager.sendServerPacket(player, packet);
		}
	}

    private static int getEnterTimer(PlayerData pd) {
        return pd.getData("enterTimer");
    }

    private static PlayerData getClaimInsideOwner(PlayerData pd) {
        return pd.getData("claimOwner");
    }
}
