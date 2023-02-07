plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    api("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    api("org.jetbrains:annotations:24.0.0")
}

group = "dev.trxsson"
version = "1.0.1-SNAPSHOT"
description = "Simple-Configs"
java.sourceCompatibility = JavaVersion.VERSION_19

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
