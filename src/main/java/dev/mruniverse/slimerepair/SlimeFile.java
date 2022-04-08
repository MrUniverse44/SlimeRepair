package dev.mruniverse.slimerepair;

import dev.mruniverse.slimelib.SlimeFiles;

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
    public String getResourceFileName() {
        return file;
    }

    @Override
    public String getFolderName() {
        return "";
    }

    @Override
    public boolean isInDifferentFolder() {
        return false;
    }
}
