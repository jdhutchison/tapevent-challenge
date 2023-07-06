plugins {
    id("java")
}

group = "sh.hutch"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest.attributes["Implementation-Title"] = "Tap Event Processor"
    manifest.attributes["Main-Class"] = "sh.hutch.taponoff.Main"
    archiveFileName.set("tap-event-processor.jar")
}
