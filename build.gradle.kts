val exposedVersion = "1.0.0-rc-4"
val kotlinXserializationVersion = "1.10.0"
plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"

    `maven-publish` // Maven yayınlama eklentisini ekleyin
}

// Yayınlama ayarlarını yapılandırın
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["java"]) // Android projesi ise "release", saf Kotlin ise "java"

                groupId = "com.github.erbalkantr"
                artifactId = "kernel"
                version = "2.0.1"
            }
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))
    // Exposed implementation
    implementation("org.jetbrains.exposed:exposed-core:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-dao:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:${exposedVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${kotlinXserializationVersion}")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}