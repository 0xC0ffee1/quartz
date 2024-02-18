package net.c0ffee1.quartz.core.service;

public interface TrinityService {
    void onRegister() throws Exception;
    void onUnregister();

    //VoxelConfig getDefaultConfig();

    void reloadConfig();

    void setDebugMode(boolean debugMode);
    boolean isDebugMode();

    boolean reloadService();
}
