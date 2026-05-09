import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.lineageos.generatebp.GenerateBpPluginExtension
import org.lineageos.generatebp.models.Module

val Ver: String = rootProject.extra["libVersion"] as String
val libMinSdk: Int = rootProject.extra["libMinSdk"] as Int
val libCompileSdkMajor: Int = rootProject.extra["libCompileSdkMajor"] as Int
val libCompileSdkMinor: Int = rootProject.extra["libCompileSdkMinor"] as Int

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.compose)
    alias(libs.plugins.lineageos.generatebp)
}

group = "io.github.dot166"
version = Ver

android {
    namespace = "io.github.dot166.jlib"
    compileSdk {
        version = release(libCompileSdkMajor) {
            minorApiLevel = libCompileSdkMinor
        }
    }

    defaultConfig {
        minSdk = libMinSdk

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures { compose = true }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget("17")
    }
}

dependencies {
    api(libs.androidx.appcompat)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.preference)
    api(libs.material)
    api(libs.androidx.core.ktx)
    api(libs.androidx.browser)
    api(libs.androidx.material3.android)
    api(libs.androidx.ui)
    api(libs.rssparser)
    api(libs.gson)
    api(libs.settingsLib) // GrapheneOS/AOSP SettingsLib
}

mavenPublishing {
    coordinates(group.toString(), rootProject.name, version.toString())

    pom {
        name = "j Common Library"
        description = "jLib - an android code library"
        inceptionYear = "2024"
        url = "https://github.com/dot166/jOS_j-lib"
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT/")
            }
        }
        developers {
            developer {
                id = "dot166"
                name = "._______166"
                url = "https://dot166.github.io"
            }
        }
        scm {
            url = "https://github.com/dot166/jOS_j-lib"
            connection = "scm:git:git://github.com/dot166/jOS_j-lib.git"
            developerConnection = "scm:git:ssh://git@github.com/dot166/jOS_j-lib.git"
        }
    }
}

configure<GenerateBpPluginExtension> {
    targetSdk.set(android.compileSdk!!)
    minSdk.set(android.defaultConfig.minSdk!!)
    versionCode.set(android.compileSdk!!)
    versionName.set(android.compileSdk!!.toString())
    availableInAOSP.set { module: Module ->
        when {
            module.group.startsWith("androidx") -> true
            module.group.startsWith("org.jetbrains") -> true
            module.group.startsWith("io.github.dot166") -> true
            module.group.startsWith("com.google") -> true
            else -> false
        }
    }
}
