package net.pgfmc.survival.masterbook.inv.staff.inv.giverewards.inv;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.Rewards;
import net.pgfmc.survival.masterbook.inv.staff.inv.StaffInventory;
import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.Builder;

public class GiveRewardsListInventory extends ListInventory<String> implements Listener {
	
	private PlayerData playerdata;

	public GiveRewardsListInventory(final PlayerData playerdata) {
		super(27, ChatColor.GRAY + "Give Rewards");
		
		setBack(0, new StaffInventory(playerdata).getInventory());
		
		this.playerdata = playerdata;
		
	}
	
	/**
	 * Don't use this constructor
	 * For Listener only
	 */
	public GiveRewardsListInventory()
	{
		super(27, ChatColor.GRAY + "Give Rewards");
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{
		if (!(e.getInventory().getHolder() instanceof GiveRewardsListInventory)) return;
		if (!(e.getClickedInventory() instanceof PlayerInventory)) return;
		if (e.getCurrentItem() == null) return;
		
		final ItemStack reward = e.getCurrentItem();
		final Player player = (Player) e.getWhoClicked();
		final PlayerData playerdata = PlayerData.from(player);
		Builder builder = new AnvilGUI.Builder();
		
		builder.onClose(stateSnapshot -> {});
		
		builder.onClick((slot, stateSnapshot) -> {
	        if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList(); // Do nothing
	        
	        final String tag_id = stateSnapshot.getText();
	        
	        if (Rewards.getRewardsMap().containsKey(tag_id))
	        {
	        	playerdata.sendMessage(ChatColor.RED + "Reward identification exists already.");
	        	
	        	return Arrays.asList(AnvilGUI.ResponseAction.run(new Runnable() {
	        		@Override
	        		public void run() {
	        			playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
	        			player.openInventory(new GiveRewardsListInventory(playerdata).getInventory());
	        			
	 				}}));
	        	
	        }
	        
	        Rewards.addRewardToList(tag_id, reward);
	        
	        return Arrays.asList(AnvilGUI.ResponseAction.run(new Runnable() {
				@Override
				public void run() {
					playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
					player.openInventory(new GiveRewardsListInventory(playerdata).getInventory());
					
				}}));
	        
		});
		
		builder.text("reward_id_here").title(ChatColor.GRAY + "Enter Reward ID").plugin(Main.plugin);
		builder.open(player);
		
	}

	@Override
	protected List<String> load() {
		// Just a weird way to convert a Set<String> to List<String>
		return Rewards.getRewardsMap().keySet().stream().collect(Collectors.toList());
	}

	@Override
	protected Butto toAction(String entry) {
		return (player, event) -> {
			player.openInventory(new RemoveRewardConfirmInventory(playerdata, entry).getInventory());
		};
	}

	@Override
	protected ItemStack toItem(String entry) {
		return new ItemWrapper(Rewards.getRewardsMap().get(entry)).l(ChatColor.RESET + "" + ChatColor.GRAY + entry).gi();
	}

}
