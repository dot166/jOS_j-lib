# jLib - a common library that (most of) my android apps use

contains a custom actionbar based on material components Toolbar and some other things.

## jLib will work ONLY ON ANDROID OREO (8) AND LATER!! because there is less devices that use Android NOUGAT (7) or older and apps are now dropping support for those versions

## how to use it?

to use jLib in an android app that is built using gradle add the following lines to your project root build.gradle
```
plugins {
    ...
    id 'org.jetbrains.kotlin.android' version '2.1.21' apply false
    id 'org.jetbrains.kotlin.plugin.compose' version '2.1.21' apply false
    id 'com.mikepenz.aboutlibraries.plugin' version "12.2.3" apply false // j-LIB dependency
}
```

then add the following lines to your apps build.gradle
```
plugins {
    ...
    id 'org.jetbrains.kotlin.android'
    id 'org.jetbrains.kotlin.plugin.compose'
    id 'com.mikepenz.aboutlibraries.plugin' // j-LIB dependency
}

android {
    ...
    buildFeatures {
        compose = true
    }
}

aboutLibraries {
    // Required to be set to true
    android.registerAndroidTasks = true
}

dependencies {
    implementation "io.github.dot166:j-Lib:4.2.13"
}
```

if you are including jLib in another android library that is built using gradle add the following lines to your project root build.gradle
```
plugins {
    ...
    id 'org.jetbrains.kotlin.android' version '2.1.21' apply false
    id 'org.jetbrains.kotlin.plugin.compose' version '2.1.21' apply false
}
```

then add the following lines to your libraries build.gradle
```
plugins {
    ...
    id 'org.jetbrains.kotlin.android'
    id 'org.jetbrains.kotlin.plugin.compose'
}

android {
    ...
    buildFeatures {
        compose = true
    }
}

dependencies {
    api "io.github.dot166:j-Lib:4.2.13"
}
```

to use jLib in an AOSP Project add the module ```j.Lib``` to the static_libs section of your Android.bp file

### generateBp

if your app uses the [generateBp Plugin](https://github.com/lineage-next/gradle-generatebp) to automatically add the dependencies for AOSP builds from gradle build files (like jLib uses), you would need to use [my fork of generateBp](https://github.com/dot166/gradle-generatebp) due to jLib using ```j.Lib``` as the AOSP dependency name.

to do that change the maven repo line to be ```dot166/gradle-generatebp``` and change the version number to ```1.25.1``` (or whatever the latest version of my fork is)


## folder structure

### j-LIB-core/

gradle library (main code) with AOSP Build Files

### ThemeEngine/

sources for the ThemeEngine Application with AOSP Build Files

### aosp/

sources for aosp features that normally be in a vendor or 'sdk' folder

### rustLib/

rust common library

## License

see [LICENSE](LICENSE)

### NOTICE: There is code in jLib that are from other projects, Those files are licenced under the licence from their respective projects, the files, projects and licences are listed below.

ContributorRow.kt, modifierIf.kt and most of the about menu code is adapted from [Lawnchair Launcher](https://github.com/LawnchairLauncher/lawnchair) and is licenced under Apache 2.0

all files under com.dede.basic ('src/main/java/com/dede/basic' folder) are adapted from [AndroidEasterEggs](https://github.com/hushenghao/AndroidEasterEggs) and are licenced under Apache 2.0

Some of the preference and theme related files are adapted from [The Android Open Source Project](https://source.android.com/) and are licenced under Apache 2.0

ToolbarUtils.java is adapted from [Material Components](https://github.com/material-components/material-components-android) and is licenced under Apache 2.0

seekbarpreference is adapted from [crDroid Android](https://github.com/crdroidandroid) and is licenced under Apache 2.0

birdgame and other game components are adapted from [The Android Open Source Project](https://source.android.com/) and are licenced under Apache 2.0

sensitive-pn is adapted from [LineageOS](https://github.com/LineageOS), see aosp/sensitive-pn/readme.md for more information

ThemeEngineActivity and all code in the themepickerimpl folder is adapted from [The Android Open Source Project](https://source.android.com/) and are licenced under Apache 2.0
