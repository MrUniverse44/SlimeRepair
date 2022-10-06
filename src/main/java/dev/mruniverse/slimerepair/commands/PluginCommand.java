package dev.mruniverse.slimerepair.commands;

import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.source.SlimeSource;
import dev.mruniverse.slimerepair.SlimeRepair;

import java.util.ArrayList;
import java.util.List;

@Command(
        description = "This command is used for repair the item in player's hand",
        usage = "/slimerepair [reload]"
)
public class PluginCommand implements SlimeCommand {

    private final List<String> aliases = new ArrayList<>();

    private final SlimeRepair plugin;

    public PluginCommand(SlimeRepair plugin) {
        this.plugin = plugin;
        aliases.add("slimer");
        aliases.add("slrepair");
    }

    @Override
    public String getCommand() {
        return "slimerepair";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(SlimeSource sender, String commandLabel, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendColoredMessage("&aSlimeRepair created by JustJustin with love &d♥");
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("slimerepair.admin")) {
                sender.sendColoredMessage("&aReloading SlimeRepair..");
                plugin.reload();
                sender.sendColoredMessage("&aSlimeRepair v" + plugin.getDescription().getVersion());
            }
        }
    }
}
