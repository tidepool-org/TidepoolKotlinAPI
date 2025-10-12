plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
    id("com.google.devtools.ksp") version "2.2.20-2.0.3"
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":domain"))
    // Networking
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.1.0")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    
    implementation("io.mcarle:konvert-api:4.3.2")
    ksp("io.mcarle:konvert:4.3.2")

    // Room KMP dependencies for local storage
    implementation("androidx.room:room-runtime:2.8.1")
    implementation("androidx.sqlite:sqlite-bundled:2.5.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")
    add("ksp", "androidx.room:room-compiler:2.8.1")

    // Koin dependency injection
    implementation("io.insert-koin:koin-core:4.1.0")

    // Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.1.0")
    testImplementation("io.insert-koin:koin-test:4.1.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}