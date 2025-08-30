package net.pgfmc.claims.ownable.block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.core.util.vector4.Vector4;

public class ClaimConfigInventory extends BaseInventory {
	
	public ClaimConfigInventory(Claim claim, boolean read) {
        super(27, (read) ? "Claim View" : "Claim Settings");

        int membersList = 10;
        int addPlayer = 11;
        int info = 13;
        int explosions = 15;
        int foreignPolicy = 16;
		
		setItem(membersList, Material.BOOK).n(ChatColor.GRAY + "Members");
		setAction(membersList, (p, e) -> {
			p.openInventory(new PlayerViewInventory(claim, read).getInventory());
		});
		
        if (!read) {
		    setItem(addPlayer, Material.PLAYER_HEAD).n(ChatColor.GRAY + "Add member");
		    setAction(addPlayer, (p,e) -> {
		    	p.openInventory(new PlayerAddInventory(claim).getInventory());
		    });
        }

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
            cvi.setBack(0, new ClaimConfigInventory(claim, read).getInventory());
            p.openInventory(cvi.getInventory());
        });

        if (claim.explosionsEnabled) {
            setItem(explosions, Material.TNT).n(ChatColor.RED + "Allow Explosions? " + ChatColor.GRAY + "(" + ChatColor.GREEN + "yes" + ChatColor.GRAY + ")");
        } else {
            setItem(explosions, Material.WATER_BUCKET).n(ChatColor.RED + "Allow Explosions? " + ChatColor.GRAY + "(" + ChatColor.RED + "no" + ChatColor.GRAY + ")");
        }

        if (!read) {
            setAction(explosions, (p,e) -> {
                claim.explosionsEnabled = !claim.explosionsEnabled;
                claim.forwardUpdateFrom(claim);
                p.openInventory(new ClaimConfigInventory(claim, read).getInventory());
            });
        }

        setItem(foreignPolicy, Material.CREEPER_HEAD).n(ChatColor.YELLOW + "Foreign Policy").l(ChatColor.GRAY + "Control what non-members are\nallowed to do in your claim!");
        setAction(foreignPolicy, (p,e) -> {
            p.openInventory(new ForeignPolicyInventory(claim, read).getInventory());
        });
	}
	
	public static class PlayerViewInventory extends ListInventory<PlayerData> {
		
		Claim claim;
        boolean read;

		public PlayerViewInventory(Claim claim, boolean read) {
			super(27, "Allowed Players");
			this.claim = claim;
            this.read = read;
			
			setBack(0, new ClaimConfigInventory(claim, read).getInventory());
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

            return (this.read) ? 

                Butto.defaultButto 
                :
			    (p, e) -> {
			    	p.openInventory(new RemovePlayerInventory(claim, arg0).getInventory());
			    };
		}

		@Override
		protected ItemStack toItem(PlayerData arg0) {
			 
			// copied from cmd.skull lol
			
            ItemStack item = new ItemWrapper(Material.PLAYER_HEAD).n(arg0.getRankedName()).gi();
			SkullMeta meta = (SkullMeta) item.getItemMeta(); // Get the created item's ItemMeta and cast it to SkullMeta so we can access the skull properties
			meta.setOwningPlayer(arg0.getOfflinePlayer()); // Set the skull's owner so it will adapt the skin of the provided username (case sensitive).
			item.setItemMeta(meta); // Apply the modified meta to the initial created item
			
			return item;
		}
	}

	public static class PlayerAddInventory extends ListInventory<PlayerData> {
		
		Claim claim;

		public PlayerAddInventory(Claim claim) {
			super(27, "Add Players");
			this.claim = claim;
			
			setBack(0, new ClaimConfigInventory(claim, false).getInventory());
		}

		@Override
		protected List<PlayerData> load() {
			
			List<PlayerData> list = new ArrayList<>();
			
			if (claim == null) {
				return list;
			}
			for (PlayerData pd : PlayerData.getPlayerDataSet()) {
				if (claim.getMembers().contains(pd) || pd == claim.getPlayer()) continue;
				list.add(pd);
			}
			
			return list;
		}

		@Override
		protected Butto toAction(PlayerData arg0) {
			return (p, e) -> {
				p.openInventory(new AddPlayerConfirm(claim, arg0).getInventory());
			};
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
	
	public static class AddPlayerConfirm extends ConfirmInventory {
		
		Claim claim;
		PlayerData player;
		
		public AddPlayerConfirm(Claim claim, PlayerData player) {
			super("Add " + player.getDisplayName() + "?", "Add Player", "Cancel");
			this.claim = claim;
			this.player = player;
			
		}

		@Override
		protected void cancelAction(Player arg0, InventoryClickEvent arg1) {
			arg0.openInventory(new PlayerAddInventory(claim).getInventory());
		}

		@Override
		protected void confirmAction(Player arg0, InventoryClickEvent arg1) {
			claim.getMembers().add(player);
			arg0.closeInventory();
			arg0.sendMessage(ChatColor.GOLD + "Added " + player.getRankedName() + ChatColor.GOLD + " to your base.");
			claim.forwardUpdateFrom(claim);
		}
	}
	
	public static class RemovePlayerInventory extends ConfirmInventory {
		
		Claim claim;
		PlayerData player;
		
		public RemovePlayerInventory(Claim claim, PlayerData player) {
			super("Remove " + player.getDisplayName() + "?", "Remove Player", "Cancel");
			this.claim = claim;
			this.player = player;
		}

		@Override
		protected void cancelAction(Player arg0, InventoryClickEvent arg1) {
			arg0.openInventory(new PlayerViewInventory(claim, false).getInventory());
		}

		@Override
		protected void confirmAction(Player arg0, InventoryClickEvent arg1) {
			claim.getMembers().remove(player);
			arg0.closeInventory();
			arg0.sendMessage(ChatColor.GOLD + "Removed " + player.getRankedName() + ChatColor.GOLD + " from your base.");
			claim.forwardUpdateFrom(claim);
		}
	}
}
