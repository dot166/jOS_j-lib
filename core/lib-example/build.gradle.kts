val Ver: String = providers.exec {
    commandLine("cat", "ver")
}.standardOutput.asText.get().trim()

plugins {
    id("com.android.application")
    id("com.mikepenz.aboutlibraries.plugin")
}

android {
    namespace = "com.j.lib_example"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.j.lib_example"
        minSdk = 24
        targetSdk = 35
        versionCode = 32 // 3.2.x
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
    implementation(project(":j-LIB-core")) // implementation "io.github.dot166:j-Lib:3.2.17"
}
