package jOS.Core;

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.github.appintro.AppIntro2


class sdkplaceholder : AppIntro2() {

    companion object {
        private const val intro_v1_12_0_completed = "intro_v1_12_0_completed"

        private const val SLIDE_ID_WELCOME = "SLIDE_ID_WELCOME"
        private const val SLIDE_ID_TEST = "SLIDE_ID_TEST"
        private const val SLIDE_ID_SUCCESS = "SLIDE_ID_SUCCESS"
    }


    private lateinit var mPreferences: SharedPreferences

    private var istestSlide = false

    private var testcompleted = false

    private var color = 550000


    private var welcomeSlide = 0
    private var testSlide = 0
    private var successSlide = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setImmersiveMode()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        isWizardMode = true
        isColorTransitionsEnabled = true

        // dont allow the intro to be bypassed
        isSystemBackButtonLocked = true


        var maxSlideId = 1

        addSlide(
            IdentifiableAppIntroFragment.createInstance(
                title = "jSDK",
                description = "The application you are using relies on the jSDK",
                imageDrawable = R.drawable.ic_launcher_jOS,
                backgroundColorRes = color,
                id = SLIDE_ID_WELCOME
            ))
        welcomeSlide = maxSlideId
        maxSlideId++

        addSlide(
            IdentifiableAppIntroFragment.createInstance(
                title = "Test SDK",
                description = "Push next to test if the sdk is working",
                imageDrawable = R.drawable.ic_launcher_jOS,
                backgroundColorRes = color,
                id = SLIDE_ID_TEST
            ))
        testSlide = maxSlideId
        maxSlideId++

        addSlide(
            IdentifiableAppIntroFragment.createInstance(
                title = "Success",
                description = "SDK test has completed",
                imageDrawable = R.drawable.ic_launcher_jOS,
                backgroundColorRes = color,
                id = SLIDE_ID_SUCCESS
            ))
        successSlide = maxSlideId
        maxSlideId++
    }

    override fun onCanRequestNextPage(): Boolean {

        if(istestSlide && testcompleted) {
            return true
        }

        if(istestSlide) {
            requestSDKTest()
            // dont allow slide to continue, this is a hard requirement
            return false
        }

        return super.onCanRequestNextPage()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .putBoolean(intro_v1_12_0_completed, true)
            .apply()
        finish()
    }

    private fun requestSDKTest() {
        startActivity(Intent(this@sdkplaceholder, test::class.java))
        testcompleted = true
        goToNextSlide(false)
    }
}
