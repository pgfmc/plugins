package net.pgfmc.claims.ownable.border;

import java.util.HashSet;
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
import net.pgfmc.core.util.Logger;
import net.pgfmc.core.util.vector4.Vector4;

public class Particles {

    private static enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NW,
        NE,
        SW,
        SE
    }


    private static class BorderBlock {
        public int x;
        public int z;
        public BorderColor color;
        public Direction direction;

        public BorderBlock(int x, int z, BorderColor color, Direction direction) {
            this.x = x;
            this.z = z;
            this.color = color;
            this.direction = direction;
        }
    }

    private static enum Orientation {
        X,
        Z,
        CORNER;
    }

    private static class BorderParticle {
        public int x;
        public int z;
        public BorderColor color;
        public int hOverlap = 0;
        public int vOverlap = 0;

        public BorderParticle(int x, int z, BorderColor color, Orientation orientation) {
            this.x = x;
            this.z = z;
            this.color = color;

            switch(orientation) {
                case X:
                    hOverlap = 2;
                    break;
                case Z:
                    vOverlap = 2;
                    break;
                case CORNER:
                    hOverlap = 1;
                    vOverlap = 1;
                    break;
            }
        }

        public boolean add(BorderParticle other) {
            if (this.x == other.x && this.z == other.z) {
                this.color = this.color.getHigher(other.color);
                this.vOverlap += other.vOverlap;
                this.hOverlap += other.hOverlap; 

                Logger.log("x: " + String.valueOf(x) + ", z: " + String.valueOf(z) + ", v: " + String.valueOf(vOverlap) + ", h: " + String.valueOf(hOverlap));
                return true;
            }
            return false;
        }
    }

     private enum BorderColor {
         MERGE(Color.YELLOW, 14, 2),
         CLAIMPLACEPREVENTION(Color.RED, 21, 1),
         PROTECTEDNONMEMBER(Color.RED, 7, 3),
         PROTECTEDMEMBER(Color.AQUA, 7, 4),
         PROTECTEDADMIN(Color.BLUE, 7, 5);

         public Color color;
         public double renderDistance;
         public int priority;
         private BorderColor(Color color, double renderDistance, int priority) {
             this.color = color;
             this.renderDistance = renderDistance;
         }

         public BorderColor getHigher(BorderColor other) {
             if (this.priority >= other.priority) {
                 return this;
             } else {
                 return other;
             }
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

                    Set<BorderParticle> mergeParticles = new HashSet<BorderParticle>();
                    Set<BorderParticle> foreignParticles = new HashSet<BorderParticle>();

                    for (Claim claim : merge) {
                        renderBorder(playerLocation, claim, merge, BorderColor.MERGE, Range.MERGE, mergeParticles);
                        //Particles.renderBorder(op, playerLocation, claim, BorderColor.MERGE, Range.MERGE, mergeParticles);
                    }

                    for (Claim claim : foreign) {
                        renderBorder(playerLocation, claim, foreign, BorderColor.CLAIMPLACEPREVENTION, Range.FOREIGN, foreignParticles);
                        //Particles.renderBorder(op, playerLocation, claim, BorderColor.CLAIMPLACEPREVENTION, Range.FOREIGN, foreignParticles);
                    }

                    renderParticles(op, foreignParticles, playerLocation);
                    renderParticles(op, mergeParticles, playerLocation);

                } else {
                    Set<Claim> claims = ClaimsTable.getNearbyClaims(playerLocation, Range.PROTECTRENDER);
                    Set<BorderParticle> particles = new HashSet<BorderParticle>();

                    for (Claim claim : claims) {
                        Security security = claim.getAccess(playerData);
                        BorderColor color = BorderColor.PROTECTEDNONMEMBER;

                        switch (security) {
                            case ADMIN: 
                                color = BorderColor.PROTECTEDADMIN;
                                break;
                            case MEMBER: 
                                color = BorderColor.PROTECTEDMEMBER;
                                break;
                            case BLOCKED: 
                                color = BorderColor.PROTECTEDNONMEMBER;
                                break;
                        }
                        renderBorder(playerLocation, claim, claims, color, Range.PROTECTED, particles);
                    }
                    renderParticles(op, particles, playerLocation);
                }
            }

        }, 0, 5);
    }

    private static void renderBorder(Vector4 playerLocation, Claim claim, Set<Claim> claims, BorderColor color, Range range, Set<BorderParticle> particles) {
        Set<BorderBlock> blocks = new HashSet<BorderBlock>();
        addBlockBorderOutline(playerLocation, claim, claims, color, range, blocks);
        blockToParticles(blocks, particles);
        blockToDryParticles(blocks, particles);
    }

    private static void addBlockBorderOutline(Vector4 playerLocation, Claim claim, Set<Claim> claims, BorderColor color, Range range, Set<BorderBlock> blocks) {
        int r = range.getRange();
        Vector4 claimLocation = claim.getLocation();

        tryAddBlock(claimLocation.x() - r, claimLocation.z() + r, color, Direction.SW, playerLocation, claim, claims, range, blocks);
        tryAddBlock(claimLocation.x() - r, claimLocation.z() - r, color, Direction.NW, playerLocation, claim, claims, range, blocks);
        tryAddBlock(claimLocation.x() + r, claimLocation.z() + r, color, Direction.SE, playerLocation, claim, claims, range, blocks);
        tryAddBlock(claimLocation.x() + r, claimLocation.z() - r, color, Direction.NE, playerLocation, claim, claims, range, blocks);

        int x = -r + 1;
        while (x <= r-1) {
            tryAddBlock(claimLocation.x() + x, claimLocation.z() + r, color, Direction.SOUTH, playerLocation, claim, claims, range, blocks);
            tryAddBlock(claimLocation.x() + x, claimLocation.z() - r, color, Direction.NORTH, playerLocation, claim, claims, range, blocks);
            x += 1;
        }


        int z = -r + 1;
        while (z <= r-1) {
            tryAddBlock(claimLocation.x() + r, claimLocation.z() + z, color, Direction.EAST, playerLocation, claim, claims, range, blocks);
            tryAddBlock(claimLocation.x() - r, claimLocation.z() + z, color, Direction.WEST, playerLocation, claim, claims, range, blocks);
            z += 1;
        }
    }

    private static void tryAddBlock(int x, int z, BorderColor color, Direction direction, Vector4 playerLocation, Claim claim, Set<Claim> claims, Range range, Set<BorderBlock> blocks) {

        if (((x - playerLocation.x()) * (x - playerLocation.x()) + ((z - playerLocation.z()) * (z - playerLocation.z())) > (color.renderDistance) * (color.renderDistance) + 10)) { return;}

        for (Claim tryClaim : claims) {
            if (tryClaim == claim) { continue; }
            if (ClaimsLogic.isInRange(tryClaim, new Vector4(x, 0, z, playerLocation.w()), range)) {return;}
        }
        blocks.add(new BorderBlock(x, z, color, direction));
    }

    private static void blockToParticles(Set<BorderBlock> blocks, Set<BorderParticle> particles) {

        for (BorderBlock block : blocks) {
            Orientation orientation = Orientation.X;

            int x = block.x;
            int z = block.z;
            BorderColor color = block.color;

            switch(block.direction) {
                case NW:
                    tryAddParticle(x, z, color, Orientation.CORNER, particles);
                    break;
                case NORTH:
                    tryAddParticle(x, z, color, orientation, particles);
                    break;
                case SOUTH:
                    tryAddParticle(x, z + 1, color, orientation, particles);
                    break;
                case WEST:
                    tryAddParticle(x, z, color, Orientation.Z, particles);
                    break;
                case EAST:
                    tryAddParticle(x + 1, z, color, Orientation.Z, particles);
                    break;
                case NE:
                    tryAddParticle(x + 1, z, color, Orientation.CORNER, particles);
                    tryAddParticle(x, z, color, Orientation.X, particles);
                    break;
                case SE:
                    tryAddParticle(x + 1, z + 1, color, Orientation.CORNER, particles);
                    tryAddParticle(x + 1, z, color, Orientation.Z, particles);
                    tryAddParticle(x, z + 1, color, Orientation.X, particles);
                    break;
                case SW:
                    tryAddParticle(x, z + 1, color, Orientation.CORNER, particles);
                    tryAddParticle(x, z, color, Orientation.Z, particles);
                    break;
            }
        }
    }

    private static void blockToDryParticles(Set<BorderBlock> blocks, Set<BorderParticle> particles) {

        for (BorderBlock block : blocks) {

            int x = block.x;
            int z = block.z;
            BorderColor color = block.color;

            switch(block.direction) {
                case NORTH:
                    dryAddParticle(x + 1, z, color, Orientation.X, particles);
                    break;
                case SOUTH:
                    dryAddParticle(x + 1, z + 1, color, Orientation.X, particles);
                    break;
                case WEST:
                    dryAddParticle(x, z + 1, color, Orientation.Z, particles);
                    break;
                case EAST:
                    dryAddParticle(x + 1, z + 1, color, Orientation.Z, particles);
                    break;
                default:
                    break;
            }


        }
    }

    private static void dryAddParticle(int x, int z, BorderColor color, Orientation orientation, Set<BorderParticle> render) {

        for (BorderParticle p : render) {

            if (p.x == x && p.z == z) {
                return;
            }
        }
        BorderParticle particle = new BorderParticle(x, z, color, orientation);

        render.add(particle);

    }
    
    private static void tryAddParticle(int x, int z, BorderColor color, Orientation orientation, Set<BorderParticle> render) {

        BorderParticle particle = new BorderParticle(x, z, color, orientation);

        for (BorderParticle p : render) {
            if (p.add(particle)) {
                return;
            }
        }

        render.add(particle);
    }

    private static void renderParticles(Player player, Set<BorderParticle> particles, Vector4 playerLocation) {
        for (BorderParticle particle : particles) {
            if (particle.hOverlap < 4 && particle.vOverlap < 4) {
                trySpawnParticle(player, particle.x, particle.z, playerLocation, particle.color);
            }
            //player.sendMessage("X = " + String.valueOf(particle.x) + ", Z = " + String.valueOf(particle.z) + " | H = " + String.valueOf(particle.hOverlap) + ", V = " + String.valueOf(particle.vOverlap));
        }
    }

    private static void trySpawnParticle(Player player, int particlex, int particlez, Vector4 playerLocation, BorderColor borderColor) {

        float particleSize = 0.5f;
        double particleRender = borderColor.renderDistance;

        Location particleLocation = new Vector4(particlex, playerLocation.y() + 1, particlez, playerLocation.w()).toLocation();

        double distance = player.getLocation().distanceSquared(particleLocation);
        if (distance > borderColor.renderDistance * borderColor.renderDistance) {return;}

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
