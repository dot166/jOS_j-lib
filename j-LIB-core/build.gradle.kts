import org.lineageos.generatebp.GenerateBpPlugin
import org.lineageos.generatebp.GenerateBpPluginExtension
import org.lineageos.generatebp.models.Module

val Ver: String = rootProject.extra["libVersion"] as String;
val libMinSdk: Int = rootProject.extra["libMinSdk"] as Int;
val libCompileSdk: Int = rootProject.extra["libCompileSdk"] as Int;

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    `maven-publish`
    id("com.vanniktech.maven.publish")
}

apply {
    plugin<GenerateBpPlugin>()
}

buildscript {
    repositories {
        maven("https://raw.githubusercontent.com/dot166/gradle-generatebp/v1.25.1/.m2")
    }

    dependencies {
        //noinspection GradleDynamicVersion
        classpath("org.lineageos:gradle-generatebp:+")
    }
}

group = "io.github.dot166"
version = Ver

android {
    namespace = "io.github.dot166.jlib"
    compileSdk = libCompileSdk

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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures { compose = true }
}

dependencies {
    api("androidx.appcompat:appcompat:1.7.1")
    api("androidx.constraintlayout:constraintlayout:2.2.1")
    api("androidx.recyclerview:recyclerview:1.4.0")
    //noinspection KtxExtensionAvailable
    api("androidx.preference:preference:1.2.1")
    api("com.google.android.material:material:1.14.0-alpha05")
    api("androidx.core:core-ktx:1.17.0")
    api("androidx.browser:browser:1.9.0")
    api("androidx.activity:activity-compose:1.11.0")
    api("androidx.compose.ui:ui-android:1.9.3")
    api("androidx.compose.material3:material3-android:1.4.0")
    api("androidx.compose.material:material-android:1.9.3")
    api("com.mikepenz:aboutlibraries-core:13.1.0")
    api("com.mikepenz:aboutlibraries-compose-m3:13.1.0")
    api("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    api("com.prof18.rssparser:rssparser:6.1.0")
    api("com.github.bumptech.glide:glide:5.0.5")
    api("com.caverock:androidsvg:1.4")
    annotationProcessor("com.github.bumptech.glide:compiler:5.0.5")
    api("androidx.media3:media3-exoplayer:1.8.0")
    api("androidx.media3:media3-common:1.8.0")
    api("androidx.media3:media3-session:1.8.0")
    api("androidx.media3:media3-exoplayer-dash:1.8.0")
    api("androidx.compose.material:material-icons-extended-android:1.7.8")
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
                url.set("https://choosealicense.com/licenses/mit/")
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
            url = "https://github.com/github.com/dot166/jOS_j-lib"
            connection = "scm:git:git://github.com/github.com/dot166/jOS_j-lib.git"
            developerConnection = "scm:git:ssh://git@github.com/github.com/dot166/jOS_j-lib.git"
        }
    }
}

configure<GenerateBpPluginExtension> {
    minSdk.set(libMinSdk)
    targetSdk.set(libCompileSdk)
    availableInAOSP.set { _: Module -> false }
}
