plugins {
    id("java")
    id("java-library")
    id("io.freefair.lombok") version "8.6"
    `maven-publish`
}

group = "net.c0ffee1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri( "https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

publishing  {
    repositories {
        mavenLocal()
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            groupId = "net.c0ffee1.quartz"
            artifactId = "quartz-bukkit-holograms"
        }
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly(libs.paper)
    api(project(":quartz-api"))
    compileOnly(project(":quartz-core"))
    api(project(":quartz-bukkit:quartz-bukkit-core"))
    api(project(":quartz-bukkit:quartz-bukkit-protocol"))
    compileOnly("me.clip:placeholderapi:2.11.5")

}

tasks.test {
    useJUnitPlatform()
}