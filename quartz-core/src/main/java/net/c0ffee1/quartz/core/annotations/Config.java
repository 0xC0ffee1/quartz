package net.c0ffee1.quartz.core.annotations;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    String node();
    String file();
    String type() default "yaml";

    boolean persistent() default true;
}
