import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("de.mannodermaus.android-junit5") version "1.10.0.0"
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "2.0.0"
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
        versionCode = 1
        versionName = "1.0"

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
        buildConfigField("String", "NATIVE_APP_KEY", nativeAppKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    val navigationVersion = "2.7.7"
    val fragmentVersion = "1.8.1"
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    androidTestImplementation("org.assertj:assertj-core:3.25.3")
    androidTestImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    androidTestImplementation("de.mannodermaus.junit5:android-test-core:1.3.0")
    androidTestRuntimeOnly("de.mannodermaus.junit5:android-test-runner:1.3.0")
    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$navigationVersion")

    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("com.google.code.gson:gson:2.8.8")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    implementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("androidx.room:room-ktx:2.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    kapt("com.github.bumptech.glide:compiler:4.13.2")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.7.0")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation(libs.androidx.core.ktx)

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    // UI Test - Fragment Scenario
    debugImplementation("androidx.fragment:fragment-testing-manifest:$fragmentVersion")
    androidTestImplementation("androidx.fragment:fragment-testing:$fragmentVersion")

    // Espresso
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.4.0")

    // Espresso RecyclerView Actions
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.3.0")

    // Pagination
    implementation("androidx.paging:paging-runtime-ktx:3.3.0")

    // WebView
    implementation("androidx.webkit:webkit:1.9.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")

    // 카카오 로그인
    implementation("com.kakao.sdk:v2-all:2.20.3") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation("com.kakao.sdk:v2-user:2.20.3") // 카카오 로그인 API 모듈
    implementation("com.kakao.sdk:v2-share:2.20.3") // 카카오톡 공유 API 모듈
    implementation("com.kakao.sdk:v2-talk:2.20.3") // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
    implementation("com.kakao.sdk:v2-friend:2.20.3") // 피커 API 모듈
    implementation("com.kakao.sdk:v2-navi:2.20.3") // 카카오내비 API 모듈
    implementation("com.kakao.sdk:v2-cert:2.20.3") // 카카오톡 인증 서비스 API 모듈

    // data store
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}
