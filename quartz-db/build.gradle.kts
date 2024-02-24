import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
    `maven-publish`
}

publishing   {
    repositories {
        mavenLocal()
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            groupId = "net.c0ffee1.quartz"
            artifactId = "quartz-db"
        }
    }
}

group = "net.c0ffee1"
version = "1.0-SNAPSHOT"

val exposedVersion: String = "0.47.0"

repositories {
    mavenCentral()
    mavenLocal()
}

application {
    mainClass.set("none")
}


dependencies {
    api("org.jetbrains.exposed:exposed-core:$exposedVersion")
    api("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    api("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    testImplementation(kotlin("test")) // The Kotlin test library
}


tasks.shadowJar {
    archiveClassifier.set("")
    dependsOn("distTar", "distZip")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}