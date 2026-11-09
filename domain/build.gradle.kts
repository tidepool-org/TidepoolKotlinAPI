import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    // AGP 9 deprecates androidLibrary in favour of android, but AGP 8 (this repo's own build) only has androidLibrary.
    androidLibrary {
        namespace = "org.tidepool.domain"
        // Configured here rather than by android-loop's convention plugins, because this repo also builds on its own.
        // -Xannotation-default-target is left out: with the Kotlin 2.1.20 in settings.gradle.kts it marks the output as pre-release.
        compileSdk = 37
        minSdk = 26

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        withHostTest {}
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kermit)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val androidMain by getting
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
