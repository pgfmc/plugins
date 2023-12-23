package net.pgfmc.survival.particleeffects;

import org.bukkit.Particle;

public abstract class ParticleEffect {
	
	private Particle particle;
	
	public abstract void animate();
	
	public ParticleEffect(final Particle particle)
	{
		this.particle = particle;
	}
	
	public Particle getParticle()
	{
		return particle;
	}
	
	public void setParticle(final Particle particle)
	{
		this.particle = particle;
	}

}
