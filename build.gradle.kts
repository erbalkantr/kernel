val exposedVersion = "1.0.0-rc-4"

plugins {
    kotlin("jvm") version "2.2.21"
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
                version = "1.0.0"
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
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}