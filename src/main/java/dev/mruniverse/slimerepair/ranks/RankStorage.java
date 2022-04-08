package dev.mruniverse.slimerepair.ranks;

import dev.mruniverse.slimelib.control.Control;
import dev.mruniverse.slimerepair.SlimeFile;
import dev.mruniverse.slimerepair.SlimeRepair;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankStorage {
    private final List<String> primaryGroupFindingList;

    public static final String DEFAULT_GROUP = "NONE";

    private final boolean groupsByPermissions;

    private final boolean usePrimaryGroup;

    private final SlimeRepair plugin;

    public RankStorage(SlimeRepair plugin) {
        this.plugin = plugin;
        Control settings = plugin.getLoader().getFiles().getControl(SlimeFile.SETTINGS);

        usePrimaryGroup = settings.getStatus("settings.use-primary-group", true);
        groupsByPermissions = settings.getStatus("settings.assign-groups-by-permissions", false);
        primaryGroupFindingList = new ArrayList<>();

        List<String> groups = settings.getStringList("settings.primary-group-finding-list");

        if(!groups.isEmpty()) {
            for (Object group : groups) {
                primaryGroupFindingList.add(group.toString());
            }
        } else {
            for (Object group : Arrays.asList("Owner", "Admin", "Helper", "default")) {
                primaryGroupFindingList.add(group.toString());
            }
        }
    }

    public String detectPermissionGroup(Player player) {
        if (isGroupsByPermissions()) {
            return getByPermission(player);
        }
        if (isUsePrimaryGroup()) {
            return getByPrimary(player);
        }
        return getFromList(player);
    }

    public String getByPrimary(Player player) {
        try {
            return plugin.getPermissionPlugin().getPrimaryGroup(player);
        } catch (Exception e) {
            plugin.getLogs().error("Failed to get permission groups of " + player.getName() + " using " + plugin.getPermissionPlugin().getName() + " v" + plugin.getPermissionPlugin().getVersion(), e);
            return DEFAULT_GROUP;
        }
    }

    public String getFromList(Player player) {
        try {
            String[] playerGroups = plugin.getPermissionPlugin().getAllGroups(player);
            if (playerGroups != null && playerGroups.length > 0) {
                for (String groupFromList : primaryGroupFindingList) {
                    for (String playerGroup : playerGroups) {
                        if (playerGroup.equalsIgnoreCase(groupFromList)) {
                            return playerGroup;
                        }
                    }
                }
                return playerGroups[0];
            } else {
                return DEFAULT_GROUP;
            }
        } catch (Exception e) {
            plugin.getLogs().error("Failed to get permission groups of " + player.getName() + " using " + plugin.getPermissionPlugin().getName() + " v" + plugin.getPermissionPlugin().getVersion(), e);
            return DEFAULT_GROUP;
        }
    }

    public String getByPermission(Player player) {
        for (Object group : primaryGroupFindingList) {
            if (player.hasPermission("guardiankitpvp.tab.group." + group)) {
                return String.valueOf(group);
            }
        }
        plugin.getLogs().error("Player " + player.getName() + " does not have any group permission while assign-groups-by-permissions is enabled! Did you forget to add his group to primary-group-finding-list?");
        return DEFAULT_GROUP;
    }

    public boolean isGroupsByPermissions() {
        return groupsByPermissions;
    }

    public boolean isUsePrimaryGroup() {
        return usePrimaryGroup;
    }
}
