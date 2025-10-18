import java.util.Properties
import java.io.FileInputStream

val ver: Int = rootProject.extra["libVersion"] as Int;
val libMinSdk: Int = rootProject.extra["libMinSdk"] as Int;
val libCompileSdk: Int = rootProject.extra["libCompileSdk"] as Int;

plugins {
    id("com.android.application")
    id("com.mikepenz.aboutlibraries.plugin")
    id("com.mikepenz.aboutlibraries.plugin.android")
}

android {
    namespace = "io.github.dot166.libexample"
    compileSdk = libCompileSdk

    defaultConfig {
        applicationId = "io.github.dot166.libexample"
        minSdk = libMinSdk
        targetSdk = libCompileSdk
        versionCode = ver
        versionName = ver.toString()
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

aboutLibraries {
    collect.configPath.set(File(rootProject.projectDir, "config"))
    export.prettyPrint = true
}

dependencies {
    implementation(project(":j-LIB-core")) // implementation "io.github.dot166:j-Lib:+"
}
