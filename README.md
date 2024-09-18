# Quartz
Quartz is still a work in progress

> Quartz is a dependency-injection framework made mainly for Minecraft Plugins inspired by Spring Boot, but also supports generic JVM-applications

## Features
- Easy configuration file (TOML, YAML) management with @Config
- Automatic service registration with @Service or @Component
- Async scheduler utilizing Kotlin channels for database operations with `FixedScheduler`

## Get started
### Clone and build the project

1. git clone https://github.com/0xc0ffee1/quartz/
2. cd quartz && ./gradlew publish

### Add quartz as a dependency
- Add the wanted modules as your dependencies
#### Gradle (with core, bukkit, db-core as example modules)
```kotlin
dependencies {
    ...
    implementation "net.c0ffee1.quartz:quartz-core:1.0-SNAPSHOT"
    implementation "net.c0ffee1.quartz:quartz-bukkit:1.0-SNAPSHOT"
    implementation "net.c0ffee1.quartz:quartz-db-core:1.0-SNAPSHOT"
    ...
}
```

### Usage (In Bukkit Plugins)
- Implement `QuartzBukkitPlugin` in your `JavaPlugin`
- in `onEnable` call `Quartz.init(new BukkitPlatform(YourPlugin.class, this));`
- You're now ready to use quartz

### Examples
#### Service Registration

Simple Bukkit event listener:
```java
@Service
@RegisterEvents
public class ExampleEventListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().sendMessage("Hello world!");
    }
}
```
#### Config Registration
```java
@Config(file = "config", node = "example", type = "toml")
@Getter
public class ExampleConfig {
    @JsonProperty("example_map")
    private HashMap<String, Integer> exampleMap;
}
```
That will load the map from a toml config defined like this:
```toml
[example.example_map]
some_key="hello"
other_key="world"
```
#### Generic Dependency Injection
Quartz uses Guice under the hood.
Refer to its documentation here: https://github.com/google/guice/wiki/GettingStarted