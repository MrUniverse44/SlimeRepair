package me.blueslime.slimerepair.sounds;

@SuppressWarnings("unused")
public enum PluginSound {
    HAND("repair-hand.sound"),
    ALL("repair-all.sound"),
    RELOAD("reload.sound"),
    ERROR("error.sound");

    private final String path;

    PluginSound(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}