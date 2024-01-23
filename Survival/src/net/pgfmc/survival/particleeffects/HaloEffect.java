package net.pgfmc.survival.particleeffects;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.Main;

public class HaloEffect {
	
	private boolean hasBeenInitiated = false;
	
	// The default radius and amount of the halo
	private double halo_radius = 0.5;
	private double number_particles_per_cycle = 7.0;
	
	public enum HaloParticle {
		CHERRY(Particle.CHERRY_LEAVES),
		HEART(Particle.HEART),
		NOTE(Particle.NOTE);
		
		private Particle particle;
		
		HaloParticle(Particle particle)
		{
			this.particle = particle;
			
		}
		
		public static HaloParticle fromString(String name)
		{
			if (name == null) return null;
			
			if (name.contains("halo_"))
			{
				name = name.substring(5).toUpperCase();
				
			}
			
			for (final HaloParticle particle: HaloParticle.values())
			{
				if (name.equals(particle.name())) return particle;
				
			}
			
			return null;			
		}
		
		public Particle getParticle()
		{
			return particle;
		}
		
		@Override
		public String toString()
		{
			return "halo_" + name().toLowerCase();
		}
		
	}
	
	public HaloEffect()
	{
		if (hasBeenInitiated) return;
		
		hasBeenInitiated = true;
		
		animate();
		
	}
	
	private void animate()
	{
		for (final HaloParticle particle : HaloParticle.values())
		{
			// Synchronous Bukkit Task
			Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable() {
				
				// Used in cos and sin functions for making a circle (halo)
				double count = 0.0;

				@Override
				public void run() {
					
					Set<PlayerData> playerdatas = PlayerData.getPlayerDataSet(playerdata -> playerdata.isOnline()
																							&& playerdata.getData("particle_effect") != null
																							&& playerdata.getData("particle_effect").equals(particle.toString()));
					
					// For each PlayerData in PLAYERDATAS
					// Spawn the particle in the correct place
					for (final PlayerData playerdata : playerdatas)
					{
						// Constants
						final World world = playerdata.getPlayer().getWorld();
						final Location location = playerdata.getPlayer().getLocation();
						final double x = location.getX();
						final double y = location.getY();
						final double z = location.getZ();
						
						// Counter clockwise spin with HALO_RADIUS radius
						// If you think in terms of an (x, y) graph, (cos(count), sin(count)) wil
						// draw a circle in a counter clockwise motion as count increases
						// Use Desmos to get a good visualization
						world.spawnParticle(particle.getParticle(), x + (Math.cos(count) * halo_radius), y + 2.5, z + (Math.sin(count) * halo_radius), 1);
						
						// Increment count by a fraction of PI
						// Loop back around once we hit 2PI (2PI = full circle)
						count = ((count >= 2 * Math.PI) ? (Math.PI / number_particles_per_cycle) : (count + (Math.PI / number_particles_per_cycle)));
						
					}
					
				}
			
			}, 0, 3); // Start the task after 0 ticks, repeat every 2 ticks
			
		}
		
	}

}