package dev.mruniverse.slimerepair.groups;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import dev.mruniverse.slimerepair.SlimeFile;
import dev.mruniverse.slimerepair.SlimeRepair;

import java.util.ArrayList;
import java.util.List;

public class SlimeGroups {

    private final List<SlimeGroup> handGroupList = new ArrayList<>();
    private final List<SlimeGroup> allGroupList = new ArrayList<>();

    private SlimeGroup handDefault = null;

    private SlimeGroup allDefault = null;

    private final SlimeRepair plugin;

    public SlimeGroups(SlimeRepair plugin) {
        this.plugin = plugin;
        load();
    }

    public void update() {
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

        ConfigurationHandler control = plugin.getConfigurationHandler(SlimeFile.SETTINGS);

        String handPath = "costs.repair-in-hand";

        final SlimeLogs logs = plugin.getLogs();

        for (String main : control.getContent(handPath, false)) {
            String section = handPath + "." + main;
            handGroupList.add(
                new SlimeGroup(
                        control.getSection(section),
                        main
                )
            );

            logs.info("&aGroup &b" + main + "&a loaded for &dHand-Repair-Groups");

            if (main.equalsIgnoreCase("default")) {
                handDefault = new SlimeGroup(
                        control.getSection(section),
                        main
                );
            }
        }

        String allPath = "costs.repair-all";

        for (String main : control.getContent(allPath, false)) {
            String section = allPath + "." + main;
            allGroupList.add(
                    new SlimeGroup(
                            control.getSection(section),
                            main
                    )
            );

            logs.info("&aGroup &b" + main + "&a loaded for &dAll-Repair-Groups");

            if (main.equalsIgnoreCase("default")) {
                allDefault = new SlimeGroup(
                        control.getSection(section),
                        main
                );
            }
        }

    }

}
