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
	
	EnumSet<EntityType> protection = EnumSet.of(EntityType.ALLAY, EntityType.ARMOR_STAND, EntityType.AXOLOTL, EntityType.BAT, EntityType.BEE, EntityType.BOAT, EntityType.CAT, 
			EntityType.CHEST_BOAT, EntityType.CHICKEN, EntityType.COD, EntityType.COW, EntityType.LEASH_HITCH, EntityType.DOLPHIN, EntityType.DONKEY, EntityType.FOX, EntityType.FROG, 
			EntityType.GLOW_ITEM_FRAME, EntityType.GLOW_SQUID, EntityType.GOAT, EntityType.HORSE, EntityType.IRON_GOLEM, EntityType.ITEM_FRAME, EntityType.LLAMA, EntityType.MINECART, 
			EntityType.MINECART_CHEST, EntityType.MINECART_COMMAND, EntityType.MINECART_FURNACE, EntityType.MINECART_HOPPER, EntityType.MINECART_MOB_SPAWNER, EntityType.MINECART_TNT, 
			EntityType.MULE, EntityType.MUSHROOM_COW, EntityType.OCELOT, EntityType.PAINTING, EntityType.PANDA, EntityType.PARROT, EntityType.PIG, EntityType.POLAR_BEAR, EntityType.PUFFERFISH, 
			EntityType.RABBIT, EntityType.SALMON, EntityType.SHEEP, EntityType.SKELETON_HORSE, EntityType.SNOWMAN, EntityType.SQUID, EntityType.STRIDER, EntityType.TADPOLE, 
			EntityType.TRADER_LLAMA, EntityType.WANDERING_TRADER, EntityType.TROPICAL_FISH, EntityType.TURTLE, EntityType.VILLAGER, EntityType.WOLF);
	
	private void doStuff(EntityDamageByEntityEvent e, PlayerData pd, Entity entity) {
		
		
		if (entity.getCustomName() != null || protection.contains(entity.getType())) {
			
			Claim claim = ClaimsTable.getClosestClaim(new Vector4(entity.getLocation()), Range.PROTECTED);
			if (claim == null) return; 
			Security access = claim.getAccess(pd);
			
			if (access == Security.BLOCKED) {
				pd.sendMessage(ChatColor.RED + "This land is claimed!");
				e.setCancelled(true);
				return;
			}
		}
	}
}