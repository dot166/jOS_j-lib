plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}

android.buildFeatures.buildConfig=true

group = "jOS.Core"
version = "3.2.2"

android {
    namespace = "jOS.Core"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "SDKVersion", "\"$version\"")
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
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.browser:browser:1.8.0")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("javalib.jar"))))
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set(rootProject.name)
                url.set("https://github.com/dot166/jOS_j-SDK")

                licenses {
                    license {
                        name.set("The GNU General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                    }
                }

                developers {
                    developer {
                        name.set("._______166")
                        url.set("https://dot166.github.io")
                    }
                }
            }
        }
    }
}
