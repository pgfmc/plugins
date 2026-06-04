package net.pgfmc.claims.ownable.block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.core.util.vector4.Vector4;

public class ClaimViewInventory extends ListInventory<Claim> {
    Claim claim;

    public ClaimViewInventory(Claim claim) {
            super(27, "Merged Claims");
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

            String name = ChatColor.GOLD + "Merged Claim";

            if (arg0 == this.claim) {
                name = ChatColor.AQUA + "This Claim";
            }

            ArrayList<PotionEffect> effects = new ArrayList<>();
            arg0.addEffectsFromClaim(effects);

            Vector4 loc = arg0.getLocation();

            return new ItemWrapper(Material.LODESTONE)
                .n(name)
                .l(
                    ChatColor.GRAY + 
                    "X " + String.valueOf(loc.x()) + 
                    "\nY " + String.valueOf(loc.y()) +
                    "\nZ " + String.valueOf(loc.z()) +
                    ChatColor.GRAY + "\nBeacons Linked to this Claim: " + ChatColor.AQUA + String.valueOf(arg0.beacons.size()) +
                    ClaimConfigInventory.displayEffects(effects)
                  ).gi();
        }
    }
