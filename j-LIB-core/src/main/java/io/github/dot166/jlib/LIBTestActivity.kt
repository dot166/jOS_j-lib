package io.github.dot166.jlib

import android.os.Bundle
import io.github.dot166.jlib.app.jActivity
import io.github.dot166.jlib.utils.ErrorUtils

class LIBTestActivity : jActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ErrorUtils.handle(Exception("GLaDOS: you have completed all available tests, you will now receive cake", Exception("Class no longer exists in $version")), this, "") {
            finish()
        }
    }
}

