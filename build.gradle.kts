// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.compose) apply false
}

buildscript {
    // Define versions in a single place
    extra.apply{
        set("libVersion", providers.exec {
            commandLine("cat", "ver")
        }.standardOutput.asText.get().trim())
        set("libMinSdk", 31)
        set("libCompileSdk", 36)
    }
}
