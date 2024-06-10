package net.pgfmc.claims.ownable.border;

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;

import net.pgfmc.claims.Main;
import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.vector4.Vector4;

public class Particles {

     private enum BorderColor {
         MERGE(Color.YELLOW, 14),
         CLAIMPLACEPREVENTION(Color.RED, 21),
         PROTECTEDNONMEMBER(Color.RED, 7),
         PROTECTEDMEMBER(Color.AQUA, 7),
         PROTECTEDADMIN(Color.BLUE, 7);

         public Color color;
         public double renderDistance;
         private BorderColor(Color color, double renderDistance) {
             this.color = color;
             this.renderDistance = renderDistance;
         }
     }

    public Particles() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(Main.plugin, () -> {

            for (OfflinePlayer offlinePlayer : Bukkit.getServer().getOfflinePlayers()) {
                if (!offlinePlayer.isOnline()) { continue; }
                Player op = (Player) offlinePlayer;
                PlayerData playerData = PlayerData.from(op);
                Vector4 playerLocation = new Vector4(op.getLocation());

                PlayerInventory playerInventory = op.getInventory();
                if (playerInventory.getItemInMainHand().getType() == Material.LODESTONE) {

                    Set<Claim> merge = ClaimsTable.getNearbyClaims(playerLocation, Range.MERGERENDER).stream().filter((x) -> {
                        Security security = x.getAccess(playerData);
                        return (security == Security.ADMIN || security == Security.MEMBER);
                    }).collect(Collectors.toSet());

                    Set<Claim> foreign = ClaimsTable.getNearbyClaims(playerLocation, Range.FOREIGNRENDER).stream().filter((x) -> {
                        return (x.getAccess(playerData) == Security.BLOCKED);
                    }).collect(Collectors.toSet());

                    for (Claim claim : merge) {
                        Particles.renderBorder(op, playerLocation, claim, BorderColor.MERGE, Range.MERGE);
                    }

                    for (Claim claim : foreign) {
                        Particles.renderBorder(op, playerLocation, claim, BorderColor.CLAIMPLACEPREVENTION, Range.FOREIGN);
                    }
                } else {
                    Set<Claim> claims = ClaimsTable.getNearbyClaims(playerLocation, Range.PROTECTRENDER);

                    for (Claim claim : claims) {
                        Security security = claim.getAccess(playerData);
                        switch (security) {
                            case ADMIN: 
                                Particles.renderBorder(op, playerLocation, claim, BorderColor.PROTECTEDADMIN, Range.PROTECTED);
                                break;
                            case MEMBER: 
                                Particles.renderBorder(op, playerLocation, claim, BorderColor.PROTECTEDMEMBER, Range.PROTECTED);
                                break;
                            case BLOCKED: 
                                Particles.renderBorder(op, playerLocation, claim, BorderColor.PROTECTEDNONMEMBER, Range.PROTECTED);
                                break;
                        }
                    }
                }
            }

        }, 0, 5);
    }

    private static void renderBorder(Player player, Vector4 playerLocation, Claim claim, BorderColor borderColor, Range range) {

        Vector4 loc = claim.getLocation();
        int r = range.getRange();

        boolean inside = ClaimsLogic.isInRange(claim, playerLocation, range);

        int x = -r;
        while (x <= r + 1) {
            trySpawnParticle(player, loc.x() + x, loc.z() + r + 1, playerLocation, borderColor, inside);
            trySpawnParticle(player, loc.x() + x, loc.z() - r, playerLocation, borderColor, inside);
            x += 1;
        }

        int z = -r + 1;
        while (z <= r) {
            trySpawnParticle(player, loc.x() + r + 1, loc.z() + z, playerLocation, borderColor, inside);
            trySpawnParticle(player, loc.x() - r, loc.z() + z, playerLocation, borderColor, inside);
            z += 1;
        }
    }


    private static void trySpawnParticle(Player player, int particlex, int particlez, Vector4 playerLocation, BorderColor borderColor, boolean inside) {

        float particleSize = 1f;
        if (inside) {
            particleSize = 0.5f;
        }
        double particleRender = borderColor.renderDistance;

        Location particleLocation = new Vector4(particlex, playerLocation.y() + 1, particlez, playerLocation.w()).toLocation();

        double distance = player.getLocation().distanceSquared(particleLocation);


        if (distance < particleRender * particleRender && distance > 0) {
            player.spawnParticle(Particle.DUST, particleLocation, 1, new Particle.DustOptions(borderColor.color, particleSize));

            double distanceInverse = (distance - particleRender * particleRender) / -particleRender;

            if (distanceInverse < 1) { return; }

            double counter = 0;
            while (counter <= distanceInverse) {
                Location upper = particleLocation.clone();
                upper.setY(particleLocation.getY() + counter);
                Location lower = particleLocation.clone();
                lower.setY(particleLocation.getY() - counter);

                player.spawnParticle(Particle.DUST, upper, 1, new Particle.DustOptions(borderColor.color, particleSize));
                player.spawnParticle(Particle.DUST, lower, 1, new Particle.DustOptions(borderColor.color, particleSize));
                counter += 1;
            }
        }
    }
}
