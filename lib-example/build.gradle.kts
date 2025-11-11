val ver: String = rootProject.extra["libVersion"] as String
val libMinSdk: Int = rootProject.extra["libMinSdk"] as Int
val libCompileSdk: Int = rootProject.extra["libCompileSdk"] as Int
val verCode: Int = (((ver.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
    .toTypedArray()[0].toInt() * 1000) + ver.split("\\.".toRegex())
    .dropLastWhile { it.isEmpty() }
    .toTypedArray()[1].toInt()) * 1000) + ver.split("\\.".toRegex())
    .dropLastWhile { it.isEmpty() }.toTypedArray()[2].toInt()

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "io.github.dot166.libexample"
    compileSdk = libCompileSdk

    defaultConfig {
        applicationId = "io.github.dot166.libexample"
        minSdk = libMinSdk
        targetSdk = libCompileSdk
        versionCode = verCode
        versionName = ver
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":j-LIB-core")) // implementation "io.github.dot166:j-Lib:+"
}
