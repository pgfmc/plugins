package net.pgfmc.claims;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

            int enterTimer = (int) Optional.ofNullable(playerData.getData("enterTimer")).orElse(0);
            PlayerData claimOwner = playerData.getData("claimOwner");

            if (enterTimer > 0 && claimOwner != null) {
                playerData.setData("enterTimer", enterTimer - 1);
                player.sendActionBar(
                            Component.text()
                                .append(Component.text("Entering ", NamedTextColor.GOLD))
                                .append(claimOwner.getRankedName())
                                .append(Component.text("'s Claim!", NamedTextColor.GOLD)).build());
                return;
                
            } else if (block != null && block.getType() == Material.LODESTONE) {
				Claim claim = ClaimsTable.getOwnable(new Vector4(block));
				if (claim != null) {
					
					if (claim.getPlayer() == null) {
                        player.sendActionBar(Component.text("Creative Claim", NamedTextColor.LIGHT_PURPLE));
					} else {
                        player.sendActionBar(
                                Component.text()
                                    .append(Component.text("Claimed by ", NamedTextColor.GRAY))
                                    .append(claim.getPlayer().getRankedName()).build());
                        return;
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
                        player.sendActionBar(Component.text("Claims a 61x61 block area", NamedTextColor.GREEN));
					} else {
						
						Security access = foreigner.getAccess(playerData);
						if (foreigner != null && (access == Security.ADMIN)) {
                            player.sendActionBar(Component.text("Claims a 61x61 block area", NamedTextColor.GREEN));
                            return;
						} else {
                            player.sendActionBar(Component.text("Cannot place claim here", NamedTextColor.RED));
                            return;
						}
					}
				} else {
					Security access = merger.getAccess(playerData);
					
				    if (access == Security.ADMIN) {
                        player.sendActionBar(Component.text("Merge with nearby claim", NamedTextColor.GOLD));
                        return;
					} else {
                        player.sendActionBar(Component.text("Cannot place claim here", NamedTextColor.RED));
                        return;
					}
				}
			}
		}
	}
}
