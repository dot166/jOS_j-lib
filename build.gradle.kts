// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.android") version "2.2.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.20" apply false
    id("com.android.library") version "8.13.0" apply false
    id("com.android.application") version "8.13.0" apply false
    id("com.mikepenz.aboutlibraries.plugin") version "13.1.0" apply false
    id("com.mikepenz.aboutlibraries.plugin.android") version "13.1.0" apply false
    id("com.vanniktech.maven.publish") version "0.34.0" apply false
}

buildscript {
    // Define versions in a single place
    extra.apply{
        set("libVersion", Integer.parseInt(providers.exec {
            commandLine("cat", "ver")
        }.standardOutput.asText.get().trim()))
        set("libMinSdk", 26)
        set("libCompileSdk", 36)
    }
}

