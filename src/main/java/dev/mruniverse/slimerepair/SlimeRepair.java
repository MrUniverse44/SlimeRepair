package dev.mruniverse.slimerepair;

import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.SlimePluginInformation;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import dev.mruniverse.slimelib.loader.DefaultSlimeLoader;
import dev.mruniverse.slimelib.logs.SlimeLogger;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import dev.mruniverse.slimerepair.commands.PluginCommand;
import dev.mruniverse.slimerepair.commands.RepairCommand;
import dev.mruniverse.slimerepair.groups.SlimeGroups;
import dev.mruniverse.slimerepair.ranks.PermissionPlugin;
import dev.mruniverse.slimerepair.ranks.RankStorage;
import dev.mruniverse.slimerepair.ranks.supports.LuckPerms;
import dev.mruniverse.slimerepair.ranks.supports.None;
import dev.mruniverse.slimerepair.ranks.supports.Vault;
import dev.mruniverse.slimerepair.sounds.SoundManager;
import dev.mruniverse.slimerepair.utils.PluginUtils;
import dev.mruniverse.slimerepair.utils.RepairUtil;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SlimeRepair extends JavaPlugin implements SlimePlugin<JavaPlugin> {

    private final SlimeLogs logs = SlimeLogger.createLogs(getServerType(), this, "SlimeRepair");

    private final SlimePluginInformation information = new SlimePluginInformation(getServerType(), this);

    private final DefaultSlimeLoader<JavaPlugin> loader = new DefaultSlimeLoader<>(this);

    private PermissionPlugin permissionPlugin;

    private SoundManager soundManager;

    private SlimeGroups groupManager;

    private RankStorage rankStorage;

    private RepairUtil repairUtil;

    private Economy economy;

    @Override
    public void onEnable() {
        PluginUtils.setupLogger(logs);

        loader.setFiles(SlimeFile.class);

        loader.init();

        setupEconomy();

        soundManager = new SoundManager(this);

        repairUtil = new RepairUtil(this);

        groupManager = new SlimeGroups(this);

        rankStorage = new RankStorage(this);

        permissionPlugin = detectPermissionPlugin();

        loader.getCommands().register(new RepairCommand(this));
        loader.getCommands().register(new PluginCommand(this));

        Metrics metrics = new Metrics(this, 16592);

        getLogs().info("Metrics has been enabled (" + metrics.isEnabled() + ")");
    }

    @Override
    public void onDisable() {
        loader.shutdown();
    }

    private PermissionPlugin detectPermissionPlugin() {
        if (getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            Plugin plugin = getServer().getPluginManager().getPlugin("LuckPerms");

            if(plugin != null) {

                getLogs().info("&6Connected with LuckPerms v" + plugin.getDescription().getVersion() +  "&6 Support!");
                return new LuckPerms(
                        getLogs(),
                        plugin.getDescription().getVersion()
                );

            } else {
                getLogs().info("Can't find LuckPerms");
                return new None();
            }
        } else if (getServer().getPluginManager().isPluginEnabled("Vault")) {

            RegisteredServiceProvider<Permission> serviceProvider = getServer().getServicesManager().getRegistration(Permission.class);

            Plugin plugin = getServer().getPluginManager().getPlugin("Vault");

            if (serviceProvider != null && plugin != null) {
                getLogs().info("&6Connected with Vault Support!");
                return new Vault(serviceProvider.getProvider(), plugin.getDescription().getVersion());
            }

            getLogs().info("Can't find Vault");
            return new None();
        } else {
            return new None();
        }
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }

        RegisteredServiceProvider<Economy> serviceProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (serviceProvider == null) {
            return;
        }

        economy = serviceProvider.getProvider();
    }

    @Override
    public SlimePlatform getServerType() {
        return SlimePlatform.SPIGOT;
    }

    @Override
    public SlimeLogs getLogs() {
        return logs;
    }

    @Override
    public SlimePluginInformation getPluginInformation() {
        return information;
    }

    @Override
    public BaseSlimeLoader<JavaPlugin> getLoader() {
        return loader;
    }

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }

    public Economy getEconomy() {
        return economy;
    }

    public PermissionPlugin getPermissionPlugin() {
        return permissionPlugin;
    }

    public RankStorage getRankStorage() {
        return rankStorage;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public SlimeGroups getGroupManager() {
        return groupManager;
    }

    public RepairUtil getRepairUtil() {
        return repairUtil;
    }

    @Override
    public void reload() {
        loader.reload();
        groupManager.update();
    }
}
