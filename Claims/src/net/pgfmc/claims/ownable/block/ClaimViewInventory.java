package net.pgfmc.claims.ownable.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.core.util.vector4.Vector4;

public class ClaimViewInventory extends ListInventory<Claim> {
    Claim claim;

    public ClaimViewInventory(Claim claim) {
            super(27, Component.text("Merged Claims"));
            this.claim = claim;
        }

        @Override
        protected List<Claim> load() {
            List<Claim> list = new ArrayList<>();

            if (claim == null) {
                return list;
            }

            list.add(this.claim);

            for (Claim c : claim.getNetwork()) {
                if (c == this.claim) { continue;}
                list.add(c);
            }
            return list;
        }

        @Override
        protected Butto toAction(Claim arg0) {
            return Butto.defaultButto;
        }

        @Override
        protected ItemStack toItem(Claim arg0) {

            Component name = Component.text("Merged Claim", NamedTextColor.GOLD);

            if (arg0 == this.claim) {
                name = Component.text("This Claim", NamedTextColor.AQUA);
            }

            ArrayList<PotionEffect> effects = new ArrayList<>();
            arg0.addEffectsFromClaim(effects);

            Vector4 loc = arg0.getLocation();

            List<Component> claimLore = Arrays.asList(
                    Component.text("X " + String.valueOf(loc.x()), NamedTextColor.GRAY),
                    Component.text("Y " + String.valueOf(loc.y()), NamedTextColor.GRAY),
                    Component.text("Z " + String.valueOf(loc.z()), NamedTextColor.GRAY),
                    Component.text("Beacons Linked to this Claim: ", NamedTextColor.GRAY)
                    .append(Component.text(arg0.beacons.size(), NamedTextColor.AQUA))
                    );
            claimLore.addAll(ClaimConfigInventory.displayEffects(effects));


            return new ItemWrapper(Material.LODESTONE)
                .name(name)
                .lore(claimLore)
                .item();
        }
    }
