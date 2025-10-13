# jLib - a common library that (most of) my android apps use

## jLib will work ONLY ON ANDROID OREO (8) AND LATER!! because there is less devices that use Android NOUGAT (7) or older and apps are now dropping support for those versions

## how to use it?

to use jLib in an android app that is built using gradle add the following lines to your project root build.gradle
```
plugins {
    ...
    id 'com.mikepenz.aboutlibraries.plugin' version "13.0.0" apply false // j-LIB dependency
    id 'com.mikepenz.aboutlibraries.plugin.android' version "13.0.0" apply false // j-LIB dependency
}
```

then add the following lines to your apps build.gradle
```
plugins {
    ...
    id 'com.mikepenz.aboutlibraries.plugin' // j-LIB dependency
    id 'com.mikepenz.aboutlibraries.plugin.android' // j-LIB dependency
}

dependencies {
    implementation "io.github.dot166:j-Lib:103"
}
```

if you are including jLib in another android library that is built using gradle add the following lines to your libraries build.gradle
```

dependencies {
    api "io.github.dot166:j-Lib:103"
}
```

to use jLib in an AOSP Project add the module ```j.Lib``` to the static_libs section of your Android.bp file

### generateBp

if your app uses the [generateBp Plugin](https://github.com/lineage-next/gradle-generatebp) to automatically add the dependencies for AOSP builds from gradle build files (like jLib uses), you would need to use [my fork of generateBp](https://github.com/dot166/gradle-generatebp) due to jLib using ```j.Lib``` as the AOSP dependency name.

to do that change the maven repo line to be ```dot166/gradle-generatebp``` and change the version number to ```1.25.1``` (or whatever the latest version of my fork is)


## folder structure

### j-LIB-core/

gradle library (main code) with AOSP Build Files

### lib-example/

example app

### ThemeEngineStub/

Stub apk for the old ThemeEngine feature (with AOSP build files)

### aosp/

sources for aosp features that normally be in a vendor or 'sdk' folder

### rustLib/

rust common library

## License

see [LICENSE](LICENSE)

### NOTICE: There is code in jLib that are from other projects, Those files are licenced under the licence from their respective projects, the files, projects and licences are listed below.

Some of the preference and theme related files are adapted from [The Android Open Source Project](https://source.android.com/) and are licenced under Apache 2.0

birdgame and other game components are adapted from [The Android Open Source Project](https://source.android.com/) and are licenced under Apache 2.0

sensitive-pn is adapted from [LineageOS](https://github.com/LineageOS), see aosp/sensitive-pn/readme.md for more information
