package jOS.Core

import android.os.Bundle

open class jComposeActivity : jActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        configure(null, false, false, false) // compose overrides view content (i think)
        super.onCreate(savedInstanceState)
    }
}
