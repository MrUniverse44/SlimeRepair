package dev.mruniverse.slimerepair.sounds;

import dev.mruniverse.slimelib.control.Control;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import dev.mruniverse.slimerepair.SlimeFile;
import dev.mruniverse.slimerepair.SlimeRepair;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;

@SuppressWarnings("unused")
public class SoundManager {
    private final SlimeRepair plugin;

    private final Random random = new Random();

    private final HashMap<PluginSound, Boolean> soundStatus = new HashMap<>();
    private final HashMap<PluginSound, Sound> sounds = new HashMap<>();
    private final HashMap<PluginSound, Float> pitch = new HashMap<>();
    private final HashMap<PluginSound, Float> volume = new HashMap<>();

    public static void playSound(SlimeLogs logs, Player player, String sound) {
        try {
            player.playSound(player.getLocation(),Sound.valueOf(sound),2.0F,1.0F);
        }catch (IllegalArgumentException ignored) {
            logs.error("The sound: " + sound + " doesn't exists.");
        }
    }

    public void play(Player player, PluginSound sound) {
        checkForNull(sound);

        if (soundStatus.get(sound)) {
            player.playSound(
                    player.getLocation(),
                    sounds.get(sound),
                    volume.get(sound),
                    pitch.get(sound)
            );
        }
    }

    public SoundManager(SlimeRepair plugin) {
        this.plugin = plugin;
        Control file = plugin.getLoader().getFiles().getControl(SlimeFile.SETTINGS);

        try {
            for (PluginSound sound : PluginSound.values()) {
                updateSound(sound);
            }
        } catch (Exception ignored) {
            SlimeLogs logs = plugin.getLogs();
            logs.error(" ");
            logs.error("Plugin sounds in sounds.yml aren't correct in your version, please put correct sounds and restart your server");
            logs.error("To prevent issues");
            logs.error(" ");
        }
    }

    public void unload() {
        soundStatus.clear();
        sounds.clear();
        pitch.clear();
        volume.clear();
    }

    public Sound getSound(PluginSound sound) {
        if(sounds.get(sound) == null) {
            updateSound(sound);
        }

        checkForNull(sound);

        return sounds.get(sound);
    }
    public void checkForNull(PluginSound sound) {

        SlimeLogs logs = plugin.getLogs();

        if(!sounds.containsKey(sound)) {
            Sound sd = random();
            logs.info("Sound of '" + sound.toString() + "' has been changed to " + sd.toString());
            logs.info("Because the sound doesn't exists, if you want change this sound");
            sounds.put(sound,sd);
        }

        if(!soundStatus.containsKey(sound)) {
            logs.info("Status of '" + sound.toString() + "' has been changed to true");
            soundStatus.put(sound, true);
        }

        if(!pitch.containsKey(sound)) {
            logs.info("Pitch of '" + sound.toString() + "' has been changed to 0.5F");
            pitch.put(sound, 0.5F);
        }

        if(!volume.containsKey(sound)) {
            logs.info("Volume of '" + sound.toString() + "' has been changed to 1.0F");
            volume.put(sound, 1.0F);
        }

    }

    private Sound random() {
        int x = random.nextInt(Sound.class.getEnumConstants().length);
        return Sound.class.getEnumConstants()[x];
    }

    public void changeSound(PluginSound sound1,Sound sound2){
        sounds.put(sound1,sound2);
    }

    public Float getPitch(PluginSound sound) {
        if(!pitch.containsKey(sound)) {
            updateSound(sound);
        }
        return pitch.get(sound);
    }

    public Float getVolume(PluginSound sound) {
        if(!volume.containsKey(sound)) {
            updateSound(sound);
        }
        return volume.get(sound);
    }

    public boolean getStatus(PluginSound sound) {
        if(!soundStatus.containsKey(sound)) {
            updateSound(sound);
        }
        return soundStatus.get(sound);
    }

    public void updateSound(PluginSound sound) {
        Control file = plugin.getLoader().getFiles().getControl(SlimeFile.SETTINGS);

        soundStatus.put(
                sound,
                file.getStatus(sound.getStatusPath(), true)
        );

        sounds.put(
                sound,
                Sound.valueOf(file.getString(sound.getSoundPath(), "NOTE_PLING"))
        );

        pitch.put(
                sound,
                Float.valueOf(file.getString(sound.getPitchPath(), "1.0F"))
        );

        volume.put(
                sound,
                Float.valueOf(file.getString(sound.getVolumePath(), "2.0F"))
        );
    }

    public void update() {
        soundStatus.clear();
        sounds.clear();
        pitch.clear();
        volume.clear();
        for(PluginSound sound : PluginSound.values()) {
            updateSound(sound);
        }
    }
}
