package net.pgfmc.claims.ownable.block.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.vector4.Vector4;

public class PlayerMove implements Listener {

    @EventHandler
    public void playerMove(PlayerMoveEvent e) {

        PlayerData pd = PlayerData.from(e.getPlayer());
        Vector4 playerLocation = new Vector4(pd.getPlayer().getLocation()); 
        Claim claim = ClaimsTable.getClosestClaim(playerLocation, Range.PROTECTED);
        PlayerData claimOwner = pd.getData("claimOwner");

        if (claim == null || claim.getPlayer() == pd || claim.getMembers().contains(pd) || claim.getPlayer() == null) {
            pd.setData("claimOwner", null);
            return;
        }
        if (claim.getPlayer() == claimOwner) { return; }


        pd.setData("enterTimer", 20);
        pd.setData("claimOwner", claim.getPlayer());
    }
} 
