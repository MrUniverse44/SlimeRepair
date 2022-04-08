package dev.mruniverse.slimerepair.groups;

import dev.mruniverse.slimelib.control.Control;

import java.util.List;

public class SlimeGroup {

    private final List<String> aliases;

    private final String main;

    private final int cost;

    public SlimeGroup(Control control, String main) {
        this.aliases = control.getStringList("aliases");
        this.cost = control.getInt("cost", 10);
        this.main = main;
    }

    public boolean check(String text) {
        if (main.equalsIgnoreCase(text)) {
            return true;
        }
        return aliases.contains(text);
    }

    public int getCost() {
        return cost;
    }
}
