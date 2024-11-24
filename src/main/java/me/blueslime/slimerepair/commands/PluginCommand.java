package me.blueslime.slimerepair.commands;

import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;
import me.blueslime.bukkitmeteor.libs.utilitiesapi.commands.AdvancedCommand;
import me.blueslime.bukkitmeteor.libs.utilitiesapi.commands.sender.Sender;
import me.blueslime.slimerepair.SlimeRepair;

public class PluginCommand extends AdvancedCommand<SlimeRepair> implements AdvancedModule {

    public PluginCommand(SlimeRepair plugin) {
        super(plugin, "slimerepair");
    }

    @Override
    public void executeCommand(Sender sender, String commandLabel, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.send("&aSlimeRepair created by JustJustin with love &d♥");
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("slimerepair.admin")) {
                sender.send("&aReloading SlimeRepair..");
                plugin.reload();
                sender.send("&aSlimeRepair v" + plugin.getDescription().getVersion());
            }
        }
    }

    @Override
    public boolean overwriteCommand() {
        return true;
    }
}
