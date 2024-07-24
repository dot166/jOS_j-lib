import org.lineageos.generatebp.GenerateBpPlugin
import org.lineageos.generatebp.GenerateBpPluginExtension
import org.lineageos.generatebp.models.Module

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

apply {
    plugin<GenerateBpPlugin>()
}

buildscript {
    repositories {
        maven("https://raw.githubusercontent.com/dot166/gradle-generatebp/v1.9/.m2")
    }

    dependencies {
        classpath("org.lineageos:gradle-generatebp:+")
    }
}

android {
    namespace = "com.j.aosp_libs"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.j.aosp_libs"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    //noinspection GradleDynamicVersion
    implementation("jOS.Core:j-SDK-core:3.+")
}


configure<GenerateBpPluginExtension> {
    targetSdk.set(android.defaultConfig.targetSdk!!)
    availableInAOSP.set { module: Module ->
        when {
            module.group.startsWith("androidx") -> true
            module.group.startsWith("com.google") -> true
            module.group.startsWith("org.jetbrains") -> true
            else -> false
        }
    }
}