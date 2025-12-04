import org.gradle.kotlin.dsl.invoke

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    id("com.android.library")
    id("de.jensklingenberg.ktorfit") version "2.6.4"
}


android {
    namespace = "org.tidepool.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":TidepoolKotlinAPI:domain"))

                // Ktorfit for networking
                implementation("de.jensklingenberg.ktorfit:ktorfit-lib:2.6.4")
                implementation("io.ktor:ktor-client-content-negotiation:3.3.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.1")
                implementation("io.ktor:ktor-client-logging:3.3.1")

                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

                // DateTime
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

                // Room KMP dependencies for local storage
                implementation("androidx.room:room-runtime:2.8.1")
                implementation("androidx.sqlite:sqlite-bundled:2.5.0")

                // Koin dependency injection
                implementation("io.insert-koin:koin-core:4.1.0")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
                implementation("io.insert-koin:koin-test:4.1.0")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-logging:3.3.1")
                implementation("io.ktor:ktor-client-content-negotiation:3.3.1")
                implementation("org.minidns:minidns-hla:1.1.1")
            }
        }
        val androidUnitTest by getting
    }
}


repositories {
    mavenCentral()
    google()
}

dependencies {
    add("kspCommonMainMetadata", "androidx.room:room-compiler:2.8.1")
    add("kspAndroid", "androidx.room:room-compiler:2.8.1")
    add("kspCommonMainMetadata", "de.jensklingenberg.ktorfit:ktorfit-ksp:2.6.4")
    add("kspAndroid", "de.jensklingenberg.ktorfit:ktorfit-ksp:2.6.4")
}