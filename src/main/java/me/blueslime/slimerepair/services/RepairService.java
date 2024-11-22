package me.blueslime.slimerepair.services;

import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;
import me.blueslime.slimerepair.economy.EconomyProvider;
import me.blueslime.slimerepair.groups.SlimeGroup;
import me.blueslime.slimerepair.sounds.PluginSound;
import me.blueslime.slimerepair.utils.RepairMode;
import me.blueslime.utilitiesapi.commands.sender.Sender;
import me.blueslime.utilitiesapi.text.TextReplacer;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@SuppressWarnings("deprecation")
public class RepairService implements AdvancedModule {

    private final Map<UUID, Long> timeouts = new HashMap<>();

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

        FileConfiguration settings = fetch(FileConfiguration.class, "settings.yml");
        FileConfiguration messages = fetch(FileConfiguration.class, "messages.yml");

        if (player.getItemInHand().getType() == Material.AIR
                || player.getItemInHand().getType().isBlock()
                || player.getItemInHand().getType().isEdible()
                || player.getItemInHand().getType().getMaxDurability() <= 0
                || player.getItemInHand().getDurability() == 0) {
            Sender.build(player).send(
                player,
                messages,
                "messages.error.item",
                "messages.error.item"
            );
            return;
        }

        String countdownPermission = settings.getString("permissions.bypass.countdown", "slimerepair.bp.countdown");
        String costPermission = settings.getString("permissions.bypass.cost", "slimerepair.bp.cost");

        if (countdownPermission != null && !player.hasPermission(countdownPermission) && System.currentTimeMillis() < timeout) {
            int timeLeft = (int) ((timeout - System.currentTimeMillis()) / 1000);

            Sender.build(player).send(
                player,
                messages,
                "messages.error.countdown",
                "&aYou cannot do that for another &e%time%&a seconds.",
                TextReplacer.builder()
                    .replace("%time%", timeLeft)
            );

            return;
        }

        double cost = getCost(RepairMode.HAND, player);

        if (costPermission != null && !player.hasPermission(costPermission)) {
            if (!fetch(EconomyProvider.class).has(player, cost)) {
                Sender.build(player).send(
                    player,
                    messages,
                    "messages.error.money",
                    "messages.error.money"
                );
                return;
            }
        }

        if (countdownPermission != null && !player.hasPermission(countdownPermission)) {
            timeouts.put(
                player.getUniqueId(),
                System.currentTimeMillis() + (settings.getLong("repair-hand.cooldown") * 1000)
            );
        }

        fetch(EconomyProvider.class).withdrawPlayer(player, cost);

        player.getItemInHand().setDurability((short) 0);
        player.updateInventory();

        fetch(SoundService.class).play(player, PluginSound.HAND);

        Sender.build(player).send(
            player,
            messages,
            "messages.successful.repaired-hand",
            "&aSuccessfully repaired the item in your hand."
        );
    }

    private void repairAll(Player player) {

        FileConfiguration settings = fetch(FileConfiguration.class, "settings.yml");
        FileConfiguration messages = fetch(FileConfiguration.class, "messages.yml");

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
            Sender.build(player).send(
                player,
                messages,
                "messages.error.items",
                "&dThere are no repairable items in your inventory."
            );
            return;
        }

        long timeout = 0;

        String countdownPermission = settings.getString("permissions.bypass.countdown", "slimerepair.bp.countdown");
        String costPermission = settings.getString("permissions.bypass.cost", "slimerepair.bp.cost");

        if (countdownPermission != null && !player.hasPermission(countdownPermission) && System.currentTimeMillis() < timeout) {

            int timeLeft = (int) ((timeout - System.currentTimeMillis()) / 1000);

            Sender.build(player).send(
                player,
                messages,
                "messages.error.countdown",
                "&aYou cannot do that for another &e%time%&a seconds.",
                TextReplacer.builder()
                        .replace("%time%", timeLeft)
            );
            return;
        }

        double cost = getCost(RepairMode.ALL, player);

        if (costPermission != null && !player.hasPermission(costPermission)) {
            if (!fetch(EconomyProvider.class).has(player, cost)) {
                Sender.build(player).send(
                    player,
                    messages,
                    "messages.error.money",
                    "messages.error.money"
                );
                return;
            }
        }

        if (countdownPermission != null && !player.hasPermission(countdownPermission)) {
            timeouts.put(
                player.getUniqueId(),
                System.currentTimeMillis() + (settings.getLong("repair-all.cooldown") * 1000)
            );
        }

        fetch(EconomyProvider.class).withdrawPlayer(player, cost);

        for (ItemStack stack : queue) {
            stack.setDurability((short) 0);
        }

        player.updateInventory();

        fetch(SoundService.class).play(player, PluginSound.ALL);

        Sender.build(player).send(
            player,
            messages,
            "messages.successful.repaired-all",
            "&aSuccessfully repaired everything in your inventory."
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

        SlimeGroup group = fetch(GroupService.class).getAllUserRank(rank);

        return group.getCost();
    }

    private double getCostHand(Player player) {
        String rank = getRank(player);

        SlimeGroup group = fetch(GroupService.class).getHandUserRank(rank);

        return group.getCost();
    }

    private String getRank(Player player) {
        return fetch(RankService.class).detectPermissionGroup(player);
    }
}
