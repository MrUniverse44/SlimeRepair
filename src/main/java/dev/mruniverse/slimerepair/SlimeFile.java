package dev.mruniverse.slimerepair;

import dev.mruniverse.slimelib.SlimeFiles;
import dev.mruniverse.slimelib.SlimePlatform;

public enum SlimeFile implements SlimeFiles {
    SETTINGS("settings.yml"),
    MESSAGES("messages.yml");

    private final String file;

    SlimeFile(String file) {
        this.file = file;
    }

    @Override
    public String getFileName() {
        return file;
    }

    @Override
    public String getFolderName() {
        return "";
    }

    @Override
    public String getResourceFileName(SlimePlatform platform) {
        return file;
    }

    @Override
    public boolean isInDifferentFolder() {
        return false;
    }
}
