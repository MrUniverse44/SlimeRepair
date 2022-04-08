package dev.mruniverse.slimerepair.ranks;

import org.bukkit.entity.Player;

public interface PermissionPlugin {
    /**
     * Returns primary permission group of player
     * @param player - player to get group of
     * @return player's primary permission group
     */
    String getPrimaryGroup(final Player player);

    /**
     * Returns list of all groups players is in
     * @param player - player to check groups of
     * @return list of all groups of player
     */
    String[] getAllGroups(final Player player);

    /**
     * Returns version of the permission plugin
     * @return version of the permission plugin
     */
    String getVersion();

    /**
     * Returns name of the permission plugin
     * @return name of the permission plugin
     */
    default String getName() {
        return getClass().getSimpleName();
    }
}
