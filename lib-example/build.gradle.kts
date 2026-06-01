val ver: String = rootProject.extra["libVersion"] as String
val libMinSdk: Int = rootProject.extra["libMinSdk"] as Int
val libCompileSdkMajor: Int = rootProject.extra["libCompileSdkMajor"] as Int
val libCompileSdkMinor: Int = rootProject.extra["libCompileSdkMinor"] as Int
val verCode: Int = (((ver.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
    .toTypedArray()[0].toInt() * 1000) + ver.split("\\.".toRegex())
    .dropLastWhile { it.isEmpty() }
    .toTypedArray()[1].toInt()) * 1000) + ver.split("\\.".toRegex())
    .dropLastWhile { it.isEmpty() }.toTypedArray()[2].toInt()

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
}

android {
    namespace = "io.github.dot166.libexample"
    compileSdk {
        version = release(libCompileSdkMajor) {
            minorApiLevel = libCompileSdkMinor
        }
    }

    defaultConfig {
        applicationId = "io.github.dot166.libexample"
        minSdk = libMinSdk
        targetSdk = libCompileSdkMajor
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
    buildFeatures { compose = true }
}

dependencies {
    implementation(project(":j-LIB-core")) // implementation "io.github.dot166:j-Lib:+"
}
