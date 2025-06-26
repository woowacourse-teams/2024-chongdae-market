plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":core:common"))
    implementation("javax.inject:javax.inject:1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation(project(":domain"))
}
