// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10" apply false
    id("com.android.library") version "8.8.0" apply false
    id("com.android.application") version "8.8.0" apply false
    id("com.mikepenz.aboutlibraries.plugin") version "11.4.0" apply false
    id("com.vanniktech.maven.publish") version "0.30.0"
}

buildscript {
    // Define versions in a single place
    extra.apply{
        set("libVersion", providers.exec {
            commandLine("cat", "ver")
        }.standardOutput.asText.get().trim())
        set("libMinSdk", 26)
        set("libCompileSdk", 35)
    }
}

