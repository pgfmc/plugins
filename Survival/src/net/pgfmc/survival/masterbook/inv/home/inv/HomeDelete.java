package net.pgfmc.survival.masterbook.inv.home.inv;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.cmd.home.Homes;
import net.pgfmc.survival.masterbook.inv.MasterbookInventory;

public class HomeDelete extends ListInventory<String> {
	
	private PlayerData pd;
	private HashMap<String, Location> homes;

	public HomeDelete(PlayerData pd) {
		super(27, "§r§8Delete Home");
		
		this.pd = pd;
		this.homes = Homes.getHomes(pd);
		
		setBack(0, new MasterbookInventory(pd).getInventory());
	}

	@Override
	public List<String> load() {
		return Homes.getHomes(pd).keySet().stream().collect(Collectors.toList());
	}
	
	@Override
	protected Butto toAction(String entry) {
		
		if (!pd.hasPermission("pgf.cmd.home.del"))
		{
			return (p, e) -> {
				p.sendMessage("§cYou don't have permission to execute this command.");
			};
		}
		
		if (homes.size() == 0)
		{
			return (p, e) -> {
				p.sendMessage("§cYou do not have any homes.");
			};
		}
		
		return (p, e) -> {
			p.performCommand("delhome " + entry);
			p.closeInventory();
		};
		
	}
	
	@Override
	protected ItemStack toItem(String entry) {
		return new ItemWrapper(Material.PAPER).n("§r§a" + entry).gi();
	}

}
