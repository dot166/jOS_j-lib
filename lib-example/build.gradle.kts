val Ver: String = providers.exec {
    commandLine("cat", "ver")
}.standardOutput.asText.get().trim()

plugins {
    id("com.android.application")
    id("com.mikepenz.aboutlibraries.plugin")
}

android {
    namespace = "io.github.dot166.jLib_example"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.github.dot166.jLib_example"
        minSdk = 26
        targetSdk = 35
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
