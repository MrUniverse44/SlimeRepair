package dev.mruniverse.slimerepair.sounds;

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

    public String getSoundPath() {
        return path + ".sound";
    }

    public String getPitchPath() {
        return path + ".pitch";
    }
    public String getVolumePath() {
       return path + ".volume";
    }
    public String getStatusPath() {
        return path + ".enabled";
    }
}