package net.pgfmc.survival.particleeffects;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.Main;

public abstract class HaloParticleEffect extends ParticleEffect {
	
	// The radius of the halo
	private final int HALO_RADIUS = 1;
	
	//private final Particle[] PARTICLES = {Particle.HEART, Particle.NOTE, Particle.SMALL_FLAME};
	
	public HaloParticleEffect(final Particle particle)
	{
		super(particle);
	}
	
	public void animate()
	{
		// Synchronous Bukkit Task
		Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable() {
			
			// A set of PlayerData of players who should have this particle effect
			// getData("particle_effect") should match "halo_" + getParticle()
			final Set<PlayerData> PLAYERDATAS = PlayerData.getPlayerDataSet(playerdata -> playerdata.getData("particle_effect").equals("halo_" + getParticle().name()));
			
			// Used in cos and sin functions for making a circle (halo)
			int count = 0;

			@Override
			public void run() {
				
				// For each PlayerData in PLAYERDATAS
				// Spawn the particle in the correct place
				for (final PlayerData PLAYERDATA : PLAYERDATAS)
				{
					// Constants
					final World WORLD = PLAYERDATA.getPlayer().getWorld();
					final Location LOCATION = PLAYERDATA.getPlayer().getLocation();
					final double X = LOCATION.getX();
					final double Y = LOCATION.getY();
					final double Z = LOCATION.getZ();
					
					// Counter clockwise spin with HALO_RADIUS radius
					// If you think in terms of an (x, y) graph, (cos(count), sin(count)) wil
					// draw a circle in a counter clockwise motion as count increases
					// Use Desmos to get a good visualization
					WORLD.spawnParticle(getParticle(), X + Math.cos(count) * HALO_RADIUS, Y + 1.5, Z + Math.sin(count) * HALO_RADIUS, 1);
					
					// If count is greater than an arbitrary multiple of 2pi, then reset it to 0
					// This is to avoid overflow
					// The value is 2pi because that is the value of the end of a unit circle in radians
					// The 1000 is just to make the number larger, because we don't need to reset count every 2pi, every 1000 2pi is okay (I hope)
					count = (count >= (1000*2*Math.PI)) ? 0 : count + 1;
					
				}
				
			}
		
		}, 0, 5); // Start the task after 0 ticks, repeat every 5 ticks
		
	}

}
