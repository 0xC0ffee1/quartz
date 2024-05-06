package net.c0ffee1.quartz.core.service;

import net.c0ffee1.quartz.core.utils.Result;

public interface ServiceManager {
    void signalShutdown();

    void registerService(Object o);
}
