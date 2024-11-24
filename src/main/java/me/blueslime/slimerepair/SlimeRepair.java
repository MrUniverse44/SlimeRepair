package me.blueslime.slimerepair;

import me.blueslime.bukkitmeteor.BukkitMeteorPlugin;
import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;
import me.blueslime.bukkitmeteor.implementation.module.Module;
import me.blueslime.slimerepair.commands.PluginCommand;
import me.blueslime.slimerepair.commands.RepairCommand;
import me.blueslime.slimerepair.economy.EconomyProvider;
import me.blueslime.slimerepair.economy.providers.NeronProvider;
import me.blueslime.slimerepair.economy.providers.NoneProvider;
import me.blueslime.slimerepair.economy.providers.VaultProvider;
import me.blueslime.slimerepair.services.GroupService;
import me.blueslime.slimerepair.ranks.PermissionPlugin;
import me.blueslime.slimerepair.services.RankService;
import me.blueslime.slimerepair.ranks.supports.LuckPerms;
import me.blueslime.slimerepair.ranks.supports.None;
import me.blueslime.slimerepair.ranks.supports.Vault;
import me.blueslime.slimerepair.services.SoundService;
import me.blueslime.slimerepair.services.RepairService;
import me.blueslime.utilitiesapi.utils.consumer.PluginConsumer;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;

public class SlimeRepair extends BukkitMeteorPlugin implements AdvancedModule {

    @Override
    public void onEnable() {
        initialize(this, false, true);
    }

    @Override
    public void onDisable() {
        shutdown();
    }

    /**
     * Here you can use the {@link #registerModule(Module...)}} or {@link #registerModule(Class[])}
     * This method is automatically used internally.
     */
    @Override
    public void registerModules() {
        registerImpl(
            FileConfiguration.class,
            "messages.yml",
            load(new File(getDataFolder(), "messages.yml"), "messages.yml"),
            true
        );

        registerModule(
            SoundService.class,
            RepairService.class,
            GroupService.class,
            RankService.class
        ).finish();

        registerImpl(SlimeRepair.class, this, true);

        setupEconomy();

        registerImpl(PermissionPlugin.class, detectPermissionPlugin(), true);

        new PluginCommand(this).register();

        for (String command : fetch(FileConfiguration.class, "settings.yml").getStringList("commands-labels")) {
            new RepairCommand(this, command).register();
        }

        new Metrics(this, 16592);

        getLogs().info("Metrics has been enabled");
        a();
    }

    private PermissionPlugin detectPermissionPlugin() {
        if (getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            Plugin plugin = getServer().getPluginManager().getPlugin("LuckPerms");

            if (plugin != null) {
                info("&6Connected with LuckPerms v" + plugin.getDescription().getVersion() +  "&6 Support!");
                return new LuckPerms(plugin.getDescription().getVersion());
            }

            info("Can't find LuckPerms");
            return new None();
        } else if (getServer().getPluginManager().isPluginEnabled("Vault")) {

            RegisteredServiceProvider<Permission> serviceProvider = getServer().getServicesManager().getRegistration(Permission.class);
            Plugin plugin = getServer().getPluginManager().getPlugin("Vault");

            if (serviceProvider != null && plugin != null) {
                info("&6Connected with Vault Support!");
                return new Vault(serviceProvider.getProvider(), plugin.getDescription().getVersion());
            }

            info("Can't find Vault");
            return new None();
        } else {
            return new None();
        }
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().isPluginEnabled("Neron")) {
            registerImpl(EconomyProvider.class, new NeronProvider(), true);
            return;
        }

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            registerImpl(EconomyProvider.class, new NoneProvider(), true);
            return;
        }

        RegisteredServiceProvider<Economy> serviceProvider = getServer().getServicesManager().getRegistration(Economy.class);

        if (serviceProvider == null) {
            registerImpl(EconomyProvider.class, new NoneProvider(), true);
            return;
        }

        registerImpl(EconomyProvider.class, new VaultProvider(), true);
    }
}
