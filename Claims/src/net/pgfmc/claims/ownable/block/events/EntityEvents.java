package net.pgfmc.claims.ownable.block.events;

import java.util.EnumSet;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.vector4.Vector4;

public class EntityEvents implements Listener {
	
	@EventHandler
	public void testEvent(EntityDamageByEntityEvent e) {
		
		
		
		Entity entity = e.getEntity();
		
		if (entity instanceof Player) return;
		
		if (e.getDamager() instanceof Player) {
			
			Player p = (Player) e.getDamager();
			
			if (p.getGameMode() == GameMode.SURVIVAL) {
				doStuff(e, PlayerData.from(p), entity);
			}
		} else if (e.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) e.getDamager();
			if (proj.getShooter() instanceof OfflinePlayer) {
				
				Player p = (Player) proj.getShooter();
				
				if (p.getGameMode() == GameMode.SURVIVAL) {
					doStuff(e, PlayerData.from(p), entity);
				}
			}
		} else return;
	}

    public EnumSet<EntityType> animals = EnumSet.of(EntityType.GOAT, EntityType.HORSE, EntityType.ALLAY, EntityType.AXOLOTL, EntityType.BAT, EntityType.BEE, EntityType.CAMEL, EntityType.CAT, EntityType.CHICKEN, EntityType.COD, EntityType.COW, EntityType.DOLPHIN, EntityType.DONKEY, EntityType.FROG, EntityType.GLOW_SQUID, EntityType.GOAT, EntityType.IRON_GOLEM, EntityType.LLAMA, EntityType.MUSHROOM_COW, EntityType.MULE, EntityType.OCELOT, EntityType.PANDA, EntityType.PARROT, EntityType.PIG, EntityType.POLAR_BEAR, EntityType.PUFFERFISH, EntityType.RABBIT, EntityType.SALMON, EntityType.SHEEP, EntityType.SNIFFER, EntityType.SNOWMAN, EntityType.SQUID, EntityType.STRIDER, EntityType.TADPOLE, EntityType.TRADER_LLAMA, EntityType.TROPICAL_FISH, EntityType.TURTLE, EntityType.VILLAGER, EntityType.WANDERING_TRADER, EntityType.WOLF, EntityType.ZOMBIE_HORSE, EntityType.SKELETON_HORSE);
    public EnumSet<EntityType> inventory = EnumSet.of(EntityType.MINECART_CHEST, EntityType.MINECART_HOPPER, EntityType.ARMOR_STAND, EntityType.ITEM_FRAME);
    public EnumSet<EntityType> monsters = EnumSet.of(EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.HOGLIN, EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WARDEN, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.ZOGLIN, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN);
	
	private void doStuff(EntityDamageByEntityEvent e, PlayerData pd, Entity entity) {
		
		
		if (entity.getCustomName() != null || animals.contains(entity.getType())) {
			
			Claim claim = ClaimsTable.getClosestClaim(new Vector4(entity.getLocation()), Range.PROTECTED);
			if (claim == null) return; 
			Security access = claim.getAccess(pd);
			
			if (access == Security.BLOCKED && !claim.livestockKilling) {
				pd.sendMessage(ChatColor.RED + "This land is claimed!");
				e.setCancelled(true);
				return;
			}
		}
	}
}
