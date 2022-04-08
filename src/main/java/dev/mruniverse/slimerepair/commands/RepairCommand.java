package dev.mruniverse.slimerepair.commands;

import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.commands.sender.Sender;
import dev.mruniverse.slimelib.commands.sender.player.SlimePlayer;
import dev.mruniverse.slimerepair.SlimeFile;
import dev.mruniverse.slimerepair.SlimeRepair;
import dev.mruniverse.slimerepair.utils.RepairUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Command(
        description = "This command is used for repair the item in player's hand",
        usage = "/repair [hand|all]"
)
public class RepairCommand implements SlimeCommand {

    private final List<String> aliases = new ArrayList<>();

    private final SlimeRepair plugin;

    public RepairCommand(SlimeRepair plugin) {
        this.plugin = plugin;
        aliases.add("fix");
        aliases.add("repair-hand");
        aliases.add("fix-hand");
    }

    @Override
    public String getCommand() {
        return "repair";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(Sender sender, String label, String[] args) {
        if (!(sender instanceof SlimePlayer)) {
            sender.sendColoredMessage(
                    plugin.getLoader().getFiles().getControl(SlimeFile.MESSAGES).getString(
                            "messages.error.console",
                            "&cYou must be a player to execute that command!")
            );
        }
        if (sender instanceof SlimePlayer) {
            Player player = ((SlimePlayer) sender).get();

            if (args.length == 0 || args[0].equalsIgnoreCase("hand")) {
                plugin.getRepairUtil().repair(RepairUtil.RepairMode.HAND, player);
                return;
            }
            if (args[0].equalsIgnoreCase("all")) {
                plugin.getRepairUtil().repair(RepairUtil.RepairMode.ALL, player);
            }
        }
    }
}
