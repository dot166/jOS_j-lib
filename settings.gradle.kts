pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://raw.githubusercontent.com/dot166/gradle-generatebp/v1.34/.m2")
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
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        mavenLocal()
    }
}

rootProject.name = "j-Lib"
include(":j-LIB-core")
include(":lib-example")
