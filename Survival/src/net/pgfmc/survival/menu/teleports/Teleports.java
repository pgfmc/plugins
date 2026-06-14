package net.pgfmc.survival.menu.teleports;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.RequestType;
import net.pgfmc.core.api.request.inv.RequestListInventory;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.menu.CommandMenuInventory;
import net.pgfmc.survival.menu.back.BackConfirmInventory;
import net.pgfmc.survival.menu.teleports.home.HomeDeleteListInventory;
import net.pgfmc.survival.menu.teleports.home.HomeSelectListInventory;
import net.pgfmc.survival.menu.tpa.TpaListInventory;
import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.Builder;

public class Teleports extends BaseInventory {
    
    public Teleports(PlayerData playerdata) {
		super(InventoryType.CHEST, Component.text("Teleport Menu"));

		setBack(0, new CommandMenuInventory(playerdata).getInventory());

        final int tpaMenu = 6;
        final int back = 16;
        final int requestMenu = 24;
        final int spawnWarp = 11;

        final int setHome = 4;
        final int tpHome = 13;
        final int removeHome = 22;

        // Code for TPA MENU
        // 
        //
        //
        //
        //
        //

        int playeramount = Bukkit.getOnlinePlayers().size();
        
        if (playeramount > 1) {
            setItem(tpaMenu, Material.PLAYER_HEAD)
            	.name(Component
            			.text("Teleport Menu")
            			.color(NamedTextColor.DARK_PURPLE))
                .lore(Arrays.asList(Component
                		.text(playeramount)
                		.color(NamedTextColor.GREEN)
                		.append(Component
                				.text(" Players Online"))));

            setAction(tpaMenu, (player, event) -> {
                player.openInventory(new TpaListInventory(playerdata).getInventory());
            });

        } else {
            setItem(tpaMenu, Material.SKELETON_SKULL)
            	.name(Component
            			.text("Teleport Menu")
            			.color(NamedTextColor.DARK_PURPLE))
                .lore(Arrays.asList(Component
                		.text("No Players Online")
                		.color(NamedTextColor.RED)));

            setAction(tpaMenu, (player, event) -> {
                playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
            });
        }

        // Code for BACK 
        //
        //
        //
        //
        //
        //
        //

        if (playerdata.getData("backLoc") == null)
        {
            setAction(back, (player, event) -> {
                playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
            });
            
            setItem(back, Material.ARROW)
            	.name(Component
            			.text("Teleport Back")
            			.color(NamedTextColor.DARK_GREEN))
                .lore(Arrays.asList(Component
                			.text("No back Location available.")
                			.color(NamedTextColor.GRAY)));

        } else {
            setAction(back, (player, event) -> {
                player.openInventory(new BackConfirmInventory(playerdata).getInventory());
            });

            setItem(back, Material.SPECTRAL_ARROW)
            	.name(Component
            			.text("Teleport Back")
            			.color(NamedTextColor.DARK_GREEN))
                .lore(Arrays.asList(Component
                		.text("Go back to where you teleported from.")
                		.color(NamedTextColor.GRAY)));

        }

        // Code for REQUESTS
        //
        //
        //
        //
        //
        //
        //

		//Get all incoming requests for this player
        final Set<Request> requests = RequestType.getInAllRequests(request -> request.target == playerdata);

        if (requests == null || requests.isEmpty()) // no incoming requests
        {
            setAction(requestMenu, (player, event) -> {
                playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
            });

            setItem(requestMenu, Material.BOOK)
            	.name(Component
            			.text("Requests Menu")
            			.color(NamedTextColor.DARK_RED))
                .lore(Arrays.asList(Component
                		.text("No Requests Available.")
                		.color(NamedTextColor.RED)));

        } else // there are incoming requests
        {
            // number of incoming requests
            final int requestsCount = requests.size();

            setAction(requestMenu, (player, event) -> {
                final RequestListInventory inv = new RequestListInventory(playerdata);
                inv.setBack(0, new Teleports(playerdata).getInventory());
                player.openInventory(inv.getInventory());
            });

            setItem(requestMenu, Material.WRITABLE_BOOK)
            	.name(Component
            			.text("Requests Menu")
            			.color(NamedTextColor.DARK_RED))
                .lore(Arrays.asList(Component
                		.text("Requests: (" + requestsCount + ")")
                		.color(NamedTextColor.DARK_GRAY)));
        }

        // Code for SPAWN WARP
        //
        //
        //
        //
        //

        setAction(spawnWarp, (player, event) -> {
            playerdata.getPlayer().performCommand("warp spawn");
            player.closeInventory();
        });

        setItem(spawnWarp, Material.CHERRY_SAPLING)
        	.name(Component
        			.text("Warp to Spawn")
        			.color(NamedTextColor.RED));

		
		setAction(tpHome, (player, event) -> {
			player.openInventory(new HomeSelectListInventory(playerdata).getInventory());
		});
		
		setItem(tpHome, Material.ENDER_PEARL)
			.name(Component
					.text("Go to Home")
					.color(NamedTextColor.LIGHT_PURPLE));
		
		setAction(setHome, (player, event) -> {
			Builder builder = new AnvilGUI.Builder().plugin(Main.plugin);
			
			builder.onClose(stateSnapshot -> {});
			
			builder.onClick((slot, stateSnapshot) -> {
		        if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList(); // Do nothing
		        
		        final String homeName = stateSnapshot.getText();
		        stateSnapshot.getPlayer().performCommand("sethome " + homeName);
		        
		        return Arrays.asList(AnvilGUI.ResponseAction.run(new Runnable() {
					@Override
					public void run() {
						player.openInventory(new Teleports(playerdata).getInventory());
						
					}}));
		    });
			
			builder.text("Enter name").title("Set Home").plugin(Main.plugin);
			builder.open(player);
			
		});
		
		setItem(setHome, Material.RED_BED)
			.name(Component
					.text("Set Home")
					.color(NamedTextColor.GREEN));
		
		setAction(removeHome, (player, event) -> {
			player.openInventory(new HomeDeleteListInventory(playerdata).getInventory());
		});
		
		setItem(removeHome, Material.BARRIER)
			.name(Component
					.text("Delete Home")
					.color(NamedTextColor.RED));
		
    }
}
