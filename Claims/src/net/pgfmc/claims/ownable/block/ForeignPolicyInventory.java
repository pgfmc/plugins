package net.pgfmc.claims.ownable.block;

import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.inventory.BaseInventory;

public class ForeignPolicyInventory extends BaseInventory {

    public ForeignPolicyInventory(Claim claim, boolean read) {
        super(27, Component.text("Settings for Foreign Players", NamedTextColor.YELLOW));

        setBack(0, new ClaimConfigInventory(claim, read).getInventory());
        int door = 11;
        int lever = 12;
        int chest = 13;
        int enemy = 14;
        int animal = 15;

        setItem(door, (claim.doorsLocked) ? Material.IRON_DOOR : Material.OAK_DOOR)
            .name(Component.text("Doors", NamedTextColor.YELLOW)
            .append(Component.text(": ", NamedTextColor.GRAY))
            .append((claim.doorsLocked) ? Component.text("Locked.", NamedTextColor.RED) : Component.text("Unlocked.", NamedTextColor.GREEN))
            );

        if (!read) {
            setAction(door, (p,e) -> {
                claim.doorsLocked = !claim.doorsLocked;
                claim.forwardUpdateFrom(claim);
                p.openInventory(new ForeignPolicyInventory(claim, read).getInventory());
            });
        }

        setItem(lever, (claim.switchesLocked) ? Material.HEAVY_WEIGHTED_PRESSURE_PLATE : Material.OAK_PRESSURE_PLATE)
            .name(Component.text("Switches", NamedTextColor.YELLOW)
            .append(Component.text(": ", NamedTextColor.GRAY))
            .append((claim.switchesLocked) ? Component.text("Locked.", NamedTextColor.RED) : Component.text("Unlocked.", NamedTextColor.GREEN))
            );

        if (!read) {
            setAction(lever, (p,e) -> {
                claim.switchesLocked = !claim.switchesLocked;
                claim.forwardUpdateFrom(claim);
                p.openInventory(new ForeignPolicyInventory(claim, read).getInventory());
            });
        }

        setItem(chest, (claim.inventoriesLocked) ? Material.TRIPWIRE_HOOK : Material.CHEST)
            .name(Component.text("Inventories", NamedTextColor.YELLOW)
            .append(Component.text(": ", NamedTextColor.GRAY))
            .append((claim.inventoriesLocked) ? Component.text("Locked.", NamedTextColor.RED) : Component.text("Unlocked.", NamedTextColor.GREEN))
            );

        if (!read) {
            setAction(chest, (p,e) -> {
                claim.inventoriesLocked = !claim.inventoriesLocked;
                claim.forwardUpdateFrom(claim);
                p.openInventory(new ForeignPolicyInventory(claim, read).getInventory());
            });
        }

        setItem(enemy, (claim.monsterKilling) ? Material.ZOMBIE_HEAD : Material.SKELETON_SKULL)
            .name(Component.text("Monster Interactions", NamedTextColor.YELLOW)
            .append(Component.text(": ", NamedTextColor.GRAY))
            .append((claim.monsterKilling) ? Component.text("Allowed", NamedTextColor.RED) : Component.text("Disallowed", NamedTextColor.GREEN))
            );

        if (!read) {
            setAction(enemy, (p,e) -> {
                claim.monsterKilling = !claim.monsterKilling;
                claim.forwardUpdateFrom(claim);
                p.openInventory(new ForeignPolicyInventory(claim, read).getInventory());
            });
        }

        setItem(animal, (claim.livestockKilling) ? Material.PORKCHOP : Material.COOKED_PORKCHOP)
            .name(Component.text("Animal Interactions", NamedTextColor.YELLOW)
            .append(Component.text(": ", NamedTextColor.GRAY))
            .append((claim.livestockKilling) ? Component.text("Allowed", NamedTextColor.RED) : Component.text("Disallowed", NamedTextColor.GREEN))
            );

        if (!read) {
            setAction(animal, (p,e) -> {
                claim.livestockKilling = !claim.livestockKilling;
                claim.forwardUpdateFrom(claim);
                p.openInventory(new ForeignPolicyInventory(claim, read).getInventory());
            });
        }
    }
}
