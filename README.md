# jLib - a common library that (most of) my android apps use

contains a version of the holo theme that is patched for use with appcompat, a custom actionbar based on material3 Toolbar and some other things.

## PrivExt subfolder can only be built with aosp, rest of sdk can be built with gradle

## the j-SDK will work ONLY ON ANDROID LOLLIPOP (5) AND LATER!! because some dependencies require android lollipop or newer

## how to use it?

to use the j-SDK in a gradle project add the following lines to your settings.gradle
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
```

then add the following lines to your project root build.gradle
```
plugins {
    id 'com.android.application' version '8.5.2' apply false
    id 'org.jetbrains.kotlin.android' version '2.0.10' apply false
    id 'org.jetbrains.kotlin.plugin.compose' version '2.0.10' apply false
    id 'com.mikepenz.aboutlibraries.plugin' version "11.2.2" apply false // j-SDK dependency
}
```

then add the following lines to your apps build.gradle
```
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'org.jetbrains.kotlin.plugin.compose'
    id 'com.mikepenz.aboutlibraries.plugin' // j-SDK dependency
}

android {
...
    buildFeatures {
        compose = true
    }
}

aboutLibraries {
    // Required to be set to true
    registerAndroidTasks = true
}

dependencies {
    implementation "io.github.dot166:j-Lib:3.2.6"
}
```


## folder structure

### core/

gradle library (main code)

### PrivExt/

aosp compiled java library (privileged code) included as javalib.jar in core/j-SDK-core/libs

### aosp-libs/

prebuilt aar of core/ and other dependencies (for use in aosp)
