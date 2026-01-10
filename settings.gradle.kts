pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
            content {
                includeGroup("com.github.PhilJay")
            }
        }
    }
}

rootProject.name = "j-Lib"
include(":j-LIB-core")
include(":lib-example")
include(":settingslib")
project(":settingslib").projectDir = File(rootProject.projectDir, "../platform_frameworks_base/packages/SettingsLib")
include(":settingslib:BannerMessagePreference")
include(":settingslib:BarChartPreference")
include(":settingslib:ButtonPreference")
include(":settingslib:Category")
include(":settingslib:CollapsingToolbarBaseActivity")
include(":settingslib:DataStore")
include(":settingslib:Metadata")
include(":settingslib:Preference")
include(":settingslib:SelectorWithWidgetPreference")
include(":settingslib:SettingsSpinner")
include(":settingslib:SettingsTheme")
include(":settingslib:SettingsTransition")
include(":settingslib:SliderPreference")
