plugins {
    id("java")
    kotlin("jvm") version "1.9.22"
    id("io.freefair.lombok") version "8.6"
    `maven-publish`
}

group = "net.c0ffee1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


publishing   {
    repositories {
        mavenLocal()
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            groupId = "net.c0ffee1.quartz"
            artifactId = "quartz-db-core"
        }
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    api("org.slf4j:slf4j-api:2.1.0-alpha1")
    api("com.zaxxer:HikariCP:5.1.0")

    implementation(project(":quartz-core"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}