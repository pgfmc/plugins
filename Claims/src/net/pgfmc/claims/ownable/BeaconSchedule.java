package net.pgfmc.claims.ownable;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import net.pgfmc.claims.Main;
import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.util.vector4.Vector4;

public class BeaconSchedule extends BukkitRunnable {
    
    @Override
    public void run() {
		for (Player player : Main.plugin.getServer().getOnlinePlayers()) {

            Claim claim = ClaimsTable.getClosestClaim(new Vector4(player.getLocation()), Range.PROTECTED);
            player.addPotionEffects(claim.getBuffs());

        }
    }
}
