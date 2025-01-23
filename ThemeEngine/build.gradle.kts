import com.android.build.gradle.internal.api.ApkVariantOutputImpl

val Ver: String = rootProject.extra["libVersion"] as String;
val libMinSdk: Int = rootProject.extra["libMinSdk"] as Int;
val libCompileSdk: Int = rootProject.extra["libCompileSdk"] as Int;

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.mikepenz.aboutlibraries.plugin")
}

android {
    namespace = "io.github.dot166.ThemeEngine"
    compileSdk = libCompileSdk

    defaultConfig {
        applicationId = "io.github.dot166.ThemeEngine"
        minSdk = libMinSdk
        targetSdk = libCompileSdk
    }

    applicationVariants.configureEach {
        outputs.configureEach {
            (this as? ApkVariantOutputImpl)?.outputFileName =
                "ThemeEngine-v$Ver-${buildType.name}.apk"
        }
    }

    buildTypes {
        all {
            resValue("string", "ver_name", Ver)
        }
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }

    aaptOptions.additionalParameters.add("--auto-add-overlay")
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures.resValues = true
}

aboutLibraries {
    // Required to be set to true
    registerAndroidTasks = true
    configPath = "config"
    prettyPrint = true
}

dependencies {
    implementation(project(":j-LIB-core")) // implementation("io.github.dot166:j-Lib:4.0.1")
}
