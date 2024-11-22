package me.blueslime.slimerepair.services;

import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;
import me.blueslime.bukkitmeteor.logs.MeteorLogger;
import me.blueslime.slimerepair.groups.SlimeGroup;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class GroupService implements AdvancedModule {

    private final List<SlimeGroup> handGroupList = new ArrayList<>();
    private final List<SlimeGroup> allGroupList = new ArrayList<>();

    private SlimeGroup handDefault = null;
    private SlimeGroup allDefault = null;

    @Override
    public void initialize() {
        load();
    }

    @Override
    public void reload() {
        load();
    }

    public SlimeGroup getHandUserRank(String rank) {
        for (SlimeGroup group : handGroupList) {
            if (group.check(rank)) {
                return group;
            }
        }
        return handDefault;
    }

    public SlimeGroup getAllUserRank(String rank) {
        for (SlimeGroup group : allGroupList) {
            if (group.check(rank)) {
                return group;
            }
        }
        return allDefault;
    }

    private void load() {
        handGroupList.clear();
        allGroupList.clear();

        FileConfiguration control = fetch(FileConfiguration.class, "settings.yml");

        String handPath = "costs.repair-in-hand";

        final MeteorLogger logs = fetch(MeteorLogger.class);

        ConfigurationSection handSection = control.getConfigurationSection(handPath);

        if (handSection != null) {
            for (String main : handSection.getKeys(false)) {
                String section = handPath + "." + main;
                handGroupList.add(
                        new SlimeGroup(
                            control.getConfigurationSection(section),
                            main
                        )
                );

                logs.info("&aGroup &b" + main + "&a loaded for &dHand-Repair-Groups");

                if (main.equalsIgnoreCase("default")) {
                    handDefault = new SlimeGroup(
                        control.getConfigurationSection(section),
                        main
                    );
                }
            }
        }

        String allPath = "costs.repair-all";

        ConfigurationSection allSection = control.getConfigurationSection(allPath);

        if (allSection == null) {
            return;
        }

        for (String main : allSection.getKeys(false)) {
            String section = allPath + "." + main;
            allGroupList.add(
                new SlimeGroup(
                    control.getConfigurationSection(section),
                    main
                )
            );

            logs.info("&aGroup &b" + main + "&a loaded for &dAll-Repair-Groups");

            if (main.equalsIgnoreCase("default")) {
                allDefault = new SlimeGroup(
                    control.getConfigurationSection(section),
                    main
                );
            }
        }
    }

}
