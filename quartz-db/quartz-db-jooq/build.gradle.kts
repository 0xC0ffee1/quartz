plugins {
    id("java")
    id("java-library")
    id("io.freefair.lombok") version "8.6"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
    `maven-publish`
}


group = "net.c0ffee1"
version = "1.0-SNAPSHOT"

publishing   {
    repositories {
        mavenLocal()
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            groupId = "net.c0ffee1.quartz"
            artifactId = "quartz-db-jooq"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    api(project(":quartz-db:quartz-db-core"))
    api("org.jooq:jooq:3.19.7")
}

application {
    mainClass.set("none")
}


tasks.shadowJar {
    archiveClassifier.set("")
    dependsOn("distTar", "distZip")
}

tasks.test {
    useJUnitPlatform()
}