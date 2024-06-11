package net.pgfmc.claims.ownable.block.events;

import java.util.EnumSet;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.ClaimConfigInventory;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.vector4.Vector4;

/**
Written by CrimsonDart

-----------------------------------

Interact Event.

-----------------------------------
 */
public class BlockInteractEvent implements Listener {
	
	@EventHandler
	public void blockInteract(PlayerInteractEvent e) { // code block for right-clicking on a block.
		
		// controls clicking containers and beacons;
		
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.PHYSICAL) return;
		
		if (!e.hasBlock()) return;
	
		Block block = e.getClickedBlock();
		
		if (block != null && block.getType() == Material.LODESTONE) {
			Claim claim = ClaimsTable.getOwnable(new Vector4(block));
			if (claim != null && claim.getAccess(pd) == Security.ADMIN) {
				e.setCancelled(true);
				pd.getPlayer().openInventory(new ClaimConfigInventory(claim).getInventory());
			}
			
			return;
		}
		
		if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
		
		Claim claim = ClaimsTable.getClosestClaim(new Vector4(block), Range.PROTECTED);
		
		if (claim == null) return;
		
		Security access = claim.getAccess(pd);
		
		if (access != Security.BLOCKED) return;

        if ((!claim.inventoriesLocked && inventories.contains(block.getType())) ||
                (!claim.switchesLocked && switches.contains(block.getType())) || 
                (!claim.doorsLocked && doors.contains(block.getType())))
                    {
            return;
        }
        
        e.setCancelled(true);
		
		switch(block.getType()) {
		
		case BARREL: pd.sendMessage(ChatColor.RED + "This barrel is claimed!"); break;
		case BLAST_FURNACE: pd.sendMessage(ChatColor.RED + "This blast furnace is claimed!"); break;
		case BREWING_STAND: pd.sendMessage(ChatColor.RED + "This brewing stand is claimed!"); break;
		case CHEST: pd.sendMessage(ChatColor.RED + "This chest is claimed!"); break;
		case DISPENSER: pd.sendMessage(ChatColor.RED + "This dispenser is claimed!"); break;
		case DROPPER: pd.sendMessage(ChatColor.RED + "This dropper is claimed!"); break;
		case FURNACE: pd.sendMessage(ChatColor.RED + "This furnace is claimed!"); break;
		case HOPPER: pd.sendMessage(ChatColor.RED + "This hopper is claimed!"); break;
		case SHULKER_BOX: pd.sendMessage(ChatColor.RED + "This shulker box is claimed!"); break;
		case SMOKER: pd.sendMessage(ChatColor.RED + "This smoker is claimed!"); break;
		case BEACON: pd.sendMessage(ChatColor.RED + "This beacon is claimed!"); break;
		default:
			
			if (e.getMaterial() == Material.ITEM_FRAME) {
				pd.sendMessage(ChatColor.RED + "This land is claimed!");
				
			}
			
		}
		
	}
    
    EnumSet<Material> switches = EnumSet.of(Material.LEVER, Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.BIRCH_BUTTON, Material.JUNGLE_BUTTON, Material.ACACIA_BUTTON, Material.DARK_OAK_BUTTON, Material.STONE_BUTTON, Material.WARPED_BUTTON, Material.CRIMSON_BUTTON, Material.MANGROVE_BUTTON, Material.CHERRY_BUTTON, Material.BAMBOO_BUTTON, Material.POLISHED_BLACKSTONE_BUTTON, Material.OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, Material.ACACIA_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE, Material.MANGROVE_PRESSURE_PLATE, Material.CHERRY_PRESSURE_PLATE, Material.BAMBOO_PRESSURE_PLATE, Material.WARPED_PRESSURE_PLATE, Material.CRIMSON_PRESSURE_PLATE, Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
    EnumSet<Material> doors = EnumSet.of(Material.OAK_DOOR, Material.OAK_TRAPDOOR, Material.SPRUCE_DOOR, Material.SPRUCE_TRAPDOOR, Material.BIRCH_DOOR, Material.BIRCH_TRAPDOOR, Material.JUNGLE_DOOR, Material.JUNGLE_TRAPDOOR, Material.ACACIA_DOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_DOOR, Material.DARK_OAK_TRAPDOOR, Material.MANGROVE_DOOR, Material.MANGROVE_TRAPDOOR, Material.CHERRY_DOOR, Material.CHERRY_TRAPDOOR, Material.BAMBOO_DOOR, Material.BAMBOO_TRAPDOOR, Material.WARPED_DOOR, Material.WARPED_TRAPDOOR, Material.CRIMSON_DOOR, Material.CRIMSON_TRAPDOOR, Material.OAK_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.MANGROVE_FENCE_GATE, Material.CHERRY_FENCE_GATE, Material.BAMBOO_FENCE_GATE, Material.WARPED_FENCE_GATE, Material.CRIMSON_FENCE_GATE);
    EnumSet<Material> inventories = EnumSet.of(Material.CHEST, Material.FURNACE, Material.SMOKER, Material.BLAST_FURNACE, Material.CAMPFIRE, Material.SOUL_CAMPFIRE, Material.COMPOSTER, Material.JUKEBOX, Material.BREWING_STAND, Material.CAULDRON, Material.BEE_NEST, Material.BEEHIVE, Material.LECTERN, Material.CHISELED_BOOKSHELF, Material.TRAPPED_CHEST, Material.DROPPER, Material.DISPENSER, Material.HOPPER, Material.SHULKER_BOX);

    @EventHandler
    public void entityInteract(EntityInteractEvent e) {
        if (!(e.getEntity() instanceof Tameable)) {return;}
        
        AnimalTamer at = ((Tameable) e.getEntity()).getOwner();
        if (!(at instanceof OfflinePlayer)) {return;}

        PlayerData pd = PlayerData.from((OfflinePlayer) at);
        if (pd.getPlayer().getGameMode() != GameMode.SURVIVAL) {return;}

        Claim claim = ClaimsTable.getClosestClaim(new Vector4(e.getBlock()), Range.PROTECTED);
        Security security = claim.getAccess(pd);
        if (security != Security.BLOCKED) {return;}

        if (claim.switchesLocked && switches.contains(e.getBlock().getType())) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void hangingBreak(HangingBreakByEntityEvent e)
    {
    	final Hanging hanging = e.getEntity();
    	final Entity remover = e.getRemover();
    	
    	if (!(remover instanceof Player)) return;
    	
    	final Player playerRemover = (Player) remover;
    	final PlayerData playerdata = PlayerData.from(playerRemover);
    	
    	final Claim claim = ClaimsTable.getClosestClaim(new Vector4(hanging.getLocation()), Range.PROTECTED);
    	
    	if (claim == null) return;
    	
    	final Security access = claim.getAccess(playerdata);
    	
    	if (access != Security.BLOCKED) return;
    	
    	e.setCancelled(true);
    	
    	playerdata.sendMessage(ChatColor.RED + "This land is claimed!");
    	playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
    	
    }
    
}
