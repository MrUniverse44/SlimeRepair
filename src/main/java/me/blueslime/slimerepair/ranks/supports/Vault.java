package me.blueslime.slimerepair.ranks.supports;

import me.blueslime.slimerepair.ranks.PermissionPlugin;
import org.bukkit.entity.Player;
import net.milkbowl.vault.permission.Permission;

public class Vault implements PermissionPlugin {

    private final Permission permission;

    private final String vaultVersion;

    public Vault(Permission permission, String vaultVersion) {
        this.permission = permission;
        this.vaultVersion = vaultVersion;
    }


    public String getPrimaryGroup(Player player) {
        return permission.getPrimaryGroup(player);
    }


    public String[] getAllGroups(Player player) {
        return permission.getPlayerGroups(player);
    }


    public String getName() {
        return permission.getName();
    }


    public String getVersion() {
        return "Vault " + vaultVersion;
    }
}
