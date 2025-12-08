val Ver: String = rootProject.extra["libVersion"] as String
val libMinSdk: Int = rootProject.extra["libMinSdk"] as Int
val libCompileSdk: Int = rootProject.extra["libCompileSdk"] as Int

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
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
}

dependencies {
    api(libs.androidx.appcompat)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.preference)
    api(libs.material)
    api(libs.androidx.core.ktx)
    api(libs.androidx.browser)
    api(libs.androidx.gridlayout)
    // GrapheneOS/AOSP SettingsLib, only the needed preference modules, needed for M3E in settings menu, among other things
    api(project(":settingslib:BarChartPreference"))
    api(project(":settingslib:ButtonPreference"))
    api(project(":settingslib:Category"))
    api(project(":settingslib:CollapsingToolbarBaseActivity"))
    api(project(":settingslib:Preference"))
    api(project(":settingslib:SelectorWithWidgetPreference"))
    api(project(":settingslib:SettingsSpinner"))
    api(project(":settingslib:SliderPreference"))
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
            url = "https://github.com/dot166/jOS_j-lib"
            connection = "scm:git:git://github.com/dot166/jOS_j-lib.git"
            developerConnection = "scm:git:ssh://git@github.com/dot166/jOS_j-lib.git"
        }
    }
}
