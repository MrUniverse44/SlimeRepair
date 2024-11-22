package me.blueslime.slimerepair.ranks.supports;

import me.blueslime.bukkitmeteor.implementation.Implements;
import me.blueslime.bukkitmeteor.logs.MeteorLogger;
import me.blueslime.slimerepair.ranks.PermissionPlugin;
import me.blueslime.slimerepair.services.RankService;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;

import org.bukkit.entity.Player;

public class LuckPerms implements PermissionPlugin {

    private static final String UPDATE_MESSAGE = "Please install a newest version of LuckPerms, this version is outdated, support for: LuckPerms5 or higher";

    private final String version;

    /**
     * Constructs new instance with given parameter
     * @param version - luckPerms version
     */
    public LuckPerms(String version) {
        this.version = version;
    }


    public String getPrimaryGroup(Player p) {
        if (version.startsWith("4")) return UPDATE_MESSAGE;
        net.luckperms.api.LuckPerms api = getAPI();
        if (api == null) {
            return RankService.DEFAULT_GROUP;
        }
        User user = api.getUserManager().getUser(p.getUniqueId());
        if (user == null) return RankService.DEFAULT_GROUP;
        return user.getPrimaryGroup();
    }


    public String[] getAllGroups(Player p) {
        if (version.startsWith("4")) {
            return new String[]{
                UPDATE_MESSAGE
            };
        }

        net.luckperms.api.LuckPerms api = getAPI();

        if (api == null) {
            return new String[]{
                RankService.DEFAULT_GROUP
            };
        }

        User user = api.getUserManager().getUser(p.getUniqueId());

        if (user == null) {
            return new String[]{
                RankService.DEFAULT_GROUP
            };
        }

        return user.getNodes().stream().filter(NodeType.INHERITANCE::matches).map(NodeType.INHERITANCE::cast).map(InheritanceNode::getGroupName).distinct().toArray(String[]::new);
    }

    private net.luckperms.api.LuckPerms getAPI() {
        try {
            return LuckPermsProvider.get();
        } catch (Exception e) {
            Implements.fetch(MeteorLogger.class).error(e, "LuckPerms v" + version + " threw an exception when retrieving API instance: " + e.getMessage());
            return null;
        }
    }


    public String getVersion() {
        return version;
    }
}