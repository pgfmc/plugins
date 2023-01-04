package net.pgfmc.survival.masterbook.inv;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.inv.RequestListInventory;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.core.util.roles.PGFRole;
import net.pgfmc.core.util.roles.Roles;
import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.survival.masterbook.inv.home.inv.HomeHomepage;

public class MasterbookInventory implements InventoryHolder {
	
	private PlayerData pd;
	
	public MasterbookInventory(PlayerData pd)
	{
		this.pd = pd;
	}
	
	public class Homepage extends BaseInventory {
		
		public Homepage()
		{
			super(27, "Command Menu");
			
			/* 
			 * [] [] XX [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (pd.hasPermission("pgf.cmd.link"))
			{
				if (pd.getData("Discord") != null)
				{
					setAction(2, (p, e) -> {
						p.openInventory(new DiscordConfirmInventory(pd).getInventory());
					});
					
					setItem(2, Material.AMETHYST_SHARD).n("§dUnlink Discord");
				
				} else {
					
					setAction(2, (p, e) -> {
						p.closeInventory();
						p.performCommand("link");
					});
					setItem(2, Material.QUARTZ).n("§dLink Discord");
					
				}
				
			}
			
			/* 
			 * [] [] [] XX [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * toggles AFK in-menu
			 */
			if (pd.hasPermission("pgf.cmd.afk"))
			{
				if (Afk.isAfk(pd.getPlayer()))
				{
					setAction(3, (p, e) -> {
						disableAFK(this);
						p.performCommand("afk");
					});
					
					setItem(3, Material.BLUE_ICE).n("§r§7AFK: §aEnabled").l("§r§7Click to disable!");
					
				} else {
					setAction(3, (p, e) -> {
						enableAFK(this);
						p.performCommand("afk");
					});
					
					setItem(3, Material.ICE).n("§r§7AFK: §cDisabled").l("§r§7Click to enable!");
					
				}
				
			}
			
			/* 
			 * [] [] [] [] [] XX [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * Back command
			 */
			setAction(23, (p, e) -> {
				p.openInventory(new BackConfirmInventory(pd).getInventory());
			});
			
			ItemStack item = new ItemWrapper(Material.TIPPED_ARROW).n("§r§4Back").l("§r§7Go back to your last location").gi();
			
			PotionMeta pot = (PotionMeta) item.getItemMeta();
			PotionData potion = new PotionData(PotionType.INSTANT_DAMAGE);
			pot.setBasePotionData(potion);
			item.setItemMeta(pot);
			
			setItem(23, item);
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] XX [] [] [] [] [] []
			 * home menu
			 */
			setAction(20, (p, e) -> {
				p.openInventory(new HomeHomepage(pd).getInventory());
			});
					
			setItem(20, Material.COMPASS).n("§r§eHomes");
			
			if (Bukkit.getOnlinePlayers().size() == 1)
			{
				setAction(21, (p, e) -> {
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
				});
				
				setItem(21, Material.GRAY_CONCRETE).n("§r§5Tpa").l("§r§cNo players online.");
				
			} else {
				setAction(21, (p, e) -> {
					p.openInventory(new TpaListInventory(pd).getInventory());
				});
			
				setItem(21, Material.ENDER_PEARL).n("§r§5Tpa").l("§r§7Teleport to another player!");
			}
			
			// Other buttons -
			
			/* 
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * Nickname
			 */
			setAction(4, (p, e) -> {
				if (pd.hasPermission("pgf.cmd.donator.nick"))
				{
					pd.addTag("nick"); // TODO make this better or soemthing
					p.closeInventory();
					p.sendMessage("§9Type your new nickname in chat.");
					
				} else {
					p.sendMessage("§cYou need donator for that!");
					
				}
				
			});
			
			PGFRole role = Roles.getTop(pd);
			
			setItem(4, Skull.getHead(pd.getUniqueId(), null))
					.n(pd.getRankedName() + " (" + role.getName().substring(0,1).toUpperCase() + role.getName().substring(1).toLowerCase() + ")")
					.l("§7Change nickname!");
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] XX [] [] [] []
			 * home menu
			 */
			setAction(22, (p, e) -> {
				p.performCommand("echest");
			});
			
			setItem(22, Material.ENDER_CHEST).n("§r§3Ender Chest").l("§r§9Donator perk!");
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * XX [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * Requests
			 */
			setAction(24, (p, e) -> {
				RequestListInventory inv = new RequestListInventory(PlayerData.from(p));
				inv.setBack(0, new MasterbookInventory(pd).getInventory());
				
				p.openInventory(inv.getInventory());
				
			});
			
			setItem(24, Material.LEVER).n("§r§4Requests");
			
			/* 
			 * [] [] [] XX [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * toggles PVP in-menu
			 */
			if (pd.hasPermission("pgf.cmd.pvp"))
			{
				if (pd.hasTag("pvp"))
				{
					setAction(5, (p, e) -> {
						enablePVP(this);
						p.performCommand("pvp");
					});
					
					setItem(5, Material.DIAMOND_SWORD).n("§r§7PVP: §aEnabled").l("§r§7Click to disable!");
					
				} else {
					setAction(5, (p, e) -> {
						disablePVP(this);
						p.performCommand("pvp");
					});
					
					setItem(5, Material.WOODEN_SWORD).n("§r§7PVP: §cDisabled").l("§r§7Click to enable!");
					
				}
				
			}
			
			 if (pd.hasPermission("pgf.cmd.lodestone"))
			 {
				 if (pd.hasTag("loded"))
				 {
					 setItem(6, Material.GRAY_CONCRETE).n("§cNo Rewards!");
					 
				 } else {
					 setItem(6, Material.LODESTONE).n("§aGet a free Lodestone!").l("§8Lodestones can be used to claim land.");
					 
					 setAction(6, (p, e) -> {
						 pd.getPlayer().performCommand("getclaim");
						 p.closeInventory();
						 
					 });
					 
				 }
				 
			 }
			 
		}
		
	}
	
	@Override
	public Inventory getInventory() {
		return new Homepage().getInventory();
	}
	
	private static void enablePVP(BaseInventory inventory)
	{
		inventory.setAction(5, (p, e) -> {
			disablePVP(inventory);
			p.performCommand("pvp");
		});
		
		inventory.setItem(5, Material.WOODEN_SWORD).n("§r§7PVP: §cDisabled").l("§r§7Click to enable!");
		
	}
	
	private static void disablePVP(BaseInventory inventory)
	{
		inventory.setAction(5, (p, e) -> {
			enablePVP(inventory);
			p.performCommand("pvp");
		});
		
		inventory.setItem(5, Material.DIAMOND_SWORD).n("§r§7PVP: §aEnabled").l("§r§7Click to disable!");
		
	}
	
	private static void enableAFK(BaseInventory inventory)
	{
		inventory.setAction(3, (p, e) -> {
			disableAFK(inventory);
			p.performCommand("afk");
		});
		
		inventory.setItem(3, Material.BLUE_ICE).n("§r§7AFK: §aEnabled").l("§r§7Click to disable!");
		
	}
	
	private static void disableAFK(BaseInventory inventory)
	{
		inventory.setAction(3, (p, e) -> {
			enableAFK(inventory);
			p.performCommand("afk");
		});
		
		inventory.setItem(3, Material.ICE).n("§r§7AFK: §cDisabled").l("§r§7Click to enable!");
		
	}
	
}
