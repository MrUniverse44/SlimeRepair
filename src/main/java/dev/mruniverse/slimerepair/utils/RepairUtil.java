package dev.mruniverse.slimerepair.utils;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimerepair.SlimeFile;
import dev.mruniverse.slimerepair.SlimeRepair;
import dev.mruniverse.slimerepair.groups.SlimeGroup;
import dev.mruniverse.slimerepair.sounds.PluginSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@SuppressWarnings("deprecation")
public class RepairUtil {

    private final Map<UUID, Long> timeouts = new HashMap<>();

    private final SlimeRepair plugin;

    public RepairUtil(SlimeRepair plugin) {
        this.plugin = plugin;
    }

    public void repair(RepairMode mode, Player player) {
        switch (mode) {
            case HAND:
                repairHand(player);
                break;
            default:
            case ALL:
                repairAll(player);
                break;
        }
    }

    private void repairHand(Player player) {
        long timeout = 0;

        if (timeouts.containsKey(player.getUniqueId())) {
            timeout = timeouts.get(player.getUniqueId());
        }

        ConfigurationHandler settings = plugin.getConfigurationHandler(SlimeFile.SETTINGS);
        ConfigurationHandler messages = plugin.getConfigurationHandler(SlimeFile.MESSAGES);

        if (player.getItemInHand().getType() == Material.AIR
                || player.getItemInHand().getType().isBlock()
                || player.getItemInHand().getType().isEdible()
                || player.getItemInHand().getType().getMaxDurability() <= 0
                || player.getItemInHand().getDurability() == 0) {
            MessageUtil.sendMessage(
                    player,
                    messages.getString("messages.error.item")
            );
            return;
        }

        String countdownPermission = settings.getString("permissions.bypass.countdown", "slimerepair.bp.countdown");
        String costPermission = settings.getString("permissions.bypass.cost", "slimerepair.bp.cost");

        if (!player.hasPermission(countdownPermission) && System.currentTimeMillis() < timeout) {

            String countdownMessage = messages.getString("messages.error.countdown", "&aYou cannot do that for another &e%time%&a seconds.");

            int timeLeft = (int)((timeout - System.currentTimeMillis()) / 1000);

            MessageUtil.sendMessage(
                    player,
                    countdownMessage.replace(
                            "%time%",
                            timeLeft + ""
                    )
            );

            return;
        }

        double cost = getCost(RepairMode.HAND, player);

        if (!player.hasPermission(costPermission)) {
            if (!plugin.getEconomy().has(player, cost)) {
                MessageUtil.sendMessage(
                        player,
                        messages.getString("messages.error.money")
                );
                return;
            }
        }

        if (!player.hasPermission(countdownPermission)) {
            timeouts.put(
                    player.getUniqueId(),
                    System.currentTimeMillis() + (settings.getLong("repair-hand.cooldown") * 1000)
            );
        }

        plugin.getEconomy().withdrawPlayer(player, cost);

        player.getItemInHand().setDurability((short) 0);
        player.updateInventory();

        plugin.getSoundManager().play(player, PluginSound.HAND);

        MessageUtil.sendMessage(
                player,
                messages.getString("messages.successful.repaired-hand", "&aSuccessfully repaired the item in your hand.")
        );
    }

    private void repairAll(Player player) {


        ConfigurationHandler settings = plugin.getConfigurationHandler(SlimeFile.SETTINGS);
        ConfigurationHandler messages = plugin.getConfigurationHandler(SlimeFile.MESSAGES);

        final List<ItemStack> queue = new ArrayList<>();

        for (ItemStack stack : player.getInventory().getContents()) {

            if (stack != null
                    && stack.getType() != Material.AIR
                    && !stack.getType().isBlock()
                    && !stack.getType().isEdible()
                    && stack.getType().getMaxDurability() > 0
                    && stack.getDurability() != 0) {

                queue.add(stack);
            }
        }

        Collections.addAll(queue, player.getInventory().getArmorContents());

        if (queue.isEmpty()) {
            MessageUtil.sendMessage(
                    player,
                    messages.getString("messages.error.items", "&dThere are no repairable items in your inventory.")
            );
            return;
        }

        long timeout = 0;

        String countdownPermission = settings.getString("permissions.bypass.countdown", "slimerepair.bp.countdown");
        String costPermission = settings.getString("permissions.bypass.cost", "slimerepair.bp.cost");

        if (!player.hasPermission(countdownPermission) && System.currentTimeMillis() < timeout) {

            String countdownMessage = messages.getString("messages.error.countdown", "&aYou cannot do that for another &e%time%&a seconds.");

            int timeLeft = (int)((timeout - System.currentTimeMillis()) / 1000);

            MessageUtil.sendMessage(
                    player,
                    countdownMessage.replace(
                            "%time%",
                            timeLeft + ""
                    )
            );

            return;
        }

        double cost = getCost(RepairMode.ALL, player);

        if (!player.hasPermission(costPermission)) {
            if (!plugin.getEconomy().has(player, cost)) {
                MessageUtil.sendMessage(
                        player,
                        messages.getString("messages.error.money")
                );
                return;
            }
        }

        if (!player.hasPermission(countdownPermission)) {
            timeouts.put(
                    player.getUniqueId(),
                    System.currentTimeMillis() + (settings.getLong("repair-all.cooldown") * 1000)
            );
        }

        plugin.getEconomy().withdrawPlayer(player, cost);

        for (ItemStack stack : queue) {
            stack.setDurability((short) 0);
        }

        player.updateInventory();

        plugin.getSoundManager().play(player, PluginSound.ALL);

        MessageUtil.sendMessage(
                player,
                messages.getString("messages.successful.repaired-all", "&aSuccessfully repaired everything in your inventory.")
        );
    }


    private double getCost(RepairMode mode, Player player) {
        switch (mode) {
            case ALL:
                return getCostAll(player);
            default:
            case HAND:
                return getCostHand(player);
        }
    }

    private double getCostAll(Player player) {
        String rank = getRank(player);

        SlimeGroup group = plugin.getGroupManager().getAllUserRank(rank);

        return group.getCost();
    }

    private double getCostHand(Player player) {
        String rank = getRank(player);

        SlimeGroup group = plugin.getGroupManager().getHandUserRank(rank);

        return group.getCost();
    }

    private String getRank(Player player) {
        return plugin.getRankStorage().detectPermissionGroup(player);
    }

    public enum RepairMode {
        HAND,
        ALL
    }


}
