# jOS SDK - a common library that (most of) my android apps use

contains a version of the holo theme that is patched for use with appcompat, a custom actionbar baced on material3 Toolbar and some other things.

## PrivExt subfolder can only be built with aosp, rest of sdk can be built with gradle

## the j-SDK will work ONLY ON ANDROID PIE (9) AND LATER!! see [this commit](https://github.com/dot166/jOS_j-sdk/commit/ae05286e8cddad03afdfd88197add1796efc4f17) for more information

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

then add the following lines to your apps build.gradle
```
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
}
```


## folder structure

### core/

gradle library (main code)

### PrivExt/

aosp compiled java library (privileged code) included as javalib.jar in core/j-SDK-core/libs

### Prebuilts/

prebuilt aar of core/ (for use in aosp)

### .m2/

maven repo containing current and older versions (for use in gradle)
