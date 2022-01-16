package net.pgfmc.core.teleportAPI;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.cmd.Goto;

/**
 * Teleport a living entity to a destination with a delay before
 * @author bk
 *
 */
public class TimedTeleport {
	Consumer<Void> act;
	
	public TimedTeleport(LivingEntity entity, Location dest, int count)
	{
		SafeLocation safe = new SafeLocation(dest);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if (safe.getThread().isAlive()) return;
				entity.teleport(safe.getLocation());
				if (act != null) { act.accept(null); }
				cancel();
			}
		}.runTaskTimer(CoreMain.plugin, 20 * count, 1);
	}
	public TimedTeleport(Player p, Location dest, int count, int protectTime)
	{
		SafeLocation safe = new SafeLocation(dest);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if (safe.getThread().isAlive()) return;
				SpawnProtect.TEMP_PROTECT(p, 20 * 2);
				p.teleport(safe.getLocation());
				if (act != null) { act.accept(null); }
				cancel();
			}
		}.runTaskTimer(CoreMain.plugin, 20 * count, 1);
	}
	public TimedTeleport(Player p, Location dest, int count, boolean logBack)
	{
		SafeLocation safe = new SafeLocation(dest);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if (safe.getThread().isAlive()) return;
				Goto.logBackLocation(p, p.getLocation());
				p.teleport(safe.getLocation());
				if (act != null) { act.accept(null); }
				cancel();
			}
		}.runTaskTimer(CoreMain.plugin, 20 * count, 1);
	}
	public TimedTeleport(Player p, Location dest, int count, int protectTime, boolean logBack)
	{
		SafeLocation safe = new SafeLocation(dest);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if (safe.getThread().isAlive()) return;
				SpawnProtect.TEMP_PROTECT(p, 20 * 2);
				Goto.logBackLocation(p, p.getLocation());
				p.teleport(safe.getLocation());
				if (act != null) { act.accept(null); }
				cancel();
			}
		}.runTaskTimer(CoreMain.plugin, 20 * count, 1);
	}
	public TimedTeleport(LivingEntity entity, LivingEntity dest, int count)
	{
		new TimedTeleport(entity, dest.getLocation(), count);
	}
	public TimedTeleport(Player p, LivingEntity dest, int count, int protectTime)
	{
		new TimedTeleport(p, dest.getLocation(), count, protectTime);
	}
	public TimedTeleport(Player p, LivingEntity dest, int count, boolean logBack)
	{
		new TimedTeleport(p, dest.getLocation(), count, logBack);
	}
	public TimedTeleport(Player p, LivingEntity dest, int count, int protectTime, boolean logBack)
	{
		new TimedTeleport(p, dest.getLocation(), count, protectTime, logBack);
	}
	
	public void setAct(Consumer<Void> act)
	{
		this.act = act;
	}
}
