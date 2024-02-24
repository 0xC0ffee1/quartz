package net.c0ffee1.mock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.inject.Singleton;
import lombok.Data;
import lombok.Getter;
import net.c0ffee1.quartz.core.annotations.Config;

import java.util.Map;
import java.util.UUID;

@Data
@Config(name = "test", type = "toml", persistent = false)
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


