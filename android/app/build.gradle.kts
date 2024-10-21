import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("de.mannodermaus.android-junit5") version "1.10.0.0"
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "2.0.0"
    id("com.google.firebase.crashlytics")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.zzang.chongdae"
    compileSdk = 34

    val properties =
        Properties().apply {
            try {
                load(FileInputStream(rootProject.file("local.properties")))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    defaultConfig {
        applicationId = "com.zzang.chongdae"
        minSdk = 26
        targetSdk = 34
        versionCode = 6
        versionName = "1.1.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"
        vectorDrawables {
            useSupportLibrary = true
        }

        val baseUrl = properties.getProperty("base_url")
        val token = properties.getProperty("token")
        val nativeAppKey = properties.getProperty("native_app_key")

        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        buildConfigField("String", "TOKEN", "\"$token\"")
        buildConfigField("String", "NATIVE_APP_KEY", "\"$nativeAppKey\"")
        manifestPlaceholders["native_app_key"] = nativeAppKey
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    packaging {
        resources {
            excludes += "META-INF/**"
            excludes += "win32-x86*/**"
        }
    }
    buildFeatures {
        buildConfig = true
    }

    dataBinding {
        enable = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.constraintlayout)

    // Test
    implementation(libs.androidx.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.core.testing)

    // Android Test
    androidTestImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.assertj.core)
    androidTestImplementation(libs.kotest.runner.junit5)
    androidTestImplementation(libs.mannodermaus.test.core)
    androidTestImplementation(libs.mannodermaus.test.runner)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)

    // Espresso 및 관련
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)

    // UI Test: Fragment Scenario
    debugImplementation(libs.androidx.fragment.testing)
    androidTestImplementation(libs.androidx.fragment.testing)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // json
    implementation(libs.kotlinx.serialization.json)

    // Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.kotlinx.serialization)

    // Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    androidTestImplementation(libs.androidx.navigation.testing)

    // Pagination
    implementation(libs.androidx.paging.runtime)

    // WebView
    implementation(libs.androidx.webkit)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation("com.google.firebase:firebase-messaging-ktx")

    // 카카오 로그인
    implementation(libs.kakao.sdk)

    // Mockk
    implementation(libs.mockwebserver)
    testImplementation(libs.mockk)

    // Swipe Refresh Layout
    implementation(libs.androidx.swiperefreshlayout)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}
