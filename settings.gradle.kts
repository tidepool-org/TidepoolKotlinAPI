pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        kotlin("multiplatform") version "2.1.20"
        kotlin("plugin.serialization") version "2.1.20"
        id("com.google.devtools.ksp") version "2.3.6"
        id("com.android.library") version "8.12.0"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "TidepoolKotlinAPI"

// Mirror the project paths used when these modules are nested in a parent build.
include(":TidepoolKotlinAPI:lib")
include(":TidepoolKotlinAPI:domain")
include(":TidepoolKotlinAPI:data")
project(":TidepoolKotlinAPI").projectDir = file("gradle") // inert anchor; dir must exist
project(":TidepoolKotlinAPI:lib").projectDir = file("lib")
project(":TidepoolKotlinAPI:domain").projectDir = file("domain")
project(":TidepoolKotlinAPI:data").projectDir = file("data")
