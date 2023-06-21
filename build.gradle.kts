plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20230227")
    implementation("io.microraft:microraft:0.6")
    implementation("org.slf4j:slf4j-log4j12:1.7.29")
    implementation("org.assertj:assertj-core:3.23.1")
}

tasks.test {
    useJUnitPlatform()
}