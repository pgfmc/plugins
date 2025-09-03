package net.pgfmc.survival;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;

import net.pgfmc.core.api.playerdata.PlayerData;

public class Reward {

    private List<ItemStack> items = List.of();
    private String title = "";
    private String description = "";
    private boolean awardToAll = false;
    private long expiration = -1L;
    private Material giftBox = null;

    private long timestamp = new Date().getTime();
    private List<OfflinePlayer> recipients = List.of();
    private UUID uuid = UUID.randomUUID();

    public Reward(List<ItemStack> items, String title, String description, boolean awardToAll, long expiration, Material giftBox)
    {
        this.items = items;
        this.title = title;
        this.description = description;
        this.awardToAll = awardToAll;
        this.expiration = expiration;
        this.giftBox = giftBox;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Reward)) return false;
        if (!((Reward) o).uuid.equals(this.uuid)) return false;
        return true;
    }

    public static List<Reward> getAllRewards()
    {
        final List<Reward> allRewards = List.of();

        PlayerData.getPlayerDataSet().forEach(pd -> {
            final List<Reward> playerRewards = getPlayerRewards(pd);
            playerRewards.removeIf(reward -> allRewards.contains(reward));

            allRewards.addAll(playerRewards);
        });

        return allRewards;
    }

    public static List<Reward> getPlayerRewards(PlayerData playerdata)
    {
        final List<Reward> rewards = playerdata.getData("rewards");

        if (rewards.removeIf(reward -> reward.isExpired()))
        {
            playerdata.setData("rewards", rewards).queue();
        }

        return rewards;
    }

    public static List<Reward> getPlayerRewards(OfflinePlayer player)
    {
        return getPlayerRewards(PlayerData.from(player));
    }

    public static Reward deserialize(Map<String, Object> rewardMap)
    {
        final List<ItemStack> items = (List<ItemStack>) rewardMap.get("items");
        final String title = (String) Optional.ofNullable(rewardMap.get("title")).orElse(null);
        final String description = (String) Optional.ofNullable(rewardMap.get("description")).orElse(null);
        final boolean awardToAll = (boolean) rewardMap.get("awardToAll");
        final long expiration = (long) Optional.ofNullable(rewardMap.get("expiration")).orElse(-1L);
        final Material giftBox = (Material) Optional.ofNullable(rewardMap.get("giftBox")).orElse(null);
        final long timestamp = (long) rewardMap.get("timestamp");
        final List<OfflinePlayer> recipients = (List<OfflinePlayer>) rewardMap.get("recipients");
        final UUID uuid = (UUID) rewardMap.get("uuid");

        final Reward reward = new Reward(items, title, description, awardToAll, expiration, giftBox);
        reward.timestamp = timestamp;
        reward.recipients = recipients;
        reward.uuid = uuid;

        return reward;
    }

    public Map<String, Object> serialize()
    {
        final Map<String, Object> reward = new HashMap<>();
        reward.put("item", this.items);
        reward.put("title", this.title);
        reward.put("description", this.description);
        reward.put("awardToAll", this.awardToAll);
        reward.put("expiration", this.expiration);
        reward.put("giftBox", this.giftBox);
        reward.put("timestamp", this.timestamp);
        reward.put("recipients", this.recipients);
        reward.put("uuid", this.uuid);

        return reward;
    }

    public void grant(PlayerData playerdata)
    {
        if (!playerdata.isOnline()) return;
		
		final Player player = playerdata.getPlayer();
		
		// Copy of player's inventory
		// Used to check if they have enough room for the reward
		final Inventory playerInventoryCopy = Bukkit.createInventory(null, InventoryType.PLAYER);
		playerInventoryCopy.setContents(player.getInventory().getContents());

        List<ItemStack> box = this.boxReward();
        for (final ItemStack boxedItem : box)
        {
            // addItem() returns a HashMap of ItemStack that couldn't be added
            if (playerInventoryCopy.addItem(boxedItem).size() == 0) continue;
            
            playerdata.sendMessage(ChatColor.RED + "You do not have enough inventory space to claim this reward.");
            playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);

            return;
        }

        PlayerInventory playerInventory = player.getInventory();
        box.forEach(item -> playerInventory.addItem(item));

		playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);

        // Remove reward from player
        playerdata.setData("rewards", getPlayerRewards(playerdata).remove(this));
		
    }
    
    public void grant(OfflinePlayer player)
    {
        grant(PlayerData.from(player));
    }

    public List<ItemStack> getItems()
    {
        return this.items;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getDescription()
    {
        return this.description;
    }

    public long getTimestamp()
    {
        return this.timestamp;
    }

    public UUID getUUID()
    {
        return this.uuid;
    }

    public long getExpiration()
    {
        return this.expiration;
    }

    public Material getGiftBox()
    {
        return this.giftBox;
    }

    public boolean isExpired()
    {
        if (this.expiration == -1) return false;
        if (this.expiration - new Date().getTime() >= 0) return false;

        return true;
    }

    public List<ItemStack> boxReward()
    {
        if (this.giftBox == null) return this.items;

        final ItemStack box = new ItemStack(this.giftBox);
        
        if (this.giftBox == Material.BUNDLE)
        {
            BundleMeta meta = (BundleMeta) box.getItemMeta();
            this.items.forEach(item -> meta.addItem(item));

            box.setItemMeta(meta);
        } else if (this.giftBox == Material.SHULKER_BOX)
        {
            BlockStateMeta meta = (BlockStateMeta) box.getItemMeta();
            ShulkerBox state = (ShulkerBox) meta.getBlockState();
            Inventory giftBoxInventory = state.getInventory();
            this.items.forEach(item -> giftBoxInventory.addItem(item));

            state.update();
            meta.setBlockState(state);
            box.setItemMeta(meta);
        }

        return List.of(box);
    }
    
}
