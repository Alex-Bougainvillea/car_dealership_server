plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

application {
    mainClass.set("com.example.car_dealership_server.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.jetbrains.exposed:exposed-core:0.51.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.51.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.51.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.51.1")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.mindrot:jbcrypt:0.4")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("com.h2database:h2:2.2.224")
}
