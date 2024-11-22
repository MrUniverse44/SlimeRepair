package me.blueslime.slimerepair.services;

import me.blueslime.bukkitmeteor.implementation.module.AdvancedModule;
import me.blueslime.bukkitmeteor.libs.utilitiesapi.utils.consumer.PluginConsumer;
import me.blueslime.bukkitmeteor.logs.MeteorLogger;
import me.blueslime.slimerepair.sounds.PluginSound;
import me.blueslime.slimerepair.sounds.SoundData;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class SoundService implements AdvancedModule {

    private final Map<PluginSound, SoundData> soundDataMap = new EnumMap<>(PluginSound.class);

    public void playSound(Player player, String sound) {
        try {
            player.playSound(player.getLocation(),Sound.valueOf(sound),2.0F,1.0F);
        }catch (IllegalArgumentException ignored) {
            fetch(MeteorLogger.class).error("The sound: " + sound + " doesn't exists.");
        }
    }

    public void play(Player player, PluginSound sound) {
        SoundData data = soundDataMap.computeIfAbsent(
            sound,
            key -> new SoundData(true, 1.0F, randomSound(), 0.5F)
        );

        if (!data.isEnabled()) {
            return;
        }

        player.playSound(
            player.getLocation(),
            data.getSound(),
            data.getVolume(),
            data.getPitch()
        );
    }

    @Override
    public void initialize() {
        try {
            for (PluginSound sound : PluginSound.values()) {
                updateSound(sound);
            }
        } catch (Exception ignored) {
            fetch(MeteorLogger.class).info(
                " ",
                "Plugin sounds in sounds.yml aren't correct in your version, please put correct sounds and restart your server",
                "Or please use our command reload argument when you finish your changes",
                " "
            );
        }
    }

    public void unload() {
        soundDataMap.clear();
    }

    private Sound randomSound() {
        int x = ThreadLocalRandom.current().nextInt(Sound.class.getEnumConstants().length);
        return Sound.class.getEnumConstants()[x];
    }

    public Optional<SoundData> getSound(PluginSound sound) {
        return Optional.ofNullable(soundDataMap.get(sound));
    }

    @SuppressWarnings("DataFlowIssue")
    public void updateSound(PluginSound sound) {
        FileConfiguration file = fetch(FileConfiguration.class, "settings.yml");

        ConfigurationSection section = file.getConfigurationSection(sound.getPath());

        if (section == null) {
            return;
        }

        soundDataMap.put(
            sound,
            new SoundData(
                section.getBoolean("enabled", true),
                Float.parseFloat(section.getString("volume", "1.0F")),
                PluginConsumer.ofUnchecked(
                    () -> Sound.valueOf(section.getString("sound", "NOTE_PLING")),
                    e -> {},
                    this::randomSound
                ),
                Float.parseFloat(section.getString("pitch", "0.5F"))
            )
        );
    }

    @Override
    public void reload() {
        soundDataMap.clear();

        for (PluginSound sound : PluginSound.values()) {
            updateSound(sound);
        }
    }
}
