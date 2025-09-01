package net.pgfmc.claims.ownable.border;

import java.util.EnumSet;
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
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
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

    private static class BorderParticle {
        public int x;
        public int z;
        public BorderColor color;

        public EnumSet<Direction> map = EnumSet.noneOf(Direction.class);

        public BorderParticle(int x, int z, BorderColor color, Direction direction) {
            this.x = x;
            this.z = z;
            this.color = color;
            map.add(direction);
        }

        public boolean add(BorderParticle other) {
            if (this.x == other.x && this.z == other.z) {
                this.color = this.color.getHigher(other.color);

                for (Direction direction : other.map) {
                    this.map.add(direction);
                }

                return true;
            }
            return false;
        }

        public boolean isRenderable() {


            if (map.containsAll(EnumSet.of(Direction.NORTH, Direction.SOUTH))) { return false; }
            if (map.containsAll(EnumSet.of(Direction.EAST, Direction.WEST))) { return false; }

            if (map.containsAll(EnumSet.of(Direction.NW, Direction.NE, Direction.SE, Direction.SW))) { return false; }

            if (map.containsAll(EnumSet.of(Direction.NORTH, Direction.SE, Direction.SW))) { return false; }
            if (map.containsAll(EnumSet.of(Direction.SOUTH, Direction.NW, Direction.NE))) { return false; }
            if (map.containsAll(EnumSet.of(Direction.WEST, Direction.NE, Direction.SE))) { return false; }
            if (map.containsAll(EnumSet.of(Direction.EAST, Direction.NW, Direction.SW))) { return false; }

            return true;

        }
    }

     private enum BorderColor {
         MERGEALIGN(Color.ORANGE, 14, 2),
         MERGE(Color.YELLOW, 14, 3),
         CLAIMPLACEPREVENTION(Color.RED, 21, 1),
         PROTECTEDNONMEMBER(Color.RED, 7, 4),
         PROTECTEDMEMBER(Color.AQUA, 7, 5),
         PROTECTEDADMIN(Color.BLUE, 7, 6);

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
                        return (security == Security.ADMIN);
                    }).collect(Collectors.toSet());

                    Set<Claim> foreign = ClaimsTable.getNearbyClaims(playerLocation, Range.FOREIGNRENDER).stream().filter((x) -> {
                        return (x.getAccess(playerData) == Security.BLOCKED || x.getAccess(playerData) == Security.MEMBER);
                    }).collect(Collectors.toSet());

                    Set<BorderParticle> mergeParticles = new HashSet<BorderParticle>();
                    Set<BorderParticle> foreignParticles = new HashSet<BorderParticle>();

                    for (Claim claim : merge) {
                        addParticles(playerLocation, claim, merge, BorderColor.MERGE, Range.MERGE, mergeParticles);
                        //cullParticles(mergeParticles, merge, Range.MERGE);
                        //Cull particles removed for merging claims, so you can make sure you're putting the claimstone perfectly inbetween two claims.
                        //renderBorder(playerLocation, claim, merge, BorderColor.MERGE, Range.MERGE, mergeParticles);
                        //Particles.renderBorder(op, playerLocation, claim, BorderColor.MERGE, Range.MERGE, mergeParticles);
                    }

                    for (Claim claim : foreign) {
                        addParticles(playerLocation, claim, foreign, BorderColor.CLAIMPLACEPREVENTION, Range.FOREIGN, foreignParticles);
                        cullParticles(foreignParticles, foreign, Range.FOREIGN);
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
                        addParticles(playerLocation, claim, claims, color, Range.PROTECTED, particles);
                        cullParticles(particles, claims, Range.PROTECTED);
                    }
                    renderParticles(op, particles, playerLocation);
                }
            }

        }, 0, 5);
    }

    private static void addParticles(Vector4 playerLocation, Claim claim, Set<Claim> claims, BorderColor color, Range range, Set<BorderParticle> particles) {

        int r = range.getRange();
        Vector4 claimLocation = claim.getLocation();

        tryAddParticle(claimLocation.x() - r, claimLocation.z() - r, color, Direction.NW, playerLocation, claim, claims, particles);
        tryAddParticle(claimLocation.x() + r + 1, claimLocation.z() - r, color, Direction.NE, playerLocation, claim, claims, particles);
        tryAddParticle(claimLocation.x() - r, claimLocation.z() + r + 1, color, Direction.SW, playerLocation, claim, claims, particles);
        tryAddParticle(claimLocation.x() + r + 1, claimLocation.z() + r + 1, color, Direction.SE, playerLocation, claim, claims, particles);


        if (color == BorderColor.MERGE) {
            int x = -r + 1;
            while (x <= r) {
                BorderColor tempcolor = BorderColor.MERGE;
                if (x == 0 || x == 1) {
                    tempcolor = BorderColor.MERGEALIGN;
                }

                tryAddParticle(claimLocation.x() + x, claimLocation.z() + r + 1, tempcolor, Direction.SOUTH, playerLocation, claim, claims, particles);
                tryAddParticle(claimLocation.x() + x, claimLocation.z() - r, tempcolor, Direction.NORTH, playerLocation, claim, claims, particles);
                x += 1;
            }

            int z = -r + 1;
            while (z <= r) {
                BorderColor tempcolor = BorderColor.MERGE;
                if (z == 0 || z == 1) {
                    tempcolor = BorderColor.MERGEALIGN;
                }

                tryAddParticle(claimLocation.x() + r + 1, claimLocation.z() + z, tempcolor, Direction.EAST, playerLocation, claim, claims, particles);
                tryAddParticle(claimLocation.x() - r, claimLocation.z() + z, tempcolor, Direction.WEST, playerLocation, claim, claims, particles);
                z += 1;
            }
            return;
        }

        int x = -r + 1;
        while (x <= r) {
            tryAddParticle(claimLocation.x() + x, claimLocation.z() + r + 1, color, Direction.SOUTH, playerLocation, claim, claims, particles);
            tryAddParticle(claimLocation.x() + x, claimLocation.z() - r, color, Direction.NORTH, playerLocation, claim, claims, particles);
            x += 1;
        }

        int z = -r + 1;
        while (z <= r) {
            tryAddParticle(claimLocation.x() + r + 1, claimLocation.z() + z, color, Direction.EAST, playerLocation, claim, claims, particles);
            tryAddParticle(claimLocation.x() - r, claimLocation.z() + z, color, Direction.WEST, playerLocation, claim, claims, particles);
            z += 1;
        }
    }
    
    private static void tryAddParticle(int x, int z, BorderColor color, Direction direction, Vector4 playerLocation, Claim claim, Set<Claim> claims, Set<BorderParticle> particles) {

        int distance = ((x - playerLocation.x()) ^ 2) + ((z - playerLocation.z()) ^ 2);
        if (distance > color.renderDistance * color.renderDistance) {return;}

        BorderParticle particle = new BorderParticle(x, z, color, direction);

        for (BorderParticle p : particles) {
            if (p.add(particle)) {
                return;
            }
        }

        particles.add(particle);
    }

    private static void cullParticles(Set<BorderParticle> particles, Set<Claim> claims, Range range) {

        int min = range.getRange() - 1;
        int max = range.getRange();

        for (Claim claim : claims) {
            int claimx = claim.getLocation().x();
            int claimz = claim.getLocation().z();

            particles.removeIf(p -> {
                return (p.x >= claimx - min && p.z >= claimz - min && p.x <= max + claimx && p.z <= max + claimz);
            });
        }
    }

    private static void renderParticles(Player player, Set<BorderParticle> particles, Vector4 playerLocation) {
        for (BorderParticle particle : particles) {
            if (particle.isRenderable()) {
                trySpawnParticle(player, particle.x, particle.z, playerLocation, particle.color);
            }
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
