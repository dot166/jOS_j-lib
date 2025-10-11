import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import java.util.Properties
import java.io.FileInputStream

val Ver: Int = rootProject.extra["libVersion"] as Int;

plugins {
    id("com.android.application")
    id("com.mikepenz.aboutlibraries.plugin")
    id("com.mikepenz.aboutlibraries.plugin.android")
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
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.dot166.themeengine"
        minSdk = 26
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 26
        versionCode = Integer.MAX_VALUE
        versionName = Integer.MAX_VALUE.toString()
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
    implementation(project(":j-LIB-core")) // implementation("io.github.dot166:j-Lib:+")
}
