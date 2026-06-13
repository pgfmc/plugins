package net.pgfmc.claims.ownable.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.claims.Main;
import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.core.util.vector4.Vector4;

public class ClaimConfigInventory extends BaseInventory {
	
	public ClaimConfigInventory(Claim claim, boolean read) {
        super(27, (read) ? Component.text("Claim View") : Component.text("Claim Settings"));

        int membersList = 10;
        int addPlayer = 11;
        int info = 13;
        int explosions = 15;
        int foreignPolicy = 16;
		
		setItem(membersList, Material.BOOK).name(Component.text("Members", NamedTextColor.GRAY));
		setAction(membersList, (p, e) -> {
			p.openInventory(new PlayerViewInventory(claim, read).getInventory());
		});
		
        if (!read) {
		    setItem(addPlayer, Material.PLAYER_HEAD).name(Component.text("Add member", NamedTextColor.GRAY));
		    setAction(addPlayer, (p,e) -> {
		    	p.openInventory(new PlayerAddInventory(claim).getInventory());
		    });
        }

        Vector4 loc = claim.getLocation();
        Component name;

        if (claim.getPlayer() == null) {
            name = Component.text("Creative", NamedTextColor.LIGHT_PURPLE); 
        } else {
            name = Component.text("Owned by ", NamedTextColor.WHITE)
                .append(claim.getPlayer().getRankedName());
        }

        Pair beaconInfo = claim.getBeaconInfo();
        int selfBeacons = claim.beacons.size();

        List<Component> infoLore = Arrays.asList(
                name,
                Component.text("X " + String.valueOf(loc.x()), NamedTextColor.GRAY),
                Component.text("Y " + String.valueOf(loc.y()), NamedTextColor.GRAY),
                Component.text("Z " + String.valueOf(loc.z()), NamedTextColor.GRAY),
                Component.text("Beacons Linked to this Claim: ")
                    .append(Component.text(String.valueOf(selfBeacons), NamedTextColor.AQUA)),
                Component.text("Total Beacons in Network: ", NamedTextColor.GRAY)
                    .append(Component.text(String.valueOf(beaconInfo.beaconCount), NamedTextColor.AQUA))
                );
        infoLore.addAll(displayEffects(beaconInfo.effects));

        infoLore.add(Component.text("Click to open list of ", NamedTextColor.WHITE)
                .append(Component.text("Merged Claims", NamedTextColor.GOLD))
                .append(Component.text("!", NamedTextColor.WHITE)));


        setItem(info, Material.PAPER)
            .name(Component.text("Claim Info", NamedTextColor.WHITE))
            .lore(infoLore);

        setAction(info, (p,e) -> {
            ClaimViewInventory cvi = new ClaimViewInventory(claim);
            cvi.setBack(0, new ClaimConfigInventory(claim, read).getInventory());
            p.openInventory(cvi.getInventory());
        });

        if (claim.explosionsEnabled) {
            setItem(explosions, Material.TNT)
                .name(Component.text("Allow Explosions? ", NamedTextColor.RED)
                        .append(Component.text("(", NamedTextColor.GRAY))
                        .append(Component.text("yes", NamedTextColor.GREEN))
                        .append(Component.text(")", NamedTextColor.GRAY))
                     );
        } else {
            setItem(explosions, Material.TNT)
                .name(Component.text("Allow Explosions? ", NamedTextColor.RED)
                        .append(Component.text("(", NamedTextColor.GRAY))
                        .append(Component.text("no", NamedTextColor.RED))
                        .append(Component.text(")", NamedTextColor.GRAY))
                     );
        }

        if (!read) {
            setAction(explosions, (p,e) -> {
                claim.explosionsEnabled = !claim.explosionsEnabled;
                claim.forwardUpdateFrom(claim);
                p.openInventory(new ClaimConfigInventory(claim, read).getInventory());
            });
        }

        setItem(foreignPolicy, Material.CREEPER_HEAD)
            .name(Component.text("Foreign Policy", NamedTextColor.YELLOW))
            .lore(Arrays.asList(
                        Component.text("Control what non-members are", NamedTextColor.GRAY),
                        Component.text("allowed to do in your claim!", NamedTextColor.GRAY)
                        )
                 );
                    
        setAction(foreignPolicy, (p,e) -> {
            p.openInventory(new ForeignPolicyInventory(claim, read).getInventory());
        });
	}
	
