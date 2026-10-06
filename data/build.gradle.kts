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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
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
                implementation("co.touchlab:kermit:2.0.4")

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
                implementation("io.ktor:ktor-client-mock:3.3.1")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-logging:3.3.1")
                implementation("io.ktor:ktor-client-content-negotiation:3.3.1")
                implementation("org.minidns:minidns-hla:1.1.1")
                // Declared explicitly (it used to arrive through Ktorfit) so the fake backend can
                // add an interceptor, and so the engine matches the Ktor core version.
                implementation("io.ktor:ktor-client-okhttp:3.3.1")
                implementation("com.squareup.okhttp3:okhttp:5.1.0")
            }
        }
        // Room DAO tests shared by the host JVM run and the on-device run.
        val androidRoomTestDir = "src/androidRoomTest/kotlin"

        val androidUnitTest by getting {
            kotlin.srcDir(androidRoomTestDir)
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                // The Android sqlite-bundled artifact only ships Android binaries. Keep this at the
                // sqlite version Room resolves to (2.6.1 for Room 2.8.1), not the 2.5.0 declared
                // above: an older driver lacks connection methods Room calls.
                implementation("androidx.sqlite:sqlite-bundled-jvm:2.6.1")
                implementation("io.mockk:mockk:1.14.9")
            }
        }
        val androidInstrumentedTest by getting {
            kotlin.srcDir(androidRoomTestDir)
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
                implementation("androidx.test:core:1.6.1")
                implementation("androidx.test:runner:1.6.2")
            }
        }
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