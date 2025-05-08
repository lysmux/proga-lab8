plugins {
    id("java")
    id("io.freefair.lombok") version "8.12.2.1"
}

group = "dev.lysmux.collex"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    implementation("org.apache.commons:commons-lang3:3.17.0")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    implementation("com.google.inject:guice:7.0.0")
    implementation("commons-codec:commons-codec:1.18.0")
    implementation("com.zaxxer:HikariCP:6.3.0")

    implementation("org.postgresql:postgresql:42.7.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}