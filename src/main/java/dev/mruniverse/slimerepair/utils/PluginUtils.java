package dev.mruniverse.slimerepair.utils;

import dev.mruniverse.slimelib.logs.SlimeLogs;

public class PluginUtils {
    public static void setupLogger(SlimeLogs logs) {
        logs.getSlimeLogger().setHidePackage("dev.mruniverse.slimerepair.");
        logs.getSlimeLogger().setContainIdentifier("dev.mruniverse.slimerepair");
        logs.getSlimeLogger().setPluginName("SlimeRepair");

        logs.getProperties().getExceptionProperties().BASE_COLOR = "&e";

        logs.getPrefixes().getIssue().setPrefix("&cSlimeRepair | &7");
        logs.getPrefixes().getWarn().setPrefix("&eSlimeRepair | &7");
        logs.getPrefixes().getDebug().setPrefix("&9SlimeRepair | &7");
        logs.getPrefixes().getInfo().setPrefix("&aSlimeRepair | &7");
    }
}
