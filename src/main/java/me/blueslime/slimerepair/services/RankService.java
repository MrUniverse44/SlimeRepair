package me.blueslime.slimerepair.services;

import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;
import me.blueslime.bukkitmeteor.logs.MeteorLogger;
import me.blueslime.slimerepair.ranks.PermissionPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankService implements AdvancedModule {

    private List<String> primaryGroupFindingList;

    public static final String DEFAULT_GROUP = "NONE";

    private boolean groupsByPermissions;

    private boolean usePrimaryGroup;

    @Override
    public void initialize() {
        FileConfiguration settings = fetch(FileConfiguration.class);

        this.usePrimaryGroup = settings.getBoolean("settings.use-primary-group", true);
        this.groupsByPermissions = settings.getBoolean("settings.assign-groups-by-permissions", false);
        this.primaryGroupFindingList = new ArrayList<>();

        List<String> groups = settings.getStringList("settings.primary-group-finding-list");

        if (!groups.isEmpty()) {
            for (Object group : groups) {
                primaryGroupFindingList.add(group.toString());
            }
        } else {
            for (Object group : Arrays.asList("Owner", "Admin", "Helper", "default")) {
                primaryGroupFindingList.add(group.toString());
            }
        }
    }

    @Override
    public void reload() {
        primaryGroupFindingList.clear();

        initialize();
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
            return fetch(PermissionPlugin.class).getPrimaryGroup(player);
        } catch (Exception e) {
            fetch(MeteorLogger.class).error(e, "Failed to get permission groups of " + player.getName() + " using " + fetch(PermissionPlugin.class).getName() + " v" + fetch(PermissionPlugin.class).getVersion());
            return DEFAULT_GROUP;
        }
    }

    public String getFromList(Player player) {
        try {
            String[] playerGroups = fetch(PermissionPlugin.class).getAllGroups(player);
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
            fetch(MeteorLogger.class).error(e, "Failed to get permission groups of " + player.getName() + " using " + fetch(PermissionPlugin.class).getName() + " v" + fetch(PermissionPlugin.class).getVersion());
            return DEFAULT_GROUP;
        }
    }

    public String getByPermission(Player player) {
        for (Object group : primaryGroupFindingList) {
            if (player.hasPermission("slimerepair.group." + group)) {
                return String.valueOf(group);
            }
        }
        fetch(MeteorLogger.class).error("Player " + player.getName() + " does not have any group permission while assign-groups-by-permissions is enabled! Did you forget to add his group to primary-group-finding-list?");
        return DEFAULT_GROUP;
    }

    public boolean isGroupsByPermissions() {
        return groupsByPermissions;
    }

    public boolean isUsePrimaryGroup() {
        return usePrimaryGroup;
    }
}
