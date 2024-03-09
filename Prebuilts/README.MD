# Prebuilt jar files

add the following lines to your settings.gradle
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://raw.githubusercontent.com/jf916/jOS_j-SDK/jOS-15.0/.m2' }
    }
}
```

then add the following lines to your apps build.gradle
```
dependencies {
    implementation "jOS.Core:j-SDK-core:3.0.1"
}
```