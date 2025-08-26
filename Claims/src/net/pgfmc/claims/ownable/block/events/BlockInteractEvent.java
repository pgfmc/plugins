package net.pgfmc.claims.ownable.block.events;

import java.util.EnumSet;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.projectiles.ProjectileSource;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.ClaimConfigInventory;
import net.pgfmc.claims.ownable.block.ClaimReadInventory;
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
		
		if (block.getType() == Material.LODESTONE) {

            if (
                    e.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS ||
                    e.getPlayer().isSneaking()
                ) {
                return;
            }

			Claim claim = ClaimsTable.getOwnable(new Vector4(block));
			if (claim != null) {
				e.setCancelled(true);

                if (claim.getAccess(pd) == Security.ADMIN || pd.getPlayer().getGameMode() == GameMode.CREATIVE) {
				    pd.getPlayer().openInventory(new ClaimConfigInventory(claim).getInventory());
                } else {
                    pd.getPlayer().openInventory(new ClaimReadInventory(claim).getInventory());
                }
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

		if (sharableInventories.contains(block.getType())) return;
        
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
    
    EnumSet<Material> switches = EnumSet.of(Material.LEVER, Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.BIRCH_BUTTON, Material.JUNGLE_BUTTON, Material.ACACIA_BUTTON, Material.DARK_OAK_BUTTON, Material.STONE_BUTTON, Material.WARPED_BUTTON, Material.CRIMSON_BUTTON, Material.MANGROVE_BUTTON, Material.CHERRY_BUTTON, Material.BAMBOO_BUTTON, Material.POLISHED_BLACKSTONE_BUTTON, Material.OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, Material.ACACIA_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE, Material.MANGROVE_PRESSURE_PLATE, Material.CHERRY_PRESSURE_PLATE, Material.BAMBOO_PRESSURE_PLATE, Material.WARPED_PRESSURE_PLATE, Material.CRIMSON_PRESSURE_PLATE, Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.PALE_OAK_BUTTON, Material.PALE_OAK_PRESSURE_PLATE, Material.TARGET);
    EnumSet<Material> doors = EnumSet.of(Material.OAK_DOOR, Material.OAK_TRAPDOOR, Material.SPRUCE_DOOR, Material.SPRUCE_TRAPDOOR, Material.BIRCH_DOOR, Material.BIRCH_TRAPDOOR, Material.JUNGLE_DOOR, Material.JUNGLE_TRAPDOOR, Material.ACACIA_DOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_DOOR, Material.DARK_OAK_TRAPDOOR, Material.MANGROVE_DOOR, Material.MANGROVE_TRAPDOOR, Material.CHERRY_DOOR, Material.CHERRY_TRAPDOOR, Material.BAMBOO_DOOR, Material.BAMBOO_TRAPDOOR, Material.WARPED_DOOR, Material.WARPED_TRAPDOOR, Material.CRIMSON_DOOR, Material.CRIMSON_TRAPDOOR, Material.OAK_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.MANGROVE_FENCE_GATE, Material.CHERRY_FENCE_GATE, Material.BAMBOO_FENCE_GATE, Material.WARPED_FENCE_GATE, Material.CRIMSON_FENCE_GATE, Material.PALE_OAK_FENCE_GATE, Material.WEATHERED_COPPER_DOOR, Material.WEATHERED_COPPER_TRAPDOOR, Material.COPPER_DOOR, Material.OXIDIZED_COPPER_DOOR, Material.EXPOSED_COPPER_DOOR, Material.COPPER_TRAPDOOR, Material.OXIDIZED_COPPER_TRAPDOOR, Material.EXPOSED_COPPER_TRAPDOOR, Material.WAXED_COPPER_DOOR, Material.WAXED_COPPER_TRAPDOOR, Material.WAXED_EXPOSED_COPPER_DOOR, Material.WAXED_EXPOSED_COPPER_TRAPDOOR, Material.WAXED_OXIDIZED_COPPER_DOOR, Material.WAXED_OXIDIZED_COPPER_TRAPDOOR, Material.WAXED_WEATHERED_COPPER_DOOR, Material.WAXED_WEATHERED_COPPER_TRAPDOOR, Material.PALE_OAK_DOOR, Material.PALE_OAK_TRAPDOOR);
	EnumSet<Material> inventories = EnumSet.of(Material.CHEST, Material.FURNACE, Material.SMOKER, Material.BLAST_FURNACE, Material.CAMPFIRE, Material.SOUL_CAMPFIRE, Material.COMPOSTER, Material.JUKEBOX, Material.BREWING_STAND, Material.CAULDRON, Material.BEE_NEST, Material.BEEHIVE, Material.CHISELED_BOOKSHELF, Material.TRAPPED_CHEST, Material.DROPPER, Material.DISPENSER, Material.HOPPER, Material.SHULKER_BOX);
	EnumSet<Material> sharableInventories = EnumSet.of(Material.ENDER_CHEST, Material.ENCHANTING_TABLE, Material.CRAFTING_TABLE, Material.GRINDSTONE, Material.STONECUTTER, Material.CARTOGRAPHY_TABLE, Material.FLETCHING_TABLE, Material.LOOM, Material.LECTERN, Material.SMITHING_TABLE);


    @EventHandler
    public void arrowHitTargetEvent(ProjectileHitEvent e ) {
        projectileHitPressurePlate(e, e.getHitBlock(), e.getEntity());
    }

    public void projectileHitPressurePlate(Cancellable e, Block b, Projectile p) {
        ProjectileSource shooter = p.getShooter();
        if (!(shooter instanceof OfflinePlayer)) {return;}
        PlayerData pd = PlayerData.from((OfflinePlayer) shooter);

        Claim claim = ClaimsTable.getClosestClaim(new Vector4(b), Range.PROTECTED);
        if (claim == null) {return;}
        if (!claim.switchesLocked) { return;}

        if (claim.getAccess(pd) == Security.BLOCKED) {
            e.setCancelled(true);
        }
    }

    //public void itemHitPressurePlate(Cancellable e, Block b, Item i) {
    //    if (b == null) {return;}
    //    if (!switches.contains(b.getType())) {return;}

    //    UUID thrower = i.getThrower();
    //    PlayerData pd = PlayerData.from(thrower);
    //    if (pd == null) {return;}

    //    Claim claim = ClaimsTable.getClosestClaim(new Vector4(b), Range.PROTECTED);
    //    if (claim == null) {return;}
    //    if (!claim.switchesLocked) { return;}

    //    if (claim.getAccess(pd) == Security.BLOCKED) {
    //        e.setCancelled(true);
    //    }
    //}

    @EventHandler
    public void entityInteract(EntityInteractEvent e) {
        Block b = e.getBlock();
        Entity entity = e.getEntity();
        if (entity instanceof Projectile) {
            projectileHitPressurePlate(e, b, (Projectile) entity);
            return;
        }

        //if (entity instanceof Item) {
        //    itemHitPressurePlate(e, b, (Item) entity);
        //}

        if (!(entity instanceof Tameable)) {return;}
        
        AnimalTamer at = ((Tameable) entity).getOwner();
        if (!(at instanceof OfflinePlayer)) {return;}

        PlayerData pd = PlayerData.from((OfflinePlayer) at);
        if (pd.getPlayer().getGameMode() != GameMode.SURVIVAL) {return;}

        Claim claim = ClaimsTable.getClosestClaim(new Vector4(b), Range.PROTECTED);
        Security security = claim.getAccess(pd);
        if (security != Security.BLOCKED) {return;}

        if (claim.switchesLocked && switches.contains(b.getType())) {
            e.setCancelled(true);
        }
    }

	@EventHandler
	public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent e)
	{
		final ArmorStand armorStand = e.getRightClicked();
    	final Player remover = e.getPlayer();
    	final PlayerData playerdata = PlayerData.from(remover);
    	final Claim claim = ClaimsTable.getClosestClaim(new Vector4(armorStand.getLocation()), Range.PROTECTED);
    	
    	if (claim == null) return;
    	
    	final Security access = claim.getAccess(playerdata);
    	
    	if (access != Security.BLOCKED) return;
    	
    	e.setCancelled(true);
    	
    	playerdata.sendMessage(ChatColor.RED + "This land is claimed!");
    	playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
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

	@EventHandler
	public void onPlayerTakeLecternBook(PlayerTakeLecternBookEvent e)
	{
		final Block block = e.getLectern().getBlock();
    	final Player remover = e.getPlayer();
    	final PlayerData playerdata = PlayerData.from(remover);
    	final Claim claim = ClaimsTable.getClosestClaim(new Vector4(block.getLocation()), Range.PROTECTED);
    	
    	if (claim == null) return;
    	
    	final Security access = claim.getAccess(playerdata);
    	
    	if (access != Security.BLOCKED) return;
    	
    	e.setCancelled(true);
    	
    	playerdata.sendMessage(ChatColor.RED + "This land is claimed!");
    	playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
	}

    
}
