package net.pgfmc.survival.menu.teleports;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.RequestType;
import net.pgfmc.core.api.request.inv.RequestListInventory;
import net.pgfmc.core.util.SoundEffect;
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
		super(InventoryType.CHEST, "Teleport Menu");

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
            setItem(tpaMenu, Material.PLAYER_HEAD).n(ChatColor.DARK_PURPLE + "Teleport Menu")
                .l(ChatColor.GREEN + String.valueOf(playeramount) + " Players Online");

            setAction(tpaMenu, (player, event) -> {
                player.openInventory(new TpaListInventory(playerdata).getInventory());
            });

        } else {
            setItem(tpaMenu, Material.SKELETON_SKULL).n(ChatColor.DARK_PURPLE + "Teleport Menu")
                    .l(ChatColor.RED + "No Players Online");

            setAction(tpaMenu, (player, event) -> {
                SoundEffect.ERROR.play(playerdata);
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
                SoundEffect.ERROR.play(playerdata);
            });
            
            setItem(back, Material.ARROW).n(ChatColor.DARK_GREEN + "Teleport Back")
                .l(ChatColor.GRAY + "No back Location available.");

        } else {
            setAction(back, (player, event) -> {
                player.openInventory(new BackConfirmInventory(playerdata).getInventory());
            });

            setItem(back, Material.SPECTRAL_ARROW).n(ChatColor.DARK_GREEN + "Teleport Back")
                .l(ChatColor.GRAY + "Go back to where you teleported from.");

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
                SoundEffect.ERROR.play(playerdata);
            });

            setItem(requestMenu, Material.BOOK).n(ChatColor.DARK_RED + "Requests Menu")
                .l(ChatColor.RED + "No Requests Available.");

        } else // there are incoming requests
        {
            // number of incoming requests
            final int requestsCount = requests.size();

            setAction(requestMenu, (player, event) -> {
                final RequestListInventory inv = new RequestListInventory(playerdata);
                inv.setBack(0, new Teleports(playerdata).getInventory());
                player.openInventory(inv.getInventory());
            });

            setItem(requestMenu, Material.WRITABLE_BOOK).n(ChatColor.DARK_RED + "Requests Menu")
                .l(ChatColor.DARK_GRAY + "Requests: (" + requestsCount + ")");
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

        setItem(spawnWarp, Material.CHERRY_SAPLING).n(ChatColor.RED + "Warp to Spawn");

		
		setAction(tpHome, (player, event) -> {
			player.openInventory(new HomeSelectListInventory(playerdata).getInventory());
		});
		
		setItem(tpHome, Material.ENDER_PEARL).n(ChatColor.LIGHT_PURPLE + "Go to Home");
		
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
		
		setItem(setHome, Material.RED_BED).n(ChatColor.GREEN + "Set Home");
		
		setAction(removeHome, (player, event) -> {
			player.openInventory(new HomeDeleteListInventory(playerdata).getInventory());
		});
		
		setItem(removeHome, Material.BARRIER).n(ChatColor.RED + "Delete Home");
		
    }
}
