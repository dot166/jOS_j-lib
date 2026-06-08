plugins {
    alias(libs.plugins.maven.publish)
}

group = "io.github.dot166"
version = "136.1.202606072158" // old artifact version

mavenPublishing {
    coordinates(group.toString(), "SettingsLibSpa", version.toString())

    pom {
        withXml {
            asNode().appendNode("distributionManagement").apply {
                appendNode("relocation").apply {
                    appendNode("groupId", "io.github.dot166")
                    appendNode("artifactId", "SpaLib")
                    appendNode("version", "136.1.202606082050")
                }
            }
        }
    }
}
