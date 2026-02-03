import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val exposedVersion = "1.0.0-rc-4"
val kotlinXserializationVersion = "1.10.0"
val ktor_version = "3.4.0"
plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"

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
                version = "4.0.0"
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
    // Ktor Auth JWT - Token doğrulama ve koruma için
    implementation("io.ktor:ktor-server-auth-jwt:${ktor_version}")
// Ktor Content Negotiation & Serialization - JSON işlemleri için (zaten bir kısmını eklemiştin)
    implementation("io.ktor:ktor-server-content-negotiation:${ktor_version}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktor_version}")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21) // Java 21 hedefini burada da belirleyelim
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        // Eğer kotlinx-datetime kullanıyorsan bunu da ekleyebilirsin:
        // freeCompilerArgs.add("-opt-in=kotlinx.datetime.ExperimentalContextApi")
    }
}