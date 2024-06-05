package net.pgfmc.claims.ownable.block;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.inventory.BaseInventory;

public class ForeignPolicyInventory extends BaseInventory {

    public ForeignPolicyInventory(Claim claim) {
        super(27, ChatColor.YELLOW + "Settings for Foreign Players");

        setBack(0, new ClaimConfigInventory(claim).getInventory());
        int door = 11;
        int lever = 12;
        int chest = 13;
        int enemy = 14;
        int animal = 15;

        if (claim.doorsLocked) {
            setItem(door, Material.IRON_DOOR).n(ChatColor.YELLOW + "Doors" + ChatColor.GRAY + ": " + ChatColor.RED + "Locked.");
        } else {
            setItem(door, Material.OAK_DOOR).n(ChatColor.YELLOW + "Doors" + ChatColor.GRAY + ": " + ChatColor.GREEN + "Unlocked.");
        }

        setAction(door, (p,e) -> {
            claim.doorsLocked = !claim.doorsLocked;
            claim.forwardUpdateFrom(claim);
            p.openInventory(new ForeignPolicyInventory(claim).getInventory());
        });
        





        if (claim.switchesLocked) {
            setItem(lever, Material.HEAVY_WEIGHTED_PRESSURE_PLATE).n(ChatColor.YELLOW + "Switches" + ChatColor.GRAY + ": " + ChatColor.RED + "Locked.");
        } else {
            setItem(lever, Material.OAK_PRESSURE_PLATE).n(ChatColor.YELLOW + "Switches" + ChatColor.GRAY + ": " + ChatColor.GREEN + "Unlocked.");
        }

        setAction(lever, (p,e) -> {
            claim.switchesLocked = !claim.switchesLocked;
            claim.forwardUpdateFrom(claim);
            p.openInventory(new ForeignPolicyInventory(claim).getInventory());
        });



        if (claim.inventoriesLocked) {
            setItem(chest, Material.TRIPWIRE_HOOK).n(ChatColor.YELLOW + "Inventories" + ChatColor.GRAY + ": " + ChatColor.RED + "Locked.");
        } else {
            setItem(chest, Material.CHEST).n(ChatColor.YELLOW + "Inventories" + ChatColor.GRAY + ": " + ChatColor.GREEN + "Unlocked.");
        }

        setAction(chest, (p,e) -> {
            claim.inventoriesLocked = !claim.inventoriesLocked;
            claim.forwardUpdateFrom(claim);
            p.openInventory(new ForeignPolicyInventory(claim).getInventory());
        });


        if (claim.monsterKilling) {
            setItem(enemy, Material.ZOMBIE_HEAD).n(ChatColor.YELLOW + "Monster Interactions" + ChatColor.GRAY + ": " + ChatColor.RED + "Allowed.");
        } else {
            setItem(enemy, Material.SKELETON_SKULL).n(ChatColor.YELLOW + "Monster Interactions" + ChatColor.GRAY + ": " + ChatColor.GREEN + "Disallowed.");
        }

        setAction(enemy, (p,e) -> {
            claim.monsterKilling = !claim.monsterKilling;
            claim.forwardUpdateFrom(claim);
            p.openInventory(new ForeignPolicyInventory(claim).getInventory());
        });


        if (claim.livestockKilling) {
            setItem(animal, Material.PORKCHOP).n(ChatColor.YELLOW + "Animal Interactions" + ChatColor.GRAY + ": " + ChatColor.RED + "Allowed.");
        } else {
            setItem(animal, Material.COOKED_PORKCHOP).n(ChatColor.YELLOW + "Animal Interactions" + ChatColor.GRAY + ": " + ChatColor.GREEN + "Disallowed.");
        }

        setAction(animal, (p,e) -> {
            claim.livestockKilling = !claim.livestockKilling;
            claim.forwardUpdateFrom(claim);
            p.openInventory(new ForeignPolicyInventory(claim).getInventory());
        });
    }
}
