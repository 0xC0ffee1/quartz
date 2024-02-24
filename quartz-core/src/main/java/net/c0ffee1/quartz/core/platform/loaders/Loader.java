package net.c0ffee1.quartz.core.platform.loaders;

import java.util.Set;
import java.util.function.Consumer;

public interface Loader<T> {
    void load(Consumer<T> consumer);
}
