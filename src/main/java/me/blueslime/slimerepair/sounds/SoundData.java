package me.blueslime.slimerepair.sounds;

import org.bukkit.Sound;

public class SoundData {
    private final boolean status;
    private final Float volume;
    private final Sound sound;
    private final Float pitch;

    public SoundData(final boolean status, final Float volume, final Sound sound, final Float pitch) {
        this.status = status;
        this.volume = volume;
        this.sound = sound;
        this.pitch = pitch;
    }

    public boolean isEnabled() {
        return status;
    }

    public Float getVolume() {
        return volume;
    }

    public Sound getSound() {
        return sound;
    }

    public Float getPitch() {
        return pitch;
    }
}
