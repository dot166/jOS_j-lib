# jLib

## how to use it?

to use jLib in an android app that is built using Gradle add the following lines to your apps build.gradle
```
dependencies {
    implementation "io.github.dot166:j-Lib:v104.2.26"
}
```

if you are including jLib in another android library that is built using Gradle add the following lines to your libraries build.gradle
```
dependencies {
    api "io.github.dot166:j-Lib:v104.2.26"
}
```

### a note on versioning

you might notice that the versioning of both this and [SettingsLib](https://github.com/dot166/platform_frameworks_base/tree/16-qpr2/packages/SettingsLib) is a mess.

the reason why is that initially this started at 3.0.0 and was AOSP only, eventually (around 3.2.6) this was added to maven.

from then to around 4.4.2, I tried my best to follow [semantic versioning](https://semver.org/)
but, with 88 (it really should have been 4.4.3 or 4.5.0), I changed the versioning to match what [GrapheneOS](https://grapheneos.org) is using for their apps,
turns out, single number versioning (no clue what the proper name for it is) does not work well with a library like this.

so I swapped to some weird cross between semver and [pride versioning](https://pridever.org/), but, because of the previous, that started at 104, that's why the major version is that high.

SettingsLib started at 104.2.1 because it initially matched the jLib version, because it was initially only a dependency of jLib, but, that makes no sense to anyone else.

so, starting with jLib 105, I will try to use semver again, with SettingsLib 136, it will sort of use semver,
its major version will be the AOSP SDK version that its sources match add 100 (because maven is doing its job, example, Android 16 - 36 - 136), the minor version will match AOSPs minor version,
and the patch version will be the build time in the following format ```yyyyMMddHHmm```.


## folder structure

### j-LIB-core/

Gradle library (main code)

### lib-example/

example app

## License

see [LICENSE](LICENSE)
