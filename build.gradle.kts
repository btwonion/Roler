import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    application

    id("com.github.breadmoirai.github-release") version "2.4.1"
}

group = "dev.nyon"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.kord:kord-core:0.8.0-M16")
    implementation("org.litote.kmongo:kmongo-coroutine-serialization:4.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation("org.slf4j:slf4j-simple:2.0.3")
}

application {
    mainClass.set("dev.nyon.roler.RolerKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

githubRelease {
    token(findProperty("github.token")?.toString())

    val split = "btwonion/Roler".split("/")
    owner(split[0])
    repo(split[1])
    tagName("v${project.version}")
    body("Add mongo integration")
}