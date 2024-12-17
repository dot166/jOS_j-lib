package io.github.dot166.jLib.app

import android.os.Bundle

open class jComposeActivity : jActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        configure(null, false, false) // compose overrides view content (i think)
        super.onCreate(savedInstanceState)
    }
}
