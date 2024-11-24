package me.blueslime.slimerepair.commands;

import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;
import me.blueslime.bukkitmeteor.libs.utilitiesapi.commands.AdvancedCommand;
import me.blueslime.bukkitmeteor.libs.utilitiesapi.commands.sender.Sender;
import me.blueslime.slimerepair.SlimeRepair;
import me.blueslime.slimerepair.services.RepairService;
import me.blueslime.slimerepair.utils.RepairMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class RepairCommand extends AdvancedCommand<SlimeRepair> implements AdvancedModule {

    public RepairCommand(SlimeRepair plugin, String command) {
        super(plugin, command);
    }

    @Override
    public void executeCommand(Sender sender, String label, String[] args) {
        if (!sender.isPlayer()) {
            sender.send(
                fetch(FileConfiguration.class, "messages.yml"),
                "messages.error.console",
                "&cYou must be a player to use this command!"
            );
            return;
        }
        Player player = sender.toPlayer();

        if (args.length == 0 || args[0].equalsIgnoreCase("hand")) {
            fetch(RepairService.class).repair(RepairMode.HAND, player);
            return;
        }
        if (args[0].equalsIgnoreCase("all")) {
            fetch(RepairService.class).repair(RepairMode.ALL, player);
        }
    }

    @Override
    public boolean overwriteCommand() {
        return true;
    }
}
