package me.blueslime.slimerepair.ranks.supports;

import me.blueslime.slimerepair.ranks.PermissionPlugin;
import org.bukkit.entity.Player;

/**
 * This class will be used when the plugin don't detect a permission plugin running in the server.
 * or an unsupported permission plugin.
 */
public class None implements PermissionPlugin {


    public String getPrimaryGroup(Player player) {
        return "<null>";
    }


    public String[] getAllGroups(Player player) {
        return new String[] {
                "<null>"
        };
    }


    public String getName() {
        return "Unknown/None";
    }


    public String getVersion() {
        return "-";
    }
}
