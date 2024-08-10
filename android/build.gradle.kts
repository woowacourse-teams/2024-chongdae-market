// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
    }
    dependencies {
        val navigationVersion = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion")
//        val secretsGradlePlugin = "2.0.0"
//        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:$secretsGradlePlugin")
    }
}

plugins {
    id("com.android.application") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
