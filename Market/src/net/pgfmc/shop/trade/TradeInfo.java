package net.pgfmc.shop.trade;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.playerdataAPI.PlayerData;

@Deprecated
public class TradeInfo {
	
	@SuppressWarnings("unused")
	private Set<TradeInfo> instances = new HashSet<>();
	
	PlayerData pd1;
	PlayerData pd2;
	
	ItemStack[] player1Items;
	ItemStack[] player2Items;
	
	public TradeInfo(PlayerData pd1, PlayerData pd2) {
		
		this.pd1 = pd1;
		this.pd2 = pd2;
		
		player1Items = new ItemStack[18];
		player2Items = new ItemStack[18];
	}
	
	@SuppressWarnings("unused")
	private TradeInfo(PlayerData pd1, PlayerData pd2, ItemStack[] ipd1, ItemStack[] ipd2) {
		this.pd1 = pd1;
		this.pd2 = pd2;
		
		player1Items = ipd1;
		player2Items = ipd2;
	}
	
	
	
	
	
	
	
	
	
	
	
}
