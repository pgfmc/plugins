package net.pgfmc.survival.menu.staff.managerewards;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.Reward;
import net.pgfmc.survival.menu.staff.StaffInventory;
import net.pgfmc.survival.menu.staff.managerewards.create.CreateRewardListInventory;

public class ManageRewardsListInventory extends ListInventory<Reward> {

    private PlayerData playerdata;

    public ManageRewardsListInventory(PlayerData playerdata) {
        super(54, "Manage Rewards");
        
        this.playerdata = playerdata;

        setBack(0, new StaffInventory(playerdata).getInventory());

        setItem(18, Material.CRAFTING_TABLE);
        setAction(18, (player, event) -> {
            playerdata.getPlayer().openInventory(new CreateRewardListInventory(playerdata).getInventory());
        });

    }

    @Override
    protected List<Reward> load() {
        return Reward.getAllRewards();
    }

    @Override
    protected Butto toAction(Reward entry) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toAction'");
    }

    @Override
    protected ItemStack toItem(Reward entry) {

        final List<String> lore = List.of(entry.getDescription(), "(Timestamp) " + String.valueOf(entry.getTimestamp()), "(Expiration) " + String.valueOf(entry.getExpiration()), "(UUID) " + entry.getUUID());
        lore.addAll(entry.getItems().stream().map(item -> item.getType().toString()).toList());

        return new ItemWrapper(entry.getGiftBox())
            .n(entry.getTitle())
            .l(lore)
            .gi();
    }
    
}
