val Ver: String = rootProject.extra["libVersion"] as String;
val libMinSdk: Int = rootProject.extra["libMinSdk"] as Int;
val libCompileSdk: Int = rootProject.extra["libCompileSdk"] as Int;

plugins {
    id("com.android.application")
    id("com.mikepenz.aboutlibraries.plugin")
}

android {
    namespace = "io.github.dot166.jLib_example"
    compileSdk = libCompileSdk

    defaultConfig {
        applicationId = "io.github.dot166.jLib_example"
        minSdk = libMinSdk
        targetSdk = libCompileSdk
        versionCode = 40 // 4.0.x
        versionName = Ver
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
}

aboutLibraries {
    registerAndroidTasks = true
}

dependencies {
    implementation(project(":j-LIB-core")) // implementation "io.github.dot166:j-Lib:4.0.0"
}
