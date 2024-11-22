package me.blueslime.slimerepair.groups;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class SlimeGroup {

    private final List<String> aliases;

    private final String main;

    private final int cost;

    public SlimeGroup(ConfigurationSection control, String main) {
        if (control != null) {
            this.aliases = control.getStringList("aliases");
            this.cost = control.getInt("cost", 10);
        } else {
            this.aliases = new ArrayList<>();
            this.cost = 1000;
        }
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
