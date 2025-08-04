// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0" apply false
    id("com.android.library") version "8.12.0" apply false
    id("com.android.application") version "8.12.0" apply false
    id("com.mikepenz.aboutlibraries.plugin") version "12.2.4" apply false
    id("com.vanniktech.maven.publish") version "0.34.0" apply false
    id("io.github.dot166.aconfig") version "1.0.12" apply false
}

buildscript {
    // Define versions in a single place
    extra.apply{
        set("libVersion", providers.exec {
            commandLine("cat", "ver")
        }.standardOutput.asText.get().trim())
        set("libMinSdk", 26)
        set("libCompileSdk", 36)
    }
}

