package net.c0ffee1.quartz.core.annotations;

import net.c0ffee1.quartz.core.QuartzApplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to fetch an implementation from another module
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface From {
    Class<? extends QuartzApplication> value();
}
