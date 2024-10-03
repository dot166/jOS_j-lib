import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.provider.Provider

val Ver: String = providers.exec {
    commandLine("cat", "ver")
}.standardOutput.asText.get().trim()



plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    `maven-publish`
    id("com.vanniktech.maven.publish")
}

android.buildFeatures.buildConfig=true

group = "io.github.dot166"
version = Ver

android {
    namespace = "jOS.Core"
    compileSdk = 35

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "LIBVersion", "\"$version\"")
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
    api("androidx.appcompat:appcompat:1.7.0")
    api("androidx.preference:preference-ktx:1.2.1")
    api("com.google.android.material:material:1.12.0")
    api("androidx.core:core-ktx:1.13.1")
    api("androidx.browser:browser:1.8.0")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api("androidx.compose.ui:ui-android:1.7.2")
    api("androidx.compose.material3:material3-android:1.3.0")
    api("androidx.compose.material:material-android:1.7.2")
    api("io.coil-kt:coil-compose:2.7.0")
    api("com.google.accompanist:accompanist-drawablepainter:0.36.0")
    api("com.google.accompanist:accompanist-placeholder-material:0.36.0")
    api("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    api("com.mikepenz:aboutlibraries-core:11.2.3")
    api("com.mikepenz:aboutlibraries-compose-m3:11.2.3")
    api("androidx.navigation:navigation-fragment-ktx:2.8.2")
    api("androidx.navigation:navigation-ui-ktx:2.8.1")
}

mavenPublishing {
    coordinates(group.toString(), rootProject.name, version.toString())

    pom {
        name = "j Common Library"
        description = "jLib - a common library that contains a version of the holo theme that is patched for use with material components and appcompat, a custom actionbar based on material components Toolbar and some other things."
        inceptionYear = "2024"
        url = "https://github.com/dot166/jOS_j-lib"
        licenses {
            license {
                name = "The GNU General Public License v3.0"
                url = "https://www.gnu.org/licenses/gpl-3.0.txt"
                distribution = "https://www.gnu.org/licenses/gpl-3.0.txt"
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
