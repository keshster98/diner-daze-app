// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Android and Kotlin
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Firebase
    alias(libs.plugins.google.gms.google.services) apply false

    // Code Generation (KSP)
    id("com.google.devtools.ksp") version "2.2.21-2.0.4" apply false

    // Dependency Injection (Hilt)
    id("com.google.dagger.hilt.android") version "2.57.2" apply false

    // Kotlin Serialization
    // Serialization version needs to match Kotlin version in libs.versions.toml
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0" apply false
}