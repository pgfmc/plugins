package net.pgfmc.survival.menu.teleports;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.RequestType;
import net.pgfmc.core.api.request.inv.RequestListInventory;
import net.pgfmc.survival.menu.CommandMenuInventory;
import net.pgfmc.survival.menu.back.BackConfirmInventory;
import net.pgfmc.survival.menu.tpa.TpaListInventory;

public class Teleports extends BaseInventory {
    
    public Teleports(PlayerData playerdata) {
		super(InventoryType.CHEST, "Teleport Menu");

		setBack(0, new CommandMenuInventory(playerdata).getInventory());

        final int tpaMenu = 6;
        final int back = 16;
        final int requestMenu = 24;


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
                playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
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






         
    }
}
