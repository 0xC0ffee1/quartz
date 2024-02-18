package net.c0ffee1.quartz.core;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public interface QuartzApplication {
    @NotNull Logger getSLF4JLogger();
}
