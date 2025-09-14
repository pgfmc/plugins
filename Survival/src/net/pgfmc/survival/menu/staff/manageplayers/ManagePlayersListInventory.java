package net.pgfmc.survival.menu.staff.manageplayers;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.cmd.Skull;
import net.pgfmc.survival.menu.staff.StaffInventory;

public class ManagePlayersListInventory extends ListInventory<PlayerData> {
	
	private PlayerData playerdata;
	private boolean showOnlinePlayersOnly = true;
	
	public ManagePlayersListInventory(final PlayerData playerdata, boolean showOnlinePlayersOnly) {
		super(54, "Manage Players");
		
		this.playerdata = playerdata;
		this.showOnlinePlayersOnly = showOnlinePlayersOnly;
        int offlineOnlineToggle = 2;
		
		setBack(0, new StaffInventory(playerdata).getInventory());
		
		/* 
		 * Player Toggle (Online/Offline)
		 * [] [] XX [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(offlineOnlineToggle, (player, event) -> {
			player.openInventory(new ManagePlayersListInventory(playerdata, !showOnlinePlayersOnly).getInventory());
			
		});
		
		if (showOnlinePlayersOnly)
		{
			setItem(offlineOnlineToggle, Material.SLIME_BALL).n(ChatColor.GOLD + "Showing: " + ChatColor.GREEN + "Online Players").l(ChatColor.GRAY + "Click to show offline players only.");
			
		} else
		{
			setItem(offlineOnlineToggle, Material.FIRE_CHARGE).n(ChatColor.GOLD + "Showing: " + ChatColor.DARK_GRAY + "Offline Players").l(ChatColor.GRAY + "Click to show online players only.");
			
		}
		
	}

	@Override
	protected List<PlayerData> load() {
		
		
		if (showOnlinePlayersOnly)
		{
			// Online Players ONLY
			return PlayerData.getPlayerDataSet(playerdata -> playerdata.isOnline()).stream().collect(Collectors.toList());
		}
		
		// Offline Players ONLY
		return PlayerData.getPlayerDataSet(playerdata -> !playerdata.isOnline()).stream().collect(Collectors.toList());
	}

	@Override
	protected Butto toAction(PlayerData entry) {
		return (player, event) -> {
			player.openInventory(new ManagePlayerInventory(playerdata, entry).getInventory());
		};
	}

	@Override
	protected ItemStack toItem(PlayerData entry) {
		return new ItemWrapper(Skull.getHead(entry.getUniqueId())).n(entry.getRankedName()).l(ChatColor.GRAY + "Click to manage.").gi();
	}
	
	

}
