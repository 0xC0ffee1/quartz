plugins {
    kotlin("jvm") version "1.9.22"
    id("io.freefair.lombok") version "8.6"
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
            artifactId = "quartz-bukkit-protocol"
        }
    }
}

group = "net.c0ffee1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven { url = uri("https://repo.codemc.org/repository/maven-public/") }
    repositories {
        maven { url = uri( "https://repo.dmulloy2.net/repository/public/") }
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly(libs.paper)
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
    implementation(project(":quartz-core"))
    api(project(":quartz-bukkit:quartz-bukkit-core"))
}

tasks.test {
    useJUnitPlatform()
}
