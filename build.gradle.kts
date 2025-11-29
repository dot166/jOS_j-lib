// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.maven.publish) apply false
}

buildscript {
    // Define versions in a single place
    extra.apply{
        set("libVersion", providers.exec {
            commandLine("cat", "ver")
        }.standardOutput.asText.get().trim())
        set("libMinSdk", 29)
        set("libCompileSdk", 36)
    }
}

subprojects {
    tasks.matching { it.name.contains("javadoc", ignoreCase = true) }.configureEach {
        enabled = false
    }
    plugins.withId("com.vanniktech.maven.publish") {
        configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
            configure(com.vanniktech.maven.publish.AndroidSingleVariantLibrary(
                variant = "release",
                sourcesJar = false,
                publishJavadocJar = false,
            ))
        }
    }
}
