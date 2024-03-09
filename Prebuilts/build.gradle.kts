group = "j-SDK"
version = "3.0.1"

plugins {
    `kotlin-dsl`
    `maven-publish`
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            artifact("jOS-Core.jar")
            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                val dependencyNode = dependenciesNode.appendNode("dependency")
                dependencyNode.appendNode("groupId", "j-SDK")
                dependencyNode.appendNode("artifactId", "j-SDK-core")
                dependencyNode.appendNode("version", "3.0.1")
            }
            pom {
                name.set(rootProject.name)
                url.set("https://github.com/dot166/jos_j-SDK")

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

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.preference:preference:1.2.1")
    implementation("com.google.android.material:material:1.11.0")
}