import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "dev.nyon"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.kord:kord-core:0.8.0-M15")
}

application {
    mainClass.set("dev.nyon.roler.RolerKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}