	public static class PlayerViewInventory extends ListInventory<PlayerData> {
		
		Claim claim;
        boolean read;

		public PlayerViewInventory(Claim claim, boolean read) {
			super(27, Component.text("Allowed Players"));
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
			
            ItemStack item = new ItemWrapper(Material.PLAYER_HEAD).name(arg0.getRankedName()).item();
			SkullMeta meta = (SkullMeta) item.getItemMeta(); // Get the created item's ItemMeta and cast it to SkullMeta so we can access the skull properties
			meta.setOwningPlayer(arg0.getOfflinePlayer()); // Set the skull's owner so it will adapt the skin of the provided username (case sensitive).
			item.setItemMeta(meta); // Apply the modified meta to the initial created item
			
			return item;
		}
	}

	public static class PlayerAddInventory extends ListInventory<PlayerData> {
		
		Claim claim;

		public PlayerAddInventory(Claim claim) {
			super(27, Component.text("Add Players"));
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
			
            ItemStack item = new ItemWrapper(Material.PLAYER_HEAD).name(arg0.getRankedName()).item();
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
			super(
                    Component.text("Add ")
                    .append(player.getRankedName())
                    .append(Component.text("?")),
                    Component.text("Add Player"),
                    Component.text("Cancel"));
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
			arg0.sendMessage(NamedTextColor.GOLD + "Added " + player.getRankedName() + NamedTextColor.GOLD + " to your base.");
			claim.forwardUpdateFrom(claim);
		}
	}
	
	public static class RemovePlayerInventory extends ConfirmInventory {
		
		Claim claim;
		PlayerData player;
		
		public RemovePlayerInventory(Claim claim, PlayerData player) {
			super(
                    Component.text("Remove ")
                    .append(player.getRankedName())
                    .append(Component.text("?")),
                    Component.text("Remove Player"),
                    Component.text("Cancel"));
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
			arg0.sendMessage(NamedTextColor.GOLD + "Removed " + player.getRankedName() + NamedTextColor.GOLD + " from your base.");
			claim.forwardUpdateFrom(claim);
		}
	}

    public static List<Component> displayEffects(ArrayList<PotionEffect> effects) {
        List<Component> acc = new ArrayList<>(); 
        if (effects.size() > 0) {
            acc.add(Component.text("Effects:", NamedTextColor.LIGHT_PURPLE));
            for (PotionEffect effect : effects) {

                PotionEffectType type = effect.getType();
                Component entry = Component.text(" - ", NamedTextColor.GRAY);

                String suffix = "";
                if (effect.getAmplifier() == 1) {
                    suffix = " II";
                }

                // I have to yanderedev code because its coded as seperate constants, not an enum lmao
                if (type.equals(PotionEffectType.STRENGTH)) {
                    entry.append(Component.text("Strength" + suffix, NamedTextColor.GOLD));
                } else if (type.equals(PotionEffectType.JUMP_BOOST)) {
                    entry.append(Component.text("Jump Boost" + suffix, NamedTextColor.YELLOW));
                } else if (type.equals(PotionEffectType.SPEED)) {
                    entry.append(Component.text("Speed" + suffix, NamedTextColor.AQUA));
                } else if (type.equals(PotionEffectType.HASTE)) {
                    entry.append(Component.text("Haste" + suffix, NamedTextColor.GOLD));
                } else if (type.equals(PotionEffectType.REGENERATION)) {
                    entry.append(Component.text("Regeneration" + suffix, NamedTextColor.RED));
                } else if (type.equals(PotionEffectType.RESISTANCE)) {
                    entry.append(Component.text("Resistance" + suffix, NamedTextColor.LIGHT_PURPLE));
                } else {
                    Main.getPlugin().getLogger().info("Invalid Buff passed to displayEffects: " + type.toString());
                    continue;
                }

                acc.add(entry);
            }
        }

        return acc;
    }
}
