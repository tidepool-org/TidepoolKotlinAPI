import org.gradle.kotlin.dsl.invoke

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    id("com.android.library")
    alias(libs.plugins.ktorfit)
}


// Room writes each version's schema here, so the next migration can be tested against the real one.
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
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
                implementation(libs.ktorfit.lib)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.kermit)

                // Serialization
                implementation(libs.kotlinx.serialization.json)

                // Coroutines
                implementation(libs.kotlinx.coroutines.core)

                // DateTime
                implementation(libs.kotlinx.datetime)

                // Room KMP dependencies for local storage
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)

                // Koin dependency injection
                implementation(libs.koin.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.koin.test)
                implementation(libs.ktor.client.mock)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.minidns.hla)
                // Declared explicitly (it used to arrive through Ktorfit) so the fake backend can
                // add an interceptor, and so the engine matches the Ktor core version.
                implementation(libs.ktor.client.okhttp)
                implementation(libs.okhttp)
            }
        }
        // Room DAO tests shared by the host JVM run and the on-device run.
        val androidRoomTestDir = "src/androidRoomTest/kotlin"

        val androidUnitTest by getting {
            kotlin.srcDir(androidRoomTestDir)
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                // The Android sqlite-bundled artifact only ships Android binaries. The catalog's
                // sqlite version must stay at the one Room resolves to (2.6.1 for Room 2.8.1): an
                // older driver lacks connection methods Room calls.
                implementation(libs.androidx.sqlite.bundled.jvm)
                implementation(libs.mockk)
            }
        }
        val androidInstrumentedTest by getting {
            kotlin.srcDir(androidRoomTestDir)
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.androidx.test.core)
                implementation(libs.androidx.test.runner)
            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.androidx.room.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspCommonMainMetadata", libs.ktorfit.ksp)
    add("kspAndroid", libs.ktorfit.ksp)
}