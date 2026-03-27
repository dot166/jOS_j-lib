# jLib

## how to use it?

to use jLib in an android app that is built using gradle add the following lines to your apps build.gradle
```
dependencies {
    implementation "io.github.dot166:j-Lib:v104.2.25"
}
```

if you are including jLib in another android library that is built using gradle add the following lines to your libraries build.gradle
```

dependencies {
    api "io.github.dot166:j-Lib:v104.2.25"
}
```

to use jLib in an AOSP Project add the module ```j.Lib``` to the static_libs section of your Android.bp file

### generateBp

if your app uses the [generateBp Plugin](https://github.com/lineage-next/gradle-generatebp) to automatically add the dependencies for AOSP builds from gradle build files (like jLib uses), you would need to use [my fork of generateBp](https://github.com/dot166/gradle-generatebp) due to jLib using ```j.Lib``` as the AOSP dependency name.

to do that change the maven repo line to be ```dot166/gradle-generatebp``` and change the version number to ```1.31.1``` (or whatever the latest version of my fork is)


## folder structure

### j-LIB-core/

gradle library (main code) with AOSP Build Files

### lib-example/

example app

## License

see [LICENSE](LICENSE)
