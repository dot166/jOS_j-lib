# jOS SDK - a common library that (most of) my android apps use

contains a version of the holo theme that is based on appcompat and some other things.

## Build subfolder can only be built with aosp, rest of sdk can be built with gradle

add the following lines to your settings.gradle
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://raw.githubusercontent.com/dot166/jOS_j-SDK/jOS-15.0/.m2' }
    }
}
```

then add the following lines to your apps build.gradle
```
dependencies {
    implementation "jOS.Core:j-SDK-core:3.+"
}
```
