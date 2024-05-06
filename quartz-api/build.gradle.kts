plugins {
    id("java")
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
            artifactId = "quartz-api"
        }
    }
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("net.kyori:adventure-api:4.16.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}