package jOS.Core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup.PreferencePositionCallback
import androidx.preference.PreferenceScreen
import jOS.Core.ThemeEngine.currentTheme
import jOS.Core.ThemeEngine.getThemeFromDB1
import jOS.Core.utils.PreferenceHighlighter

/**
 * Settings activity for Launcher. Currently implements the following setting: Allow rotation
 */
class jConfigActivity : jActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback,
    PreferenceFragmentCompat.OnPreferenceStartScreenCallback {
    fun preferenceFragmentValue(): Int {
        return R.string.settings_fragment_name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        configure(R.layout.settings_activity, false)
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val intent = intent

        if (savedInstanceState == null) {
            var args = intent.getBundleExtra(EXTRA_FRAGMENT_ARGS)
            if (args == null) {
                args = Bundle()
            }

            val highlight = intent.getStringExtra(EXTRA_FRAGMENT_HIGHLIGHT_KEY)
            if (!TextUtils.isEmpty(highlight)) {
                args.putString(EXTRA_FRAGMENT_HIGHLIGHT_KEY, highlight)
            }
            val root = intent.getStringExtra(EXTRA_FRAGMENT_ROOT_KEY)
            if (!TextUtils.isEmpty(root)) {
                args.putString(EXTRA_FRAGMENT_ROOT_KEY, root)
            }

            val fm = supportFragmentManager
            val f = fm.fragmentFactory.instantiate(
                classLoader,
                getString(preferenceFragmentValue())
            )
            f.arguments = args
            // Display the fragment as the main content.
            fm.beginTransaction().replace(R.id.content_frame, f).commit()
        }
    }

    private fun startPreference(fragment: String?, args: Bundle, key: String): Boolean {
        if (supportFragmentManager.isStateSaved) {
            // Sometimes onClick can come after onPause because of being posted on the handler.
            // Skip starting new preferences in that case.
            return false
        }
        val fm = supportFragmentManager
        val f = fm.fragmentFactory.instantiate(classLoader, fragment!!)
        if (f is DialogFragment) {
            f.setArguments(args)
            f.show(fm, key)
        } else {
            startActivity(
                Intent(this, this.javaClass)
                    .putExtra(EXTRA_FRAGMENT_ARGS, args)
            )
        }
        return true
    }

    override fun onPreferenceStartFragment(
        preferenceFragment: PreferenceFragmentCompat, pref: Preference
    ): Boolean {
        return startPreference(pref.fragment, pref.extras, pref.key)
    }

    override fun onPreferenceStartScreen(
        caller: PreferenceFragmentCompat,
        pref: PreferenceScreen
    ): Boolean {
        val args = Bundle()
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref.key)
        return startPreference(getString(preferenceFragmentValue()), args, pref.key)
    }


    /**
     * This fragment shows the launcher preferences.
     */
    class LauncherSettingsFragment : PreferenceFragmentCompat() {
        private var mHighLightKey: String? = null
        private var mPreferenceHighlighted = false
        val isSDKConfig: Boolean
            get() = false

        fun hideSDK(): Boolean {
            return false
        }

        fun preferenceXML(): Int {
            return R.xml.launcher_preferences
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val args = arguments
            mHighLightKey = args?.getString(EXTRA_FRAGMENT_HIGHLIGHT_KEY)

            if (savedInstanceState != null) {
                mPreferenceHighlighted = savedInstanceState.getBoolean(SAVE_HIGHLIGHTED_KEY)
            }

            initPreference(rootKey)
        }

        fun initPreference(rootKey: String?) {
            setPreferencesFromResource(preferenceXML(), rootKey)
            if (!hideSDK()) {
                addPreferencesFromResource(R.xml.sdk_preference)
            }

            val screen = preferenceScreen
            for (i in screen.preferenceCount - 1 downTo 0) {
                val preference = screen.getPreference(i)
                if (preference.key == "sdk_category") {
                    val category = preference as PreferenceCategory
                    for (i2 in category.preferenceCount - 1 downTo 0) {
                        val preference2 = category.getPreference(i2)
                        if (!configPreference(preference2)) {
                            category.removePreference(preference2)
                        }
                    }
                }
                if (!configPreference(preference)) {
                    screen.removePreference(preference)
                }
            }
            if (activity != null && !TextUtils.isEmpty(preferenceScreen.title)) {
                requireActivity().title = preferenceScreen.title
            }
        }

        /**
         * Initializes a preference. This is called for every preference. Returning false here
         * will remove that preference from the list.
         */
        protected fun configPreference(preference: Preference): Boolean {
            Log.i("Preference Logging", preference.key)
            when (preference.key) {
                "SDK" -> {
                    Log.i("Preference Logging", "SDK Found!!!!")
                    preference.onPreferenceClickListener =
                        Preference.OnPreferenceClickListener { p: Preference? ->
                            val intent = Intent("jOS.System.SDKConfig")
                            startActivity(intent)
                            !isSDKConfig
                        }
                    return !isSDKConfig
                }

                "SDKVer" -> {
                    Log.i("Preference Logging", "SDKVer Found!!!!")
                    preference.summary = BuildConfig.SDKVersion
                    return true
                }
            }
            return extraPrefs(preference)
        }

        protected fun extraPrefs(preference: Preference?): Boolean {
            return true
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val listView: View = listView
            val bottomPadding = listView.paddingBottom
            listView.setOnApplyWindowInsetsListener { v: View, insets: WindowInsets ->
                v.setPadding(
                    v.paddingLeft,
                    v.paddingTop,
                    v.paddingRight,
                    bottomPadding + insets.systemWindowInsetBottom
                )
                insets.consumeSystemWindowInsets()
            }

            // Overriding Text Direction in the Androidx preference library to support RTL
            view.textDirection = View.TEXT_DIRECTION_LOCALE
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putBoolean(SAVE_HIGHLIGHTED_KEY, mPreferenceHighlighted)
        }

        override fun onResume() {
            super.onResume()

            if (isAdded && !mPreferenceHighlighted) {
                val highlighter = createHighlighter()
                if (highlighter != null) {
                    requireView().postDelayed(highlighter, DELAY_HIGHLIGHT_DURATION_MILLIS.toLong())
                    mPreferenceHighlighted = true
                }
            }

            if (currentTheme != getThemeFromDB1(preferenceManager.context)) {
                recreateActivityNow()
            }
        }

        private fun recreateActivityNow() {
            val activity: Activity? = activity
            activity?.recreate()
        }

        private fun createHighlighter(): PreferenceHighlighter? {
            if (TextUtils.isEmpty(mHighLightKey)) {
                return null
            }

            val screen = preferenceScreen ?: return null

            val list = listView
            val callback = list.adapter as PreferencePositionCallback?
            val position = callback!!.getPreferenceAdapterPosition(mHighLightKey!!)
            return if (position >= 0) PreferenceHighlighter(
                list, position, screen.findPreference(mHighLightKey!!)!!
            )
            else null
        }
    }

    companion object {
        const val EXTRA_FRAGMENT_ARGS: String = ":settings:fragment_args"

        // Intent extra to indicate the pref-key to highlighted when opening the settings activity
        const val EXTRA_FRAGMENT_HIGHLIGHT_KEY: String = ":settings:fragment_args_key"

        // Intent extra to indicate the pref-key of the root screen when opening the settings activity
        const val EXTRA_FRAGMENT_ROOT_KEY: String = PreferenceFragmentCompat.ARG_PREFERENCE_ROOT

        private const val DELAY_HIGHLIGHT_DURATION_MILLIS = 600
        const val SAVE_HIGHLIGHTED_KEY: String = "android:preference_highlighted"
    }
}
