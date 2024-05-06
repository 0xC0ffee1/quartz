package net.c0ffee1.quartz.core.config.nodes;

public interface ConfigNode {
    ConfigNode path(String fieldName);
    String asText();
    boolean isMissingNode();
}
