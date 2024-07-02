package jOS.Core

import android.os.Bundle

class SDKChangelogActivity : jWebActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        configure("https://dot166.github.io/jOS/commlib/Changelog/", true, false, true, false, true)
        super.onCreate(savedInstanceState)
    }
}
