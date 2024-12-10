# jLib - a common library that (most of) my android apps use

contains a custom actionbar based on material components Toolbar and some other things.

## A Note On ThemeEngine

ThemeEngine is an important component of jLib but the code for the database app is at https://github.com/dot166/jOS_ThemeEngine

## PrivExt subfolder can only be built with AOSP, rest of lib can be built with gradle or AOSP

## jLib will work ONLY ON ANDROID NOUGAT (7) AND LATER!! because 97.2% of Android devices use Android nougat or newer and it would be a nightmare to keep supporting Android Marshmallow (6) and older

## how to use it?

to use jLib in an android app that is built using gradle add the following lines to your settings.gradle
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
    id 'com.android.application' version '8.7.1' apply false
    id 'org.jetbrains.kotlin.android' version '2.0.21' apply false
    id 'org.jetbrains.kotlin.plugin.compose' version '2.0.21' apply false
    id 'com.mikepenz.aboutlibraries.plugin' version "11.2.3" apply false // j-SDK dependency
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
    implementation "io.github.dot166:j-Lib:3.3.1"
}
```

if you are including jLib in another android library that is built using gradle add the following lines to your settings.gradle
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
    id 'com.android.library' version '8.7.1' apply false
    id 'org.jetbrains.kotlin.android' version '2.0.21' apply false
    id 'org.jetbrains.kotlin.plugin.compose' version '2.0.21' apply false
}
```

then add the following lines to your libraries build.gradle
```
plugins {
    id 'com.android.library'
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
    api "io.github.dot166:j-Lib:3.3.1"
}
```

to use jLib in an AOSP Project add the module ```j.Lib``` to the static_libs section of your Android.bp file


## folder structure

### j-LIB-core/

gradle library (main code) with AOSP Build Files

### PrivExt/

AOSP compiled java library (privileged code) included as javalib.jar in core/j-SDK-core/libs and included in AOSP Build

### lib-example/

example gradle application

## License

[![GNU GPLv3 Image](https://www.gnu.org/graphics/gplv3-127x51.png)](http://www.gnu.org/licenses/gpl-3.0.en.html)

jLib is Free Software: You can use, study share and improve it at your
will. Specifically you can redistribute and/or modify it under the terms of the
[GNU General Public License](https://www.gnu.org/licenses/gpl.html) as
published by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

### NOTICE: There is code in jLib that are from other projects, Those files are licenced under the licence from their respective projects, the files, projects and licences are listed below.

ContributorRow.kt, modifierIf.kt and most of the about menu code is adapted from [Lawnchair Launcher](https://github.com/LawnchairLauncher/lawnchair) and is licenced under Apache 2.0

all files under com.dede.basic ('src/com/dede/basic' folder) are adapted from [AndroidEasterEggs](https://github.com/hushenghao/AndroidEasterEggs) and are licenced under Apache 2.0

Some of the preference and holo theme related files are adapted from [The Android Open Source Project](https://source.android.com/) and are licenced under Apache 2.0

ToolbarUtils.java is adapted from [Material Components](https://github.com/material-components/material-components-android) and is licenced under Apache 2.0
