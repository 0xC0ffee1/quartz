

plugins {
    kotlin("plugin.serialization") version "1.9.22"
    kotlin("jvm") version "1.9.22"
    id("io.freefair.lombok") version "8.6"
    `maven-publish`
     application
}

publishing   {
    repositories {
        mavenLocal()
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            groupId = "net.c0ffee1.quartz"
            artifactId = "quartz-core"
        }
    }
}

group = "net.c0ffee1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven {
        url = uri("https://jitpack.io")
    }

}

dependencies {
    testImplementation(kotlin("test")) // The Kotlin test library
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    api("org.jetbrains:annotations:24.0.0")
    api(libs.guice){
        exclude(group = "com.google.guava")
    }
    api(libs.reflections)
    api("org.slf4j:slf4j-api:2.0.12")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-toml:2.16.1")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.16.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}



kotlin {
    jvmToolchain(17)
}
