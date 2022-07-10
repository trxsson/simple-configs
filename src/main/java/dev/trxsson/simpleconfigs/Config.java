package dev.trxsson.simpleconfigs;

import org.jetbrains.annotations.NotNull;

public abstract class Config {
    protected String config_version = "";

    public String getConfigVersion() {
        return config_version;
    }

    public void setConfigVersion(@NotNull String configVersion) {
        this.config_version = configVersion;
    }
}
