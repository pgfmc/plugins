package net.pgfmc.survival.menu.staff.manageplayers;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
		super(54, Component.text("Manage Players"));
		
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
			setItem(offlineOnlineToggle, Material.SLIME_BALL)
                .name(Component.text()
                        .append(Component.text("Showing: ", NamedTextColor.GOLD))
                        .append(Component.text("Online Players", NamedTextColor.GREEN)).build())
                .lore(Component.text("Click to show offline players only.", NamedTextColor.GRAY));
			
		} else {
			setItem(offlineOnlineToggle, Material.SLIME_BALL)
                .name(Component.text()
                        .append(Component.text("Showing: ", NamedTextColor.GOLD))
                        .append(Component.text("Offline Players", NamedTextColor.DARK_GRAY)).build())
                .lore(Component.text("Click to show offline players only.", NamedTextColor.GRAY));
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
		return new ItemWrapper(Skull.getHead(entry.getUniqueId()))
            .name(entry.getRankedName())
            .lore(Component.text("Click to manage.", NamedTextColor.GRAY)).item();
	}
}
