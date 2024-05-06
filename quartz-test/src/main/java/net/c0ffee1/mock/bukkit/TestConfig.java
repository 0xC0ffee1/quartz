package net.c0ffee1.mock.bukkit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import net.c0ffee1.quartz.core.annotations.Config;

import java.util.Map;
import java.util.UUID;

@Data
@Config(node = "test", file = "test", type = "toml", persistent = false)
public class TestConfig {
    private boolean testEnabled;
    private String testString;

    private Map<String, Integer> someData;

    private Test test;

    @JsonIgnore
    public UUID id = UUID.randomUUID();

    @Data
    private static class Test{
        public String someKey;
    }
}


