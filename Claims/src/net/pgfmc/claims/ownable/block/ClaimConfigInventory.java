package net.pgfmc.claims.ownable.block;

import java.util.ArrayList;
import java.util.List;

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

public class ClaimConfigInventory extends BaseInventory {
	
	public ClaimConfigInventory(Claim claim) {
		super(27, "Claim Settings");
		
		
		setItem(0, Material.FEATHER).n("§r§7Back");
		setAction(0, (p, e) -> {
			p.closeInventory();
		});
		
		setItem(11, Material.BOOK).n("§r§7Members");
		setAction(11, (p, e) -> {
			p.openInventory(new PlayerViewInventory(claim).getInventory());
		});
		
		setItem(15, Material.PLAYER_HEAD).n("§r§7Add member");
		setAction(15, (p,e) -> {
			p.openInventory(new PlayerAddInventory(claim).getInventory());
		});
	}
	
	public static class PlayerViewInventory extends ListInventory<PlayerData> {
		
		Claim claim;

		public PlayerViewInventory(Claim claim) {
			super(27, "Allowed Players");
			this.claim = claim;
			
			setItem(0, Material.FEATHER).n("§r§7Back");
			setAction(0, (p, e) -> {
				p.openInventory(new ClaimConfigInventory(claim).getInventory());
			});
			refresh();
			
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
			return (p, e) -> {
				p.openInventory(new RemovePlayerInventory(claim, arg0).getInventory());
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

	public static class PlayerAddInventory extends ListInventory<PlayerData> {
		
		Claim claim;

		public PlayerAddInventory(Claim claim) {
			super(27, "Add Players");
			this.claim = claim;
			
			setItem(0, Material.FEATHER).n("§r§7Back");
			setAction(0, (p, e) -> {
				p.openInventory(new ClaimConfigInventory(claim).getInventory());
			});
			refresh();
			
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
			super("Add " + player.getDisplayNameRaw() + "?", "Add Player", "Cancel");
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
			arg0.sendMessage("Added " + player.getRankedName() + " to your base.");
			claim.forwardUpdateFrom(claim, null);
		}
	}
	
	public static class RemovePlayerInventory extends ConfirmInventory {
		
		Claim claim;
		PlayerData player;
		
		public RemovePlayerInventory(Claim claim, PlayerData player) {
			super("Remove " + player.getDisplayNameRaw() + "?", "Remove Player", "Cancel");
			this.claim = claim;
			this.player = player;
		}

		@Override
		protected void cancelAction(Player arg0, InventoryClickEvent arg1) {
			arg0.openInventory(new PlayerAddInventory(claim).getInventory());
			
		}

		@Override
		protected void confirmAction(Player arg0, InventoryClickEvent arg1) {
			claim.getMembers().remove(player);
			arg0.closeInventory();
			arg0.sendMessage("Removed " + player.getRankedName() + " from your base.");
			claim.forwardUpdateFrom(claim, null);
		}
	}
}