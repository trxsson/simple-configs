package dev.trxsson.simpleconfigs;

public abstract class Config {
    protected String config_version = "";

    public String getConfigVersion() {
        return config_version;
    }

    public void setConfigVersion(String config_version) {
        this.config_version = config_version;
    }
}
