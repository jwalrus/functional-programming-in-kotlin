plugins {
    kotlin("jvm") version "1.3.70"
}

group = "jwalrus.fp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.allWarningsAsErrors = true
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}