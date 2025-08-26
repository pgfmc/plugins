package net.pgfmc.claims.ownable.block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.vector4.Vector4;

public class ClaimReadInventory extends BaseInventory {

	public ClaimReadInventory(Claim claim) {
		super(27, "Claim View");

        int membersList = 10;
        int info = 13;
        int explosions = 15;
        int foreignPolicy = 16;
		
		setItem(membersList, Material.BOOK).n(ChatColor.GRAY + "Members");
		setAction(membersList, (p, e) -> {
			p.openInventory(new PlayerViewInventory(claim).getInventory());
		});

        Vector4 loc = claim.getLocation();

        String name = "";

        if (claim.getPlayer() == null) {
            name = ChatColor.LIGHT_PURPLE + "Creative"; 
        } else {
            name = ChatColor.WHITE + "Owned by " + claim.getPlayer().getRankedName() + ChatColor.RESET;
        }

        setItem(info, Material.PAPER).n(ChatColor.WHITE + "Claim Info").l(
                name + 
                ChatColor.GRAY + 
                "\nX " + String.valueOf(loc.x()) + 
                "\nY " + String.valueOf(loc.y()) +
                "\nZ " + String.valueOf(loc.z()) +
                ChatColor.ITALIC + ChatColor.WHITE + "\nClick to open list of " + ChatColor.GOLD + "Merged Claims" + ChatColor.WHITE + "!"
            );

        setAction(info, (p,e) -> {
            ClaimViewInventory cvi = new ClaimViewInventory(claim);
            cvi.setBack(0, new ClaimReadInventory(claim).getInventory());
            p.openInventory(cvi.getInventory());
        });

        if (claim.explosionsEnabled) {
            setItem(explosions, Material.TNT).n(ChatColor.RED + "Allow Explosions? " + ChatColor.GRAY + "(" + ChatColor.GREEN + "yes" + ChatColor.GRAY + ")");
        } else {
            setItem(explosions, Material.WATER_BUCKET).n(ChatColor.RED + "Allow Explosions? " + ChatColor.GRAY + "(" + ChatColor.RED + "no" + ChatColor.GRAY + ")");
        }

        setItem(foreignPolicy, Material.CREEPER_HEAD).n(ChatColor.YELLOW + "Foreign Policy").l(ChatColor.GRAY + "Control what non-members are\nallowed to do in your claim!");
        setAction(foreignPolicy, (p,e) -> {
            p.openInventory(new ForeignPolicyInventory(claim).getInventory());
        });
	}

	
	public static class PlayerViewInventory extends ListInventory<PlayerData> {
		
		Claim claim;

		public PlayerViewInventory(Claim claim) {
			super(27, "Members");
			this.claim = claim;
			
			setBack(0, new ClaimReadInventory(claim).getInventory());
			
		}

		@Override
		protected List<PlayerData> load() {
			
			List<PlayerData> list = new ArrayList<>();
			
			if (claim == null) {
				return list;
			}
			for (PlayerData pd : claim.getMembers()) {
				list.add(pd);
			}
			
			return list;
		}

		@Override
		protected Butto toAction(PlayerData arg0) {
            return Butto.defaultButto;
		}

		@Override
		protected ItemStack toItem(PlayerData arg0) {
			 
			// copied from cmd.skull lol
			
			ItemStack item = new ItemStack(Material.PLAYER_HEAD); // Create a new ItemStack of the Player Head type.
			SkullMeta meta = (SkullMeta) item.getItemMeta(); // Get the created item's ItemMeta and cast it to SkullMeta so we can access the skull properties
			meta.setOwningPlayer(arg0.getOfflinePlayer()); // Set the skull's owner so it will adapt the skin of the provided username (case sensitive).
			
			
			item.setItemMeta(meta); // Apply the modified meta to the initial created item
			
			return item;
		}
	}

    public static class ForeignPolicyInventory extends BaseInventory {
    
        public ForeignPolicyInventory(Claim claim) {
            super(27, ChatColor.YELLOW + "Settings for Foreign Players");
    
            setBack(0, new ClaimReadInventory(claim).getInventory());
            int door = 11;
            int lever = 12;
            int chest = 13;
            int enemy = 14;
            int animal = 15;
    
            if (claim.doorsLocked) {
                setItem(door, Material.IRON_DOOR).n(ChatColor.YELLOW + "Doors" + ChatColor.GRAY + ": " + ChatColor.RED + "Locked.");
            } else {
                setItem(door, Material.OAK_DOOR).n(ChatColor.YELLOW + "Doors" + ChatColor.GRAY + ": " + ChatColor.GREEN + "Unlocked.");
            }
    
            if (claim.switchesLocked) {
                setItem(lever, Material.HEAVY_WEIGHTED_PRESSURE_PLATE).n(ChatColor.YELLOW + "Switches" + ChatColor.GRAY + ": " + ChatColor.RED + "Locked.");
            } else {
                setItem(lever, Material.OAK_PRESSURE_PLATE).n(ChatColor.YELLOW + "Switches" + ChatColor.GRAY + ": " + ChatColor.GREEN + "Unlocked.");
            }
    
            if (claim.inventoriesLocked) {
                setItem(chest, Material.TRIPWIRE_HOOK).n(ChatColor.YELLOW + "Inventories" + ChatColor.GRAY + ": " + ChatColor.RED + "Locked.");
            } else {
                setItem(chest, Material.CHEST).n(ChatColor.YELLOW + "Inventories" + ChatColor.GRAY + ": " + ChatColor.GREEN + "Unlocked.");
            }
    
            if (claim.monsterKilling) {
                setItem(enemy, Material.ZOMBIE_HEAD).n(ChatColor.YELLOW + "Monster Interactions" + ChatColor.GRAY + ": " + ChatColor.RED + "Allowed.");
            } else {
                setItem(enemy, Material.SKELETON_SKULL).n(ChatColor.YELLOW + "Monster Interactions" + ChatColor.GRAY + ": " + ChatColor.GREEN + "Disallowed.");
            }
    
            if (claim.livestockKilling) {
                setItem(animal, Material.PORKCHOP).n(ChatColor.YELLOW + "Animal Interactions" + ChatColor.GRAY + ": " + ChatColor.RED + "Allowed.");
            } else {
                setItem(animal, Material.COOKED_PORKCHOP).n(ChatColor.YELLOW + "Animal Interactions" + ChatColor.GRAY + ": " + ChatColor.GREEN + "Disallowed.");
            }
        }
    }
}
