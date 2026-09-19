plugins {
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.serialization") version "2.4.0"
    application
}


group = "com.company"
version = "1.0"


repositories {
    mavenCentral()
}


dependencies {

    implementation(
        "org.jetbrains.exposed:exposed-java-time:0.58.0"
    )
    implementation("ch.qos.logback:logback-classic:1.5.16")

    implementation("io.ktor:ktor-server-core:3.0.0")

    implementation("io.ktor:ktor-server-netty:3.0.0")

    implementation("io.ktor:ktor-server-content-negotiation:3.0.0")

    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")

    implementation("io.ktor:ktor-server-status-pages:3.0.0")
    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.56.0")

    implementation("org.jetbrains.exposed:exposed-jdbc:0.56.0")

    implementation("org.postgresql:postgresql:42.7.4")

}


kotlin {

    jvmToolchain(22)

}

application {
    mainClass.set("com.company.licenseserver.ApplicationKt")

    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=ALL-UNNAMED"
    )
}