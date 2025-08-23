import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import java.util.Properties
import java.io.FileInputStream

val Ver: Int = rootProject.extra["libVersion"] as Int;
val libMinSdk: Int = rootProject.extra["libMinSdk"] as Int;
val libCompileSdk: Int = rootProject.extra["libCompileSdk"] as Int;

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.mikepenz.aboutlibraries.plugin")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")

// Initialize a new Properties() object called keystoreProperties.
val keystoreProperties = Properties()

// Load your keystore.properties file into the keystoreProperties object.
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            enableV4Signing = true // fix out of band signing (i think)
        }
    }
    namespace = "io.github.dot166.themeengine"
    compileSdk = libCompileSdk

    defaultConfig {
        applicationId = "io.github.dot166.themeengine"
        minSdk = libMinSdk
        targetSdk = libCompileSdk
        versionCode = Ver
        versionName = Ver.toString()
    }

    applicationVariants.configureEach {
        outputs.configureEach {
            (this as? ApkVariantOutputImpl)?.outputFileName =
                "ThemeEngine-$Ver-${buildType.name}.apk"
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }

    sourceSets {
        named("main") {
            java {
                srcDir(rootProject.projectDir.absolutePath + "/themepickerimpl/java")
            }
            res {
                srcDir(rootProject.projectDir.absolutePath + "/themepickerimpl/res")
            }
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
    android.registerAndroidTasks = true
    collect.configPath.set(File(rootProject.projectDir, "config"))
    export.prettyPrint = true
}

dependencies {
    implementation(project(":j-LIB-core")) // implementation("io.github.dot166:j-Lib:+")
}
