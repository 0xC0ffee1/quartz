package net.c0ffee1.quartz.core.service;

import net.c0ffee1.quartz.core.utils.Result;

public interface ServiceManager {

    public Result<?, ?> reloadService(Object serviceClass);
}
