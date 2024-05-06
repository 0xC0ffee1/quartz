plugins {
    kotlin("jvm") version "1.9.22"
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
            artifactId = "quartz-bukkit"
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
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")


    compileOnly(libs.paper)
    implementation(project(":quartz-core"))


}
tasks.test {
    useJUnitPlatform()
}
