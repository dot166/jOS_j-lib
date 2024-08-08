# jOS SDK - a common library that (most of) my android apps use

contains a version of the holo theme that is patched for use with appcompat, a custom actionbar baced on material3 Toolbar and some other things.

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
        maven { url 'https://raw.githubusercontent.com/dot166/jOS_j-SDK/main/.m2' }
    }
}
```

then add the following lines to your project root build.gradle
```
plugins {
    id 'com.android.application' version '8.5.0' apply false
    id 'com.mikepenz.aboutlibraries.plugin' version "11.2.2" apply false // j-SDK dependency
}
```

then add the following lines to your apps build.gradle
```
plugins {
    id 'com.android.application'
    id 'com.mikepenz.aboutlibraries.plugin' // j-SDK dependency
}

android {
...
}

aboutLibraries {
    // Required to be set to true
    registerAndroidTasks = true
}

dependencies {
    implementation "androidx.appcompat:appcompat:1.7.0" // j-SDK dependency
    implementation "androidx.constraintlayout:constraintlayout:2.1.4" // j-SDK dependency
    implementation "androidx.preference:preference:1.2.1" // j-SDK dependency
    implementation "androidx.preference:preference-ktx:1.2.1" // j-SDK dependency
    implementation "com.google.android.material:material:1.12.0" // j-SDK dependency
    implementation "androidx.core:core-ktx:1.13.1" // j-SDK dependency
    implementation "androidx.browser:browser:1.8.0" // j-SDK dependency
    implementation "jOS.Core:j-SDK-core:3.+"
    implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0" // j-SDK dependency
    implementation "com.mikepenz:aboutlibraries-core:11.2.2" // j-SDK dependency
    implementation "com.mikepenz:aboutlibraries-compose-m3:11.2.2" // j-SDK dependency
    implementation "com.kieronquinn.smartspacer:sdk-client:+" // j-SDK dependency
}
```


## folder structure

### core/

gradle library (main code)

### PrivExt/

aosp compiled java library (privileged code) included as javalib.jar in core/j-SDK-core/libs

### aosp-libs/

prebuilt aar of core/ and other dependencies (for use in aosp)

### .m2/

maven repo containing current and older versions (for use in gradle)